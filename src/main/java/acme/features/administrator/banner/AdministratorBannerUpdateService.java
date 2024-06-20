
package acme.features.administrator.banner;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Administrator;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.banner.Banner;

@Service
public class AdministratorBannerUpdateService extends AbstractService<Administrator, Banner> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorBannerRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Banner banner;

		masterId = super.getRequest().getData("id", int.class);
		banner = this.repository.findOneBannerById(masterId);

		status = banner != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Banner object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneBannerById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Banner object) {
		assert object != null;
		Date moment;
		moment = MomentHelper.getCurrentMoment();

		object.setInstantiationUpdateMoment(moment);

		super.bind(object, "type", "displayPeriodStart", "displayPeriodEnd", "slogan", "picture", "link");
		object.setInstantiationUpdateMoment(moment);
	}

	@Override
	public void validate(final Banner object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("displayPeriodStart") && !super.getBuffer().getErrors().hasErrors("displayPeriodEnd")) {

			Date moment = MomentHelper.parse("2101/01/01 00:00", "yyyy/MM/dd HH:mm");

			final boolean startBefore2101 = MomentHelper.isAfter(moment, object.getDisplayPeriodStart());
			super.state(startBefore2101, "displayPeriodStart", "administrator.banner.form.error.start-after-2100");

			final boolean endBefore2101 = MomentHelper.isAfter(moment, object.getDisplayPeriodStart());
			super.state(endBefore2101, "displayPeriodEnd", "administrator.banner.form.error.end-after-2100");

			final boolean startAfterInstantiationUpdate = MomentHelper.isAfter(object.getDisplayPeriodStart(), object.getInstantiationUpdateMoment());
			super.state(startAfterInstantiationUpdate, "displayPeriodStart", "administrator.banner.form.error.start-before-instantiationUpdate");

			final boolean startBeforeEnd = MomentHelper.isAfter(object.getDisplayPeriodEnd(), object.getDisplayPeriodStart());
			super.state(startBeforeEnd, "displayPeriodEnd", "administrator.banner.form.error.start-before-end");

			final boolean oneWeekBetweenStartAndEnd = MomentHelper.isLongEnough(object.getDisplayPeriodStart(), object.getDisplayPeriodEnd(), 7, ChronoUnit.DAYS);
			super.state(oneWeekBetweenStartAndEnd, "displayPeriodEnd", "administrator.banner.form.error.one-week");

		}

		if (super.getBuffer().getErrors().hasErrors("type"))
			super.state(false, "type", "administrator.banner.form.error.wrong-type");

	}

	@Override
	public void perform(final Banner object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Banner object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "type", "instantiationUpdateMoment", "displayPeriodStart", "displayPeriodEnd", "slogan", "picture", "link");

		super.getResponse().addData(dataset);
	}

}
