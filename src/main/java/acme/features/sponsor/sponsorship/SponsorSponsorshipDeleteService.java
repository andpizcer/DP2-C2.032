
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.invoices.Invoice;
import acme.entities.projects.Project;
import acme.entities.sponsorships.Sponsorship;
import acme.entities.sponsorships.SponsorshipType;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorshipDeleteService extends AbstractService<Sponsor, Sponsorship> {

	@Autowired
	private SponsorSponsorshipRepository repository;


	@Override
	public void authorise() {
		int id;
		Sponsorship sponsorship;
		Sponsor owner;
		boolean status;
		boolean isDraftMode;

		id = super.getRequest().getData("id", int.class);
		sponsorship = this.repository.findOneSponsorshipById(id);
		owner = sponsorship.getSponsor();
		//User must own this invoice
		status = this.getRequest().getPrincipal().hasRole(owner);

		isDraftMode = sponsorship.isDraftMode();
		//This invoice cannot be published
		status = status && isDraftMode;

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
		type = SponsorshipType.valueOf(typeKey);

		super.bind(object, "code", "startDate", "endDate", "amount", "email", "link");
		object.setProject(project);
		object.setType(type);
	}

	@Override
	public void validate(final Sponsorship object) {
		assert object != null;

		boolean condition;

		Collection<Invoice> invoices;

		invoices = this.repository.findManyInvoicesBySponsorshipId(object.getId());
		condition = invoices.stream().allMatch(Invoice::isDraftMode);
		super.state(condition, "*", "sponsorship.sponsor.form.error.publishedInvoices");
	}

	@Override
	public void perform(final Sponsorship object) {
		assert object != null;

		Collection<Invoice> invoices;

		invoices = this.repository.findManyInvoicesBySponsorshipId(object.getId());
		this.repository.deleteAll(invoices);
		this.repository.delete(object);
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
