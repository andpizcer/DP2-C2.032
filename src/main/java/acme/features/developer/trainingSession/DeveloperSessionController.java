
package acme.features.developer.trainingSession;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.sessions.Session;
import acme.roles.Developer;

@Controller
public class DeveloperSessionController extends AbstractController<Developer, Session> {

	//Internal State

	@Autowired
	private DeveloperSessionListService			listService;

	@Autowired
	private DeveloperSessionShowService			showService;

	@Autowired
	private DeveloperSessionCreateService		createService;

	@Autowired
	private DeveloperSessionListByModuleService	listByModuleService;

	@Autowired
	private DeveloperSessionUpdateService		updateService;

	@Autowired
	private DeveloperSessionDeleteService		deleteService;

	@Autowired
	private DeveloperSessionPublishService		publishService;

	//Constructors


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addCustomCommand("list-by-module", "list", this.listByModuleService);

		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("delete", this.deleteService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
