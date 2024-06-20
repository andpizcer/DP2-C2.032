
package acme.features.sponsor.invoice;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.invoices.Invoice;
import acme.entities.sponsorships.Sponsorship;
import acme.roles.Sponsor;

@Service
public class SponsorInvoiceShowService extends AbstractService<Sponsor, Invoice> {

	@Autowired
	SponsorInvoiceRepository repository;


	@Override
	public void authorise() {
		int id;
		Invoice invoice;
		Sponsor owner;
		boolean status;

		id = super.getRequest().getData("id", int.class);
		invoice = this.repository.findOneInvoiceById(id);
		owner = invoice.getSponsorship().getSponsor();
		status = super.getRequest().getPrincipal().hasRole(owner);

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
	public void unbind(final Invoice object) {
		assert object != null;

		int sponsorId;
		Collection<Sponsorship> availableSponsorships;
		SelectChoices sponsorshipChoices;
		Dataset dataset;

		sponsorId = super.getRequest().getPrincipal().getActiveRoleId();
		if (object.isDraftMode())
			availableSponsorships = this.repository.findManySponsorshipsInDraftModeBySponsorId(sponsorId);
		else
			availableSponsorships = List.of(object.getSponsorship());
		sponsorshipChoices = SelectChoices.from(availableSponsorships, "code", object.getSponsorship());

		dataset = super.unbind(object, "code", "registrationTime", "dueDate", "quantity", "tax", "link", "draftMode");
		dataset.put("totalAmount", object.totalAmount());
		dataset.put("sponsorships", sponsorshipChoices);
		dataset.put("sponsorship", sponsorshipChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
