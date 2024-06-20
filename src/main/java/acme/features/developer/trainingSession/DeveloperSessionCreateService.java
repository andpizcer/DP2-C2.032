
package acme.features.developer.trainingSession;

import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.modules.Module;
import acme.entities.sessions.Session;
import acme.roles.Developer;

@Service
public class DeveloperSessionCreateService extends AbstractService<Developer, Session> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperSessionRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int moduleId;
		Developer developer;
		Module module;

		moduleId = super.getRequest().getData("moduleId", int.class);
		module = this.repository.findOneModuleById(moduleId);
		developer = module == null ? null : module.getDeveloper();
		status = module != null && module.isDraftMode() && super.getRequest().getPrincipal().hasRole(developer);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Session object;
		int moduleId;
		Module module;

		Principal principal;
		Developer developer;
		principal = super.getRequest().getPrincipal();
		developer = this.repository.findDeveloperById(principal.getActiveRoleId());

		moduleId = super.getRequest().getData("moduleId", int.class);
		module = this.repository.findOneModuleById(moduleId);

		object = new Session();
		object.setDraftMode(true);
		object.setTrainingModule(module);
		object.setDeveloper(developer);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Session object) {
		assert object != null;

		super.bind(object, "code", "fechaInicio", "fechaFinal", "location", "instructor", "email", "link");
	}

	@Override
	public void validate(final Session object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Session existing;

			existing = this.repository.findOneSessionByCode(object.getCode());
			super.state(existing == null, "code", "developer.training-session.form.error.duplicated");
		}

		if (object.getFechaFinal() != null && !super.getBuffer().getErrors().hasErrors("fechaInicio")) {
			Date startLimit;
			startLimit = MomentHelper.deltaFromMoment(object.getTrainingModule().getCreationMoment(), 1, ChronoUnit.WEEKS);
			super.state(MomentHelper.isAfterOrEqual(object.getFechaInicio(), startLimit), "fechaInicio", "developer.training-session.form.error.invalid-start-date");
			Date endlimit;
			endlimit = MomentHelper.deltaFromMoment(object.getFechaInicio(), 1, ChronoUnit.WEEKS);
			super.state(MomentHelper.isAfterOrEqual(object.getFechaFinal(), endlimit), "fechaInicio", "developer.training-session.form.error.invalid-start-date-2");
			Date maxDate;
			maxDate = MomentHelper.parse("2200-12-31 23:59", "yyyy-MM-dd HH:mm");
			super.state(MomentHelper.isBeforeOrEqual(object.getFechaInicio(), maxDate), "fechaInicio", "developer.training-session.form.error.max-date");
		}

		if (object.getFechaInicio() != null && !super.getBuffer().getErrors().hasErrors("fechaFinal")) {
			Date endlimit;
			endlimit = MomentHelper.deltaFromMoment(object.getFechaInicio(), 1, ChronoUnit.WEEKS);
			super.state(MomentHelper.isAfterOrEqual(object.getFechaFinal(), endlimit), "fechaFinal", "developer.training-session.form.error.invalid-end-date");
			Date maxDate;
			maxDate = MomentHelper.parse("2200-12-31 23:59", "yyyy-MM-dd HH:mm");
			super.state(MomentHelper.isBeforeOrEqual(object.getFechaFinal(), maxDate), "fechaFinal", "developer.training-session.form.error.max-date");
		}
	}

	@Override
	public void perform(final Session object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Session object) {
		assert object != null;

		Dataset dataset;
		dataset = super.unbind(object, "code", "fechaInicio", "fechaFinal", "location", "instructor", "email", "link", "draftMode");
		dataset.put("moduleId", super.getRequest().getData("moduleId", int.class));

		super.getResponse().addData(dataset);

	}

}
