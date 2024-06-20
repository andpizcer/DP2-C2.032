
package acme.features.sponsor.sponsorship;

import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.sponsorships.Sponsorship;
import acme.roles.Sponsor;
import acme.util.BooleanLocalizeHelper;

@Service
public class SponsorSponsorshipListService extends AbstractService<Sponsor, Sponsorship> {

	@Autowired
	private SponsorSponsorshipRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Collection<Sponsorship> objects;
		int sponsorId;

		sponsorId = super.getRequest().getPrincipal().getActiveRoleId();
		objects = this.repository.findManySponsorshipsBySponsorId(sponsorId);

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
