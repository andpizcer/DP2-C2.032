
package acme.features.sponsor.invoice;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.client.controllers.AbstractController;
import acme.entities.invoices.Invoice;
import acme.roles.Sponsor;

@Controller
public class SponsorInvoiceController extends AbstractController<Sponsor, Invoice> {

	@Autowired
	private SponsorInvoiceListService		listService;

	@Autowired
	private SponsorInvoiceShowService		showService;

	@Autowired
	private SponsorInvoiceCreateService		createService;

	@Autowired
	private SponsorInvoiceDeleteService		deleteService;

	@Autowired
	private SponsorInvoiceUpdateService		updateService;

	@Autowired
	private SponsorInvoicePublishService	publishService;


	@PostConstruct
	protected void initialize() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addBasicCommand("update", this.updateService);

		super.addCustomCommand("publish", "update", this.publishService);
	}
}
