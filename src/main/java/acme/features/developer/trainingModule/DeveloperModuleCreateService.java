
package acme.features.developer.trainingModule;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.modules.Module;
import acme.entities.modules.ModuleDifficulty;
import acme.entities.projects.Project;
import acme.roles.Developer;

@Service
public class DeveloperModuleCreateService extends AbstractService<Developer, Module> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperModuleRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Developer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Module object;
		Principal principal;
		Developer developer;

		principal = super.getRequest().getPrincipal();
		developer = this.repository.findDeveloperById(principal.getActiveRoleId());

		object = new Module();
		object.setDraftMode(true);
		object.setDeveloper(developer);
		object.setCreationMoment(MomentHelper.getCurrentMoment());
		object.setUpdateMoment(null);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Module object) {
		assert object != null;

		int projectId;
		Project project;

		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.getProjectById(projectId);

		super.bind(object, "code", "details", "difficultyLevel", "link", "totalTime");
		object.setProject(project);
	}

	@Override
	public void validate(final Module object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Module existing;

			existing = this.repository.findOneModuleByCode(object.getCode());
			super.state(existing == null, "code", "developer.training-module.form.error.duplicated");
		}
	}

	@Override
	public void perform(final Module object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Module object) {
		assert object != null;

		SelectChoices choices;
		SelectChoices projectChoices;

		choices = SelectChoices.from(ModuleDifficulty.class, object.getDifficultyLevel());
		Collection<Project> projects;

		projects = this.repository.getAllPublishedProjects();
		projectChoices = SelectChoices.from(projects, "code", object.getProject());

		Dataset dataset;
		dataset = super.unbind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "link", "totalTime", "draftMode");
		dataset.put("moduleDifficulty", choices);
		dataset.put("project", projectChoices.getSelected().getKey());
		dataset.put("projects", projectChoices);
		super.getResponse().addData(dataset);

	}
}
