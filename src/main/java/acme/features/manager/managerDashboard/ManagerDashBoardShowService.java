
package acme.features.manager.managerDashboard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.userStories.Priority;
import acme.forms.ManagerDashboard;
import acme.roles.Manager;

@Service
public class ManagerDashBoardShowService extends AbstractService<Manager, ManagerDashboard> {

	//Internal state
	@Autowired
	private ManagerDashBoardRepository repository;


	//AbstractService interface
	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		ManagerDashboard object;
		int managerId;

		managerId = super.getRequest().getPrincipal().getActiveRoleId();
		object = new ManagerDashboard();

		object.setMustStories(this.repository.countUserStoriesOfPriority(Priority.MUST, managerId));
		object.setShouldStories(this.repository.countUserStoriesOfPriority(Priority.SHOULD, managerId));
		object.setCouldStories(this.repository.countUserStoriesOfPriority(Priority.COULD, managerId));
		object.setWontStories(this.repository.countUserStoriesOfPriority(Priority.WONT, managerId));
		object.setAverageEstCost(this.repository.averageEstimatedCostUserStories(managerId));
		object.setDeviationEstCost(this.repository.deviationEstimatedCostUserStories(managerId));
		object.setMinimumEstCost(this.repository.minEstimatedCostUserStories(managerId));
		object.setMaximumEstCost(this.repository.maxEstimatedCostUserStories(managerId));

		Set<String> conjuntoDeCurrencies;
		conjuntoDeCurrencies = this.repository.currenciesOfProjectsOfManager(managerId);

		Map<String, List<Double>> metricsByCurrency;
		metricsByCurrency = new HashMap<>();
		for (String key : conjuntoDeCurrencies) {
			List<Double> metrics = new ArrayList<>();
			metrics.add(this.repository.averageCostProjects(managerId, key));
			metrics.add(this.repository.deviationCostProjects(managerId, key));
			metrics.add(this.repository.minCostProjects(managerId, key));
			metrics.add(this.repository.maxCostProjects(managerId, key));
			metricsByCurrency.put(key, metrics);

		}
		object.setMetricsOfProjectsByCurrency(metricsByCurrency);

		super.getBuffer().addData(object);

	}

	@Override
	public void unbind(final ManagerDashboard object) {
		assert object != null;
		Dataset dataset;

		dataset = super.unbind(object, "mustStories", "shouldStories", "couldStories", "wontStories", "wontStories", "averageEstCost", "deviationEstCost", "minimumEstCost", "maximumEstCost", "metricsOfProjectsByCurrency");

		super.getResponse().addData(dataset);
	}
}
