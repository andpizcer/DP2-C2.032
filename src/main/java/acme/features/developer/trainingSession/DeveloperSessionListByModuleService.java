
package acme.features.developer.trainingSession;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.modules.Module;
import acme.entities.sessions.Session;
import acme.roles.Developer;

@Service
public class DeveloperSessionListByModuleService extends AbstractService<Developer, Session> {

	//Internal state
	@Autowired
	private DeveloperSessionRepository repository;


	//AbstractService interface
	@Override
	public void authorise() {
		boolean status;

		int moduleId;
		Module module;
		Developer developer;

		moduleId = super.getRequest().getData("moduleId", int.class);
		module = this.repository.findOneModuleById(moduleId);
		developer = module == null ? null : module.getDeveloper();

		status = module != null && super.getRequest().getPrincipal().hasRole(developer);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Session> objects;
		int moduleId;

		moduleId = super.getRequest().getData("moduleId", int.class);
		objects = this.repository.findAllSessionsByModuleId(moduleId);
		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Session object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "draftMode", "trainingModule");

		super.getResponse().addData(dataset);
	}

	@Override
	public void unbind(final Collection<Session> objects) {
		assert objects != null;

		int moduleId;
		Module module;
		Boolean createBut;

		moduleId = super.getRequest().getData("moduleId", int.class);
		module = this.repository.findOneModuleById(moduleId);
		createBut = module.isDraftMode();

		super.getResponse().addGlobal("createBut", createBut);
		super.getResponse().addGlobal("moduleId", moduleId);
	}

}
