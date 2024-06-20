
package acme.features.manager.managerDashboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.entities.projects.Project;
import acme.entities.userStories.Priority;
import acme.entities.userStories.UserStory;
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

		Collection<Project> projects = this.repository.allProjectsManager(managerId);
		Collection<UserStory> userStories = this.repository.allUserStoriesManager(managerId);

		object.setMustStories(0);
		object.setShouldStories(0);
		object.setCouldStories(0);
		object.setWontStories(0);
		object.setAverageEstCost(Double.NaN);
		object.setDeviationEstCost(Double.NaN);
		object.setMaximumEstCost(0);
		object.setMinimumEstCost(0);

		if (!userStories.isEmpty()) {
			object.setMustStories(this.repository.countUserStoriesOfPriority(Priority.MUST, managerId));
			object.setShouldStories(this.repository.countUserStoriesOfPriority(Priority.SHOULD, managerId));
			object.setCouldStories(this.repository.countUserStoriesOfPriority(Priority.COULD, managerId));
			object.setWontStories(this.repository.countUserStoriesOfPriority(Priority.WONT, managerId));
			object.setAverageEstCost(this.repository.averageEstimatedCostUserStories(managerId));
			object.setDeviationEstCost(this.repository.deviationEstimatedCostUserStories(managerId));
			object.setMinimumEstCost(this.repository.minEstimatedCostUserStories(managerId));
			object.setMaximumEstCost(this.repository.maxEstimatedCostUserStories(managerId));
		}

		if (!projects.isEmpty()) {
			Set<String> conjuntoDeCurrencies;
			conjuntoDeCurrencies = new HashSet<>();
			for (Project p : projects)
				if (!conjuntoDeCurrencies.contains(p.getCost().getCurrency()))
					conjuntoDeCurrencies.add(p.getCost().getCurrency());

			Map<String, List<Double>> metricsByCurrency;
			metricsByCurrency = new HashMap<>();
			for (String key : conjuntoDeCurrencies) {
				List<Double> metrics = new ArrayList<>();
				metrics.add(this.repository.averageCostProjects(managerId, key));
				metrics.add(this.repository.deviationCostProjects(managerId, key));
				metrics.add(this.repository.maxCostProjects(managerId, key));
				metrics.add(this.repository.minCostProjects(managerId, key));
				metricsByCurrency.put(key, metrics);

			}
			object.setMetricsOfProjectsByCurrency(metricsByCurrency);

		}
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
