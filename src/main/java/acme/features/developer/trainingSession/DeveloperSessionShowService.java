
package acme.features.developer.trainingSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.modules.Module;
import acme.entities.sessions.Session;
import acme.roles.Developer;

@Service
public class DeveloperSessionShowService extends AbstractService<Developer, Session> {

	//Internal state
	@Autowired
	private DeveloperSessionRepository repository;


	//AbstractService interface
	@Override
	public void authorise() {
		boolean status;
		int sessionId;
		Session session;
		Module module;
		Developer developer;

		sessionId = super.getRequest().getData("id", int.class);
		session = this.repository.findOneSessionById(sessionId);
		module = this.repository.findOneModuleBySessionId(sessionId);
		developer = session == null ? null : module.getDeveloper();
		status = session != null && super.getRequest().getPrincipal().hasRole(developer);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Session object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneSessionById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Session object) {
		assert object != null;
		Dataset dataset;
		dataset = super.unbind(object, "code", "fechaInicio", "fechaFinal", "location", "instructor", "email", "link", "trainingModule", "draftMode");
		super.getResponse().addData(dataset);
	}

}
