
package acme.features.sponsor.invoice;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.invoices.Invoice;
import acme.entities.sponsorships.Sponsorship;
import acme.roles.Sponsor;
import acme.util.BooleanLocalizeHelper;

@Service
public class SponsorInvoiceListService extends AbstractService<Sponsor, Invoice> {

	@Autowired
	SponsorInvoiceRepository repository;


	@Override
	public void authorise() {
		int sponsorshipId;
		Sponsorship sponsorship;
		Sponsor owner;
		boolean status;

		sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		sponsorship = this.repository.findOneSponsorshipById(sponsorshipId);
		owner = sponsorship.getSponsor();
		status = super.getRequest().getPrincipal().hasRole(owner);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Invoice> objects;
		int sponsorshipId;

		sponsorshipId = super.getRequest().getData("sponsorshipId", int.class);
		objects = this.repository.findManyInvoicesBySponsorshipId(sponsorshipId);

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Invoice object) {
		assert object != null;

		Dataset dataset;
		Locale locale;

		dataset = super.unbind(object, "code", "dueDate", "quantity");

		locale = super.getRequest().getLocale();
		dataset.put("published", BooleanLocalizeHelper.localizeBoolean(locale, !object.isDraftMode()));

		super.getResponse().addData(dataset);
	}

}
