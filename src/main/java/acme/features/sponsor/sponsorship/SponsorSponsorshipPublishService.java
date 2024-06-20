
package acme.features.sponsor.sponsorship;

import java.time.temporal.ChronoUnit;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.invoices.Invoice;
import acme.entities.projects.Project;
import acme.entities.sponsorships.Sponsorship;
import acme.entities.sponsorships.SponsorshipType;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorshipPublishService extends AbstractService<Sponsor, Sponsorship> {

	@Autowired
	private SponsorSponsorshipRepository repository;


	@Override
	public void authorise() {
		int id;
		Sponsorship sponsorship;
		Sponsor owner;
		boolean status;
		boolean isDraftMode;
		int projectId;
		Project project;

		id = super.getRequest().getData("id", int.class);
		sponsorship = this.repository.findOneSponsorshipById(id);
		owner = sponsorship.getSponsor();
		//User must own this sponsorship 
		status = this.getRequest().getPrincipal().hasRole(owner);

		isDraftMode = sponsorship.isDraftMode();
		//This sponsorship cannot be published
		status = status && isDraftMode;

		if (super.getRequest().hasData("project", int.class)) {
			projectId = super.getRequest().getData("project", int.class);
			if (projectId != 0) {
				project = this.repository.findOneProjectById(projectId);

				//Project must be published
				status = status && project.isPublished();
			}
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Sponsorship object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneSponsorshipById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Sponsorship object) {
		assert object != null;

		int projectId;
		Project project;
		String typeKey;
		SponsorshipType type;

		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.findOneProjectById(projectId);

		typeKey = super.getRequest().getData("type", String.class);
		type = typeKey.equals("0") ? null : SponsorshipType.valueOf(typeKey);

		super.bind(object, "code", "startDate", "endDate", "amount", "email", "link");
		object.setProject(project);
		object.setType(type);
	}

	@Override
	public void validate(final Sponsorship object) {
		assert object != null;

		boolean condition;

		if (!super.getBuffer().getErrors().hasErrors("endDate") && object.getStartDate() != null && object.getEndDate() != null) {
			condition = MomentHelper.isAfter(object.getEndDate(), object.getStartDate());
			super.state(condition, "endDate", "sponsor.sponsorship.form.error.endDateAfterStartDate");
		}
		if (!super.getBuffer().getErrors().hasErrors("endDate") && object.getStartDate() != null && object.getEndDate() != null) {
			condition = MomentHelper.isLongEnough(object.getStartDate(), object.getEndDate(), 30, ChronoUnit.DAYS);
			super.state(condition, "endDate", "sponsor.sponsorship.form.error.oneMonthLong");
		}
		if (!super.getBuffer().getErrors().hasErrors("startDate") && object.getStartDate() != null) {
			condition = MomentHelper.isAfter(object.getStartDate(), object.getMoment());
			super.state(condition, "startDate", "sponsor.sponsorship.form.error.afterMoment");
		}
		if (!super.getBuffer().getErrors().hasErrors("amount") && object.getAmount() != null) {
			condition = object.getAmount().getAmount() >= 0;
			super.state(condition, "amount", "sponsor.sponsorship.form.error.positiveAmount");
		}
		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Sponsorship existingSponsorship;

			existingSponsorship = this.repository.findOneSponsorshipByCode(object.getCode());
			condition = existingSponsorship == null || existingSponsorship.getId() == object.getId();
			super.state(condition, "code", "sponsor.sponsorship.form.error.duplicateCode");
		}
		if (!super.getBuffer().getErrors().hasErrors("amount")) {
			Collection<Invoice> invoices;
			String invoiceCurrency;

			invoices = this.repository.findManyInvoicesBySponsorshipId(object.getId());
			if (!invoices.isEmpty()) {
				invoiceCurrency = invoices.stream().findAny().orElseThrow().getQuantity().getCurrency();
				condition = object.getAmount().getCurrency().equals(invoiceCurrency);
				super.state(condition, "amount", "sponsor.sponsorship.form.error.differentCurrency");
			}
		}

		Collection<Invoice> invoices = null;
		double amountSumFromInvoices;
		invoices = this.repository.findManyInvoicesBySponsorshipId(object.getId());

		if (!super.getBuffer().getErrors().hasErrors("*")) {
			condition = invoices.stream().noneMatch(Invoice::isDraftMode);
			super.state(condition, "*", "sponsor.sponsorship.form.error.unpublishedInvoices");
		}

		if (!super.getBuffer().getErrors().hasErrors("*") && object.getAmount() != null) {
			amountSumFromInvoices = invoices.stream().mapToDouble(i -> i.totalAmount().getAmount()).sum();
			condition = object.getAmount().getAmount().equals(amountSumFromInvoices);
			super.state(condition, "*", "sponsor.sponsorship.form.error.unexpectedTotalAmount");
		}
	}

	@Override
	public void perform(final Sponsorship object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Sponsorship object) {
		assert object != null;

		Collection<Project> availableProjects;
		SelectChoices projectChoices;
		SelectChoices typeChoices;
		Dataset dataset;

		availableProjects = this.repository.findAllPublishedProjects();
		projectChoices = SelectChoices.from(availableProjects, "code", object.getProject());
		typeChoices = SelectChoices.from(SponsorshipType.class, object.getType());

		dataset = super.unbind(object, "code", "moment", "startDate", "endDate", "amount", "type", "draftMode", "email", "link");
		dataset.put("projects", projectChoices);
		dataset.put("project", projectChoices.getSelected().getKey());
		dataset.put("types", typeChoices);
		dataset.put("type", typeChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
