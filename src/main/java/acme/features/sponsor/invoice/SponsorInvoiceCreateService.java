
package acme.features.sponsor.invoice;

import java.time.temporal.ChronoUnit;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.invoices.Invoice;
import acme.entities.sponsorships.Sponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorInvoiceCreateService extends AbstractService<Sponsor, Invoice> {

	@Autowired
	private SponsorInvoiceRepository repository;


	@Override
	public void authorise() {
		int sponsorshipId;
		Sponsorship sponsorship;
		boolean status;

		if (this.getRequest().hasData("sponsorship", int.class)) {
			sponsorshipId = super.getRequest().getData("sponsorship", int.class);
			if (sponsorshipId != 0) {
				sponsorship = this.repository.findOneSponsorshipById(sponsorshipId);
				//User must own the sponsorship
				status = super.getRequest().getPrincipal().hasRole(sponsorship.getSponsor());
				//The sponsorship cannot be published
				status = status && sponsorship.isDraftMode();
			} else
				status = true;
		} else
			status = true;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Invoice object;

		object = new Invoice();
		object.setDraftMode(true);
		object.setRegistrationTime(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Invoice object) {
		assert object != null;

		int sponsorshipId;
		Sponsorship sponsorship;

		sponsorshipId = super.getRequest().getData("sponsorship", int.class);
		sponsorship = this.repository.findOneSponsorshipById(sponsorshipId);

		super.bind(object, "code", "dueDate", "quantity", "tax", "link");
		object.setSponsorship(sponsorship);
	}

	@Override
	public void validate(final Invoice object) {
		assert object != null;
		boolean condition;

		/*
		 * if (!super.getBuffer().getErrors().hasErrors("sponsorshipPublished") && object.getSponsorship() != null) {
		 * condition = object.getSponsorship().isDraftMode();
		 * super.state(condition, "sponsorshipPublished", "sponsor.invoice.form.error.sponsorshipPublished");
		 * }
		 */

		if (!super.getBuffer().getErrors().hasErrors("dueDate") && object.getDueDate() != null) {
			condition = MomentHelper.isAfter(object.getDueDate(), object.getRegistrationTime());
			super.state(condition, "dueDate", "sponsor.invoice.form.error.dueDateAfterRegistartion");
		}
		if (!super.getBuffer().getErrors().hasErrors("dueDate") && object.getDueDate() != null) {
			condition = MomentHelper.isLongEnough(object.getRegistrationTime(), object.getDueDate(), 30, ChronoUnit.DAYS);
			super.state(condition, "dueDate", "sponsor.invoice.form.error.oneMonthLong");
		}
		if (!super.getBuffer().getErrors().hasErrors("quantity") && object.getQuantity() != null) {
			condition = object.getQuantity().getAmount() > 0;
			super.state(condition, "quantity", "sponsor.invoice.form.error.positiveNotNoughtQuantity");
		}

		if (!super.getBuffer().getErrors().hasErrors("quantity") && object.getSponsorship() != null && object.getQuantity() != null) {
			String sponsorshipCurrency = object.getSponsorship().getAmount().getCurrency();
			condition = object.getQuantity().getCurrency().equals(sponsorshipCurrency);
			super.state(condition, "quantity", "sponsor.invoice.form.error.differentCurrency");
		}

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Invoice existingInvoice;

			existingInvoice = this.repository.findOneInvoiceByCode(object.getCode());
			condition = existingInvoice == null;
			super.state(condition, "code", "sponsor.invoice.form.error.duplicateCode");
		}
	}

	@Override
	public void perform(final Invoice object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Invoice object) {
		assert object != null;

		int sponsorId;
		Collection<Sponsorship> availableSponsorships;
		SelectChoices sponsorshipChoices;
		Dataset dataset;

		sponsorId = super.getRequest().getPrincipal().getActiveRoleId();
		availableSponsorships = this.repository.findManySponsorshipsInDraftModeBySponsorId(sponsorId);
		sponsorshipChoices = SelectChoices.from(availableSponsorships, "code", object.getSponsorship());

		dataset = super.unbind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "link", "draftMode");
		dataset.put("sponsorships", sponsorshipChoices);
		dataset.put("sponsorship", sponsorshipChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
