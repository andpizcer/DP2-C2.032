
package acme.features.developer.trainingSession;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.sessions.Session;
import acme.roles.Developer;

@Service
public class DeveloperSessionListService extends AbstractService<Developer, Session> {

	//Internal state
	@Autowired
	private DeveloperSessionRepository repository;


	//AbstractService interface
	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Developer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Session> objects;
		Principal principal;

		principal = super.getRequest().getPrincipal();
		objects = this.repository.findAllSessionsByDeveloperId(principal.getActiveRoleId());
		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Session object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "draftMode", "trainingModule");

		super.getResponse().addData(dataset);
	}

}
