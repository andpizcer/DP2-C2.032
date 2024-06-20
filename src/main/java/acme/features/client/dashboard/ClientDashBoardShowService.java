
package acme.features.client.dashboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.ClientDashboard;
import acme.roles.Client;

@Service
public class ClientDashBoardShowService extends AbstractService<Client, ClientDashboard> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private ClientDashboardRepository repository;

	// AbstractService interface ----------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		ClientDashboard clientDashboard;
		int progressLogsWithCompletenessRateBelow25;
		int progressLogsWithCompletenessRateBetween25And50;
		int progressLogsWithCompletenessRateBetween50And75;
		int progressLogsWithCompletenessRateAbove75;

		Map<String, Double> contractsBudgetAverageByCurrency = new HashMap<>();
		Map<String, Double> contractsBudgetDeviationByCurrency = new HashMap<>();

		Map<String, Double> contractsMinBudgetByCurrency = new HashMap<>();
		Map<String, Double> contractsMaxBudgetByCurrency = new HashMap<>();

		Collection<String> currencies;

		int clientId;
		clientId = super.getRequest().getPrincipal().getActiveRoleId();

		progressLogsWithCompletenessRateBelow25 = this.repository.progressLogsWithCompletenessRateBelow25(clientId);
		progressLogsWithCompletenessRateBetween25And50 = this.repository.progressLogsWithCompletenessRateBetween25And50(clientId);
		progressLogsWithCompletenessRateBetween50And75 = this.repository.progressLogsWithCompletenessRateBetween50And75(clientId);
		progressLogsWithCompletenessRateAbove75 = this.repository.progressLogsWithCompletenessRateAbove75(clientId);

		clientDashboard = new ClientDashboard();
		clientDashboard.setProgressLogsWithCompletenessRateBelow25(progressLogsWithCompletenessRateBelow25);
		clientDashboard.setProgressLogsWithCompletenessRateBetween25And50(progressLogsWithCompletenessRateBetween25And50);
		clientDashboard.setProgressLogsWithCompletenessRateBetween50And75(progressLogsWithCompletenessRateBetween50And75);
		clientDashboard.setProgressLogsWithCompletenessRateAbove75(progressLogsWithCompletenessRateAbove75);

		currencies = this.repository.findAllCurrencies(clientId);
		clientDashboard.setCurrencies(currencies);
		for (String currency : currencies) {
			Double contractsBudgetAverage = this.repository.contractsBudgetAverage(clientId, currency);
			Double contractsBudgetDeviation = this.repository.contractsBudgetDeviation(clientId, currency);

			Double contractsMinBudget = this.repository.contractsMinBudget(clientId, currency);
			Double contractsMaxBudget = this.repository.contractsMaxBudget(clientId, currency);

			contractsBudgetAverageByCurrency.put(currency, contractsBudgetAverage);
			contractsBudgetDeviationByCurrency.put(currency, contractsBudgetDeviation);
			contractsMinBudgetByCurrency.put(currency, contractsMinBudget);
			contractsMaxBudgetByCurrency.put(currency, contractsMaxBudget);

			clientDashboard.setContractsBudgetAverageByCurrency(contractsBudgetAverageByCurrency);
			clientDashboard.setContractsBudgetDeviationByCurrency(contractsBudgetDeviationByCurrency);
			clientDashboard.setContractsMinBudgetByCurrency(contractsMinBudgetByCurrency);
			clientDashboard.setContractsMaxBudgetByCurrency(contractsMaxBudgetByCurrency);

		}

		super.getBuffer().addData(clientDashboard);
	}

	@Override
	public void unbind(final ClientDashboard object) {

		Dataset dataset;

		dataset = super.unbind(object, "progressLogsWithCompletenessRateBelow25", "progressLogsWithCompletenessRateBetween25And50", //
			"progressLogsWithCompletenessRateBetween50And75", "progressLogsWithCompletenessRateAbove75", "contractsBudgetAverageByCurrency", "contractsBudgetDeviationByCurrency", //
			"contractsMinBudgetByCurrency", "contractsMaxBudgetByCurrency", "currencies");

		super.getResponse().addData(dataset);

	}

}
