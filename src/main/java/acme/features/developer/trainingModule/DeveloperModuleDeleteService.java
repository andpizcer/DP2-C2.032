
package acme.features.developer.trainingModule;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.modules.Module;
import acme.entities.modules.ModuleDifficulty;
import acme.entities.projects.Project;
import acme.entities.sessions.Session;
import acme.roles.Developer;

@Service
public class DeveloperModuleDeleteService extends AbstractService<Developer, Module> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private DeveloperModuleRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int moduleId;
		Module module;
		Developer developer;

		moduleId = super.getRequest().getData("id", int.class);
		module = this.repository.findOneModuleById(moduleId);
		developer = module == null ? null : module.getDeveloper();
		status = module != null && module.isDraftMode() && super.getRequest().getPrincipal().hasRole(developer);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Module object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneModuleById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Module object) {
		assert object != null;

		int projectId;
		Project project;

		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.getProjectById(projectId);

		super.bind(object, "code", "creationMoment", "details", "difficultyLevel", "updateMoment", "link", "totalTime");
		object.setProject(project);
	}

	@Override
	public void validate(final Module object) {
		assert object != null;
	}

	@Override
	public void perform(final Module object) {
		assert object != null;

		Collection<Session> sessions;

		sessions = this.repository.findSessionsByModuleId(object.getId());
		this.repository.deleteAll(sessions);
		this.repository.delete(object);
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
