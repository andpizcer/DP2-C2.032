
package acme.features.manager.userstory;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.assignments.Assignment;
import acme.entities.userStories.Priority;
import acme.entities.userStories.UserStory;
import acme.roles.Manager;

@Service
public class ManagerUserStoryDeleteService extends AbstractService<Manager, UserStory> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerUserStoryRepository repository;


	// AbstractService interface ----------------------------------------------
	@Override
	public void authorise() {
		boolean status;
		int userStoryId;
		Manager manager;
		UserStory userStory;
		Principal principal;

		userStoryId = super.getRequest().getData("id", int.class);
		userStory = this.repository.findUserStoryById(userStoryId);
		principal = super.getRequest().getPrincipal();
		manager = this.repository.findManagerById(principal.getActiveRoleId());
		status = userStory != null && userStory.getManager().equals(manager) && !userStory.isPublished();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void bind(final UserStory object) {
		assert object != null;

		Manager manager;
		manager = object.getManager();

		super.bind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priority", "link");
		object.setManager(manager);
	}

	@Override
	public void load() {
		UserStory object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findUserStoryById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void validate(final UserStory object) {
		assert object != null;
	}

	@Override
	public void perform(final UserStory object) {
		assert object != null;

		Collection<Assignment> assignments = this.repository.findAssignmentByUserStoryId(object.getId());
		this.repository.deleteAll(assignments);
		this.repository.delete(object);
	}

	@Override
	public void unbind(final UserStory object) {
		assert object != null;
		SelectChoices choices;
		Dataset dataset;

		choices = SelectChoices.from(Priority.class, object.getPriority());
		dataset = super.unbind(object, "title", "description", "estimatedCost", "acceptanceCriteria", "priority", "link");
		dataset.put("statuses", choices);
		super.getResponse().addData(dataset);
	}
}
