
package acme.features.manager.project;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.projects.Project;
import acme.roles.Manager;

@Controller
public class ManagerProjectController extends AbstractController<Manager, Project> {

	//Internal State
	@Autowired
	private ManagerProjectListService		listService;

	@Autowired
	private ManagerProjectShowService		showService;

	@Autowired
	private ManagerProjectCreateService		createService;

	@Autowired
	private ManagerProjectUpdateService		updateService;

	@Autowired
	private ManagerProjectDeleteService		deleteService;

	@Autowired
	private ManagerProjectPublishService	publishService;


	//Constructors
	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);

		super.addCustomCommand("publish", "update", this.publishService);
	}
}
