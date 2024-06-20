
package acme.features.client.progressLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.contracts.Contract;
import acme.entities.progressLogs.ProgressLog;
import acme.roles.Client;

@Service
public class ClientProgressLogDeleteService extends AbstractService<Client, ProgressLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientProgressLogRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int id;
		ProgressLog progressLog;
		Client client;

		id = super.getRequest().getData("id", int.class);
		progressLog = this.repository.findOneProgressLogById(id);
		client = progressLog == null ? null : progressLog.getContract().getClient();

		status = progressLog != null && progressLog.isDraftMode() && super.getRequest().getPrincipal().hasRole(client);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ProgressLog object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneProgressLogById(id);

		super.getBuffer().addData(object);

	}

	@Override
	public void bind(final ProgressLog object) {
		assert object != null;

		int progressLogId;
		Contract contract;

		progressLogId = super.getRequest().getData("id", int.class);
		contract = this.repository.findOneContractByProgressLogId(progressLogId);

		super.bind(object, "recordId", "completeness", "comment", "responsiblePerson");
		object.setContract(contract);
	}

	@Override
	public void validate(final ProgressLog object) {
		assert object != null;
	}

	@Override
	public void perform(final ProgressLog object) {
		assert object != null;

		this.repository.delete(object);
	}

	@Override
	public void unbind(final ProgressLog object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "recordId", "completeness", "comment", "registrationMoment", "responsiblePerson", "draftMode");
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);

	}

}
