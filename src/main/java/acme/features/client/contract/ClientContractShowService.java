
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
public class ClientContractShowService extends AbstractService<Client, Contract> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientContractRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		Boolean status;
		int masterId;
		Contract contract;
		Client client;

		masterId = super.getRequest().getData("id", int.class);
		contract = this.repository.findOneContractById(masterId);
		client = contract == null ? null : contract.getClient();

		status = contract != null && (!contract.isDraftMode() || this.getRequest().getPrincipal().hasRole(client));

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
	public void unbind(final Contract object) {
		assert object != null;

		int id;
		Collection<Project> projects;
		SelectChoices choices;
		Dataset dataset;
		final boolean showProgressLogSButton;

		if (object.isDraftMode())
			projects = this.repository.findAllPublishedProjects(true);
		else {
			id = super.getRequest().getData("id", int.class);
			projects = this.repository.findOneProjectByContractId(id);
		}
		choices = SelectChoices.from(projects, "code", object.getProject());

		dataset = super.unbind(object, "code", "instantiationMoment", "providerName", "customerName", "goals", "budget", "draftMode");
		dataset.put("project", choices.getSelected().getKey());
		dataset.put("projects", choices);

		showProgressLogSButton = super.getRequest().getPrincipal().hasRole(object.getClient());

		super.getResponse().addData(dataset);
		super.getResponse().addGlobal("showProgressLogSButton", showProgressLogSButton);

	}

}
