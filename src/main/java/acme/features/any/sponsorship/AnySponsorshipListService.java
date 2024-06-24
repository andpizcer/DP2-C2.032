
package acme.features.any.sponsorship;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Any;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.sponsorships.Sponsorship;
import acme.util.BooleanLocalizeHelper;

@Service
public class AnySponsorshipListService extends AbstractService<Any, Sponsorship> {

	@Autowired
	private AnySponsorshipRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Sponsorship> objects;

		objects = this.repository.findAllPublishedSponsorships();

		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Sponsorship object) {
		assert object != null;

		Dataset dataset;
		Locale locale;

		dataset = super.unbind(object, "code", "amount", "startDate", "endDate");
		locale = super.getRequest().getLocale();

		dataset.put("published", BooleanLocalizeHelper.localizeBoolean(locale, !object.isDraftMode()));

		super.getResponse().addData(dataset);
	}

}
