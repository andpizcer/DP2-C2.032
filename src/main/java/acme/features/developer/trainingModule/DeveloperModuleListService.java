
package acme.features.developer.trainingModule;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.modules.Module;
import acme.roles.Developer;

@Service
public class DeveloperModuleListService extends AbstractService<Developer, Module> {

	//Internal state
	@Autowired
	private DeveloperModuleRepository repository;


	//AbstractService interface
	@Override
	public void authorise() {
		boolean status;

		status = super.getRequest().getPrincipal().hasRole(Developer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<Module> objects;
		Principal principal;

		principal = super.getRequest().getPrincipal();
		objects = this.repository.findAllModulesByDeveloperId(principal.getActiveRoleId());
		super.getBuffer().addData(objects);
	}

	@Override
	public void unbind(final Module object) {
		assert object != null;

		Dataset dataset;

		dataset = super.unbind(object, "code", "draftMode", "project");

		super.getResponse().addData(dataset);
	}
}
