
package acme.features.manager.assignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.assignments.Assignment;
import acme.entities.projects.Project;
import acme.entities.userStories.UserStory;
import acme.roles.Manager;

@Service
public class ManagerCreateAssignmentService extends AbstractService<Manager, Assignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerAssignmentRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int userStoryId;
		Manager manager;
		UserStory userStory;
		Principal principal;

		userStoryId = super.getRequest().getData("userStoryId", int.class);
		userStory = this.repository.findUserStoryById(userStoryId);
		principal = super.getRequest().getPrincipal();
		manager = this.repository.findManagerById(principal.getActiveRoleId());
		status = userStory != null && userStory.getManager().equals(manager);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Assignment object;
		int userStoryId;
		UserStory userStory;

		object = new Assignment();
		userStoryId = super.getRequest().getData("userStoryId", int.class);
		userStory = this.repository.findUserStoryById(userStoryId);
		object.setUserStory(userStory);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Assignment object) {
		assert object != null;

		int projectId;
		Project project;

		projectId = super.getRequest().getData("projectId", int.class);
		project = this.repository.findProjectById(projectId);
		object.setProject(project);
	}

	@Override
	public void validate(final Assignment object) {
		assert object != null;
		Project project;
		UserStory userStory;
		Assignment existing;

		project = object.getProject();
		userStory = object.getUserStory();

		super.state(project != null, "projectId", "manager.assignment.form.error.empty");

		if (!super.getBuffer().getErrors().hasErrors("projectId")) {
			existing = this.repository.findAssingmentByProjectIdAndUserStoryId(project.getId(), userStory.getId());

			super.state(!project.isPublished(), "*", "manager.assignment.form.error.published");
			super.state(project.getManager().equals(userStory.getManager()), "*", "manager.assignment.form.error.manager");
			super.state(existing == null, "*", "manager.assignment.form.error.already-linked");
		}

	}

	@Override
	public void perform(final Assignment object) {
		assert object != null;

		this.repository.save(object);
	}

	@Override
	public void unbind(final Assignment object) {
		assert object != null;

		int userStoryId;
		int managerId;
		UserStory userStory;
		Collection<Project> projects;
		SelectChoices choices;
		Dataset dataset;

		userStoryId = super.getRequest().getData("userStoryId", int.class);
		userStory = this.repository.findUserStoryById(userStoryId);
		managerId = super.getRequest().getPrincipal().getActiveRoleId();
		projects = this.repository.findUnpublishedProjectsOfManager(managerId);
		choices = SelectChoices.from(projects, "code", object.getProject());

		dataset = super.unbind(object, "project", "userStory");
		dataset.put("userStoryTitle", userStory.getTitle());
		dataset.put("projectCodes", choices);

		super.getResponse().addData(dataset);
	}

}
