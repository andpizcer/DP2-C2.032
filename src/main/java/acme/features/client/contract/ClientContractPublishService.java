
package acme.features.client.contract;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.contracts.Contract;
import acme.entities.projects.Project;
import acme.roles.Client;

@Service
public class ClientContractPublishService extends AbstractService<Client, Contract> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientContractRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Contract contract;
		Client client;

		masterId = super.getRequest().getData("id", int.class);
		contract = this.repository.findOneContractById(masterId);
		client = contract == null ? null : contract.getClient();

		status = contract != null && contract.isDraftMode() && super.getRequest().getPrincipal().hasRole(client);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Contract object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneContractById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Contract object) {
		assert object != null;

		int projectId;
		Project project;

		projectId = super.getRequest().getData("project", int.class);
		project = this.repository.findOneProjectById(projectId);

		super.bind(object, "code", "providerName", "customerName", "goals", "budget");
		object.setProject(project);
	}

	@Override
	public void validate(final Contract object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Contract existing;

			existing = this.repository.findOneContractByCode(object.getCode());
			super.state(existing == null || existing.equals(object), "code", "client.contract.form.error.duplicated");
		}

		// In case I try to associate a contract to a project that is not published
		if (!super.getBuffer().getErrors().hasErrors("project"))
			super.state(object.getProject().isPublished() == true, "project", "client.contract.form.error.wrong-project");

		if (!super.getBuffer().getErrors().hasErrors("budget"))
			super.state(object.getBudget().getAmount() >= 0, "budget", "client.contract.form.error.wrong-budget");

		if (!super.getBuffer().getErrors().hasErrors("budget") && !super.getBuffer().getErrors().hasErrors("project")) {
			Project project;

			project = this.repository.findOneProjectById(object.getProject().getId());
			super.state(project.getCost().getCurrency().equals(object.getBudget().getCurrency()), "budget", "client.contract.form.error.wrong-budget-currency-publish");
			// In case the contract exceeds the allowable cost
			super.state(this.checkAllBudgetsAreLessThanProjectCost(object), "budget", "client.contract.form.error.too-much-budget-publish");
		}

	}

	private boolean checkAllBudgetsAreLessThanProjectCost(final Contract object) {
		assert object != null;

		int projectId;
		Double budgetContractsSameProject;
		Double projectCost;
		Double totalBudgetContracts;

		projectId = object.getProject().getId();
		projectCost = this.repository.findOneProjectById(projectId).getCost().getAmount();
		budgetContractsSameProject = this.repository.sumAllBudgetsOfPublishContracts(projectId) == null //
			? 0
			: this.repository.sumAllBudgetsOfPublishContracts(projectId);
		totalBudgetContracts = budgetContractsSameProject + object.getBudget().getAmount();

		if (totalBudgetContracts.doubleValue() <= projectCost.doubleValue())
			return true;

		return false;

	}

	@Override
	public void perform(final Contract object) {
		assert object != null;

		object.setDraftMode(false);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Contract object) {
		assert object != null;

		Collection<Project> projects;
		SelectChoices choices;
		Dataset dataset;

		projects = this.repository.findAllPublishedProjects(true);
		choices = SelectChoices.from(projects, "code", object.getProject());

		dataset = super.unbind(object, "code", "instantiationMoment", "providerName", "customerName", "goals", "budget", "draftMode");
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);

		super.getResponse().addData(dataset);
	}

}
