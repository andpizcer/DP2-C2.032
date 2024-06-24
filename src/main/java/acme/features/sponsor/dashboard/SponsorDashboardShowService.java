
package acme.features.sponsor.dashboard;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.datatypes.Money;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.SponsorDashboard;
import acme.roles.Sponsor;

@Service
public class SponsorDashboardShowService extends AbstractService<Sponsor, SponsorDashboard> {

	@Autowired
	SponsorDashboardRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		SponsorDashboard dashboard;
		Integer lowTaxInvoices;
		Integer linkedSponsorships;
		Map<String, Money> averageAmountByCurrency;
		Map<String, Money> amountDeviationByCurrency;
		Map<String, Money> minAmountByCurrency;
		Map<String, Money> maxAmountByCurrency;
		Map<String, Money> averageQuantityByCurrency;
		Map<String, Money> quantityDeviationByCurrency;
		Map<String, Money> minQuantityByCurrency;
		Map<String, Money> maxQuantityByCurrency;

		int sponsorId = super.getRequest().getPrincipal().getActiveRoleId();

		lowTaxInvoices = this.repository.lowTaxInvoicesCount(sponsorId);
		linkedSponsorships = this.repository.linkedSponsorshipsCount(sponsorId);
		averageAmountByCurrency = this.repository.averageAmountByCurrency(sponsorId);
		amountDeviationByCurrency = this.repository.amountDeviationByCurrency(sponsorId);
		minAmountByCurrency = this.repository.minAmountByCurrency(sponsorId);
		maxAmountByCurrency = this.repository.maxAmountByCurrency(sponsorId);
		averageQuantityByCurrency = this.repository.averageQuantityByCurrency(sponsorId);
		quantityDeviationByCurrency = this.repository.quantityDeviationByCurrency(sponsorId);
		minQuantityByCurrency = this.repository.minQuantityByCurrency(sponsorId);
		maxQuantityByCurrency = this.repository.maxQuantityByCurrency(sponsorId);

		dashboard = new SponsorDashboard();
		dashboard.setLowTaxInvoices(lowTaxInvoices);
		dashboard.setLinkedSponsorships(linkedSponsorships);
		dashboard.setAverageAmountByCurrency(averageAmountByCurrency);
		dashboard.setAmountDeviationByCurrency(amountDeviationByCurrency);
		dashboard.setMinAmountByCurrency(minAmountByCurrency);
		dashboard.setMaxAmountByCurrency(maxAmountByCurrency);
		dashboard.setAverageQuantityByCurrency(averageQuantityByCurrency);
		dashboard.setQuantityDeviationByCurrency(quantityDeviationByCurrency);
		dashboard.setMinQuantityByCurrency(minQuantityByCurrency);
		dashboard.setMaxQuantityByCurrency(maxQuantityByCurrency);

		super.getBuffer().addData(dashboard);
	}

	@Override
	public void unbind(final SponsorDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, //
			"lowTaxInvoices", "linkedSponsorships", "averageAmountByCurrency",//
			"amountDeviationByCurrency", "minAmountByCurrency", "maxAmountByCurrency",//
			"averageQuantityByCurrency", "quantityDeviationByCurrency", //
			"minQuantityByCurrency", "maxQuantityByCurrency");

		super.getResponse().addData(dataset);
	}
}
