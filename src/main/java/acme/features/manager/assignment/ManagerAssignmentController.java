
package acme.features.manager.assignment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.assignments.Assignment;
import acme.roles.Manager;

@Controller
public class ManagerAssignmentController extends AbstractController<Manager, Assignment> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ManagerCreateAssignmentService	createService;
	@Autowired
	private ManagerAssignmentDeleteService	deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
	}

}
