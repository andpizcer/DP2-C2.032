
package acme.features.sponsor.invoice;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.invoices.Invoice;
import acme.entities.sponsorships.Sponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorInvoiceDeleteService extends AbstractService<Sponsor, Invoice> {

	@Autowired
	SponsorInvoiceRepository repository;


	@Override
	public void authorise() {
		int id;
		Invoice invoice;
		Sponsor owner;
		boolean status;
		boolean isDraftMode;

		id = super.getRequest().getData("id", int.class);
		invoice = this.repository.findOneInvoiceById(id);
		owner = invoice.getSponsorship().getSponsor();
		status = super.getRequest().getPrincipal().hasRole(owner);

		isDraftMode = invoice.isDraftMode();
		status = status && isDraftMode;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Invoice object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneInvoiceById(id);

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
	}

	@Override
	public void perform(final Invoice object) {
		assert object != null;

		this.repository.delete(object);
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
		dataset.put("totalAmount", object.totalAmount());
		dataset.put("sponsorships", sponsorshipChoices);
		dataset.put("sponsorship", sponsorshipChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}

}
