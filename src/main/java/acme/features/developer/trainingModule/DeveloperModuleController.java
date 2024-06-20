
package acme.features.developer.trainingModule;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.modules.Module;
import acme.roles.Developer;

@Controller
public class DeveloperModuleController extends AbstractController<Developer, Module> {

	//Internal State

	@Autowired
	private DeveloperModuleListService		listService;

	@Autowired
	private DeveloperModuleShowService		showService;

	@Autowired
	private DeveloperModuleCreateService	createService;

	@Autowired
	private DeveloperModuleUpdateService	updateService;

	@Autowired
	private DeveloperModuleDeleteService	deleteService;

	@Autowired
	private DeveloperModulePublishService	publishService;

	//Constructors


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);

		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
