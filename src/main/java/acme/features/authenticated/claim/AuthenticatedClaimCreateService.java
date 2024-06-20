
package acme.features.authenticated.claim;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Authenticated;
import acme.client.data.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractService;
import acme.entities.claims.Claim;

@Service
public class AuthenticatedClaimCreateService extends AbstractService<Authenticated, Claim> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private AuthenticatedClaimRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		Claim object;
		Date moment;

		moment = MomentHelper.getCurrentMoment();

		object = new Claim();
		object.setCode("");
		object.setInstantiationMoment(moment);
		object.setHeading("");
		object.setDescription("");
		object.setDepartment("");
		object.setEmail("");
		object.setLink("");

		super.getBuffer().addData(object);
	}

	@Override
	public void bind(final Claim object) {
		assert object != null;

		super.bind(object, "code", "instantiationMoment", "heading", "description", "department", "email", "link");
	}

	@Override
	public void validate(final Claim object) {
		assert object != null;

		boolean confirmation;

		confirmation = super.getRequest().getData("confirmation", boolean.class);
		super.state(confirmation, "confirmation", "authenticated.claim.form.error.confirmation");

		if (!super.getBuffer().getErrors().hasErrors("code")) {
			Claim claim;
			Boolean status;

			claim = this.repository.findClaimByCode(object.getCode());
			status = claim != null && claim.getId() != object.getId();
			super.state(!status, "code", "manager.claim.form.error.duplicated");
		}
	}

	@Override
	public void perform(final Claim object) {
		assert object != null;

		Date moment;

		moment = MomentHelper.getCurrentMoment();
		object.setInstantiationMoment(moment);
		this.repository.save(object);
	}

	@Override
	public void unbind(final Claim object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "instantiationMoment", "heading", "description", "department", "email", "link");
		dataset.put("confirmation", false);
		dataset.put("readonly", false);

		super.getResponse().addData(dataset);
	}
}
