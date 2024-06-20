
package acme.features.client.progressLog;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.contracts.Contract;
import acme.entities.progressLogs.ProgressLog;
import acme.roles.Client;

@Service
public class ClientProgressLogCreateService extends AbstractService<Client, ProgressLog> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientProgressLogRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Contract contract;

		masterId = super.getRequest().getData("masterId", int.class);
		contract = this.repository.findOneContractById(masterId);
		status = contract != null && !contract.isDraftMode() && super.getRequest().getPrincipal().hasRole(contract.getClient());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ProgressLog object;
		int masterId;
		Contract contract;
		Date moment;

		moment = MomentHelper.getCurrentMoment();
		masterId = super.getRequest().getData("masterId", int.class);
		contract = this.repository.findOneContractById(masterId);

		object = new ProgressLog();
		object.setRegistrationMoment(moment);
		object.setDraftMode(true);
		object.setContract(contract);

		super.getBuffer().addData(object);

	}

	@Override
	public void bind(final ProgressLog object) {
		assert object != null;

		super.bind(object, "recordId", "completeness", "comment", "responsiblePerson");

	}

	@Override
	public void validate(final ProgressLog object) {
		assert object != null;

		if (!super.getBuffer().getErrors().hasErrors("recordId")) {
			ProgressLog existing;

			existing = this.repository.findOneProgressLogByRecordId(object.getRecordId());
			super.state(existing == null, "recordId", "client.progress-log.form.error.duplicated");
		}

		//In case I try to create a progress log with a lesser completeness than the highest one published
		if (!super.getBuffer().getErrors().hasErrors("completeness")) {
			Double maxCompletenessOfAPublishedProgressLogByContractId;
			Double maxReal;

			maxCompletenessOfAPublishedProgressLogByContractId = this.repository.findMaxCompletenessOfAPublishedProgressLogByContractId(object.getContract().getId());
			maxReal = maxCompletenessOfAPublishedProgressLogByContractId == null ? 0 : maxCompletenessOfAPublishedProgressLogByContractId;

			super.state(object.getCompleteness() > maxReal, "completeness", "client.contract.form.error.wrong-completeness");
		}

	}

	@Override
	public void perform(final ProgressLog object) {
		assert object != null;

		this.repository.save(object);
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
