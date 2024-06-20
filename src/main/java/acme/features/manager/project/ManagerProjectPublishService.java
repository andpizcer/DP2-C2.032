
package acme.features.manager.project;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.Project;
import acme.entities.userStories.UserStory;
import acme.roles.Manager;

@Service
public class ManagerProjectPublishService extends AbstractService<Manager, Project> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerProjectRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int projectId;
		Manager manager;
		Project project;
		Principal principal;

		projectId = super.getRequest().getData("id", int.class);
		project = this.repository.findOneProjectById(projectId);
		principal = super.getRequest().getPrincipal();
		manager = this.repository.findManagerById(principal.getActiveRoleId());
		status = project != null && project.getManager().equals(manager) && !project.isPublished();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Project object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneProjectById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Project object) {
		assert object != null;

		super.bind(object, "code", "title", "abstractInfo", "indication", "cost", "link");
	}

	@Override
	public void validate(final Project object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Project existing;

			existing = this.repository.findProjectByCode(object.getCode());
			super.state(existing == null || existing.equals(object), "code", "manager.project.form.error.duplicated");
		}

		if (!super.getBuffer().getErrors().hasErrors("cost")) {
			Double cantidad = object.getCost().getAmount();
			super.state(cantidad > -1, "cost", "manager.project.form.error.negative-cost");
		}

		Collection<UserStory> userStories;

		userStories = this.repository.findUserStoriesByProjectId(object.getId());
		super.state(!userStories.isEmpty(), "*", "manager.project.form.error.no-user-stories");
		super.state(userStories.stream().allMatch(UserStory::isPublished), "*", "manager.project.form.error.unpublished-user-stories");
		super.state(!object.getIndication(), "indication", "manager.project.form.error.indication");

	}

	@Override
	public void perform(final Project object) {
		assert object != null;

		object.setPublished(true);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Project object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "published", "code", "title", "abstractInfo", "indication", "cost", "link");

		super.getResponse().addData(dataset);
	}

}
