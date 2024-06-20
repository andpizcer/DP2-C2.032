
package acme.features.developer.dashboard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.accounts.Principal;
import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.forms.DeveloperDashboard;
import acme.roles.Developer;

@Service
public class DeveloperDashboardShowService extends AbstractService<Developer, DeveloperDashboard> {

	// Internal state ---------------------------------------------------------
	@Autowired
	protected DeveloperDashboardsRepository repository;

	// AbstractService Interface ----------------------------------------------


	@Override
	public void authorise() {
		boolean status;

		Principal principal = super.getRequest().getPrincipal();
		int id = principal.getAccountId();
		Developer developer = this.repository.findDeveloperByUserId(id);
		status = developer != null && principal.hasRole(Developer.class);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		final Principal principal;
		int userId;

		principal = super.getRequest().getPrincipal();
		userId = principal.getAccountId();

		Integer trainingModulesWithUpdateMoment = this.repository.getTrainingModulesWithUpdateMoment(userId);
		Integer trainingSessionsWithLink = this.repository.getTrainingSessionsWithLink(userId);

		Double averageTimeOfTrainingModules = this.repository.getAverageTimeOfTrainingModules(userId);
		Double deviationTimeOfTrainingModules = this.repository.getDeviationTimeOfTrainingModules(userId);
		Double minimumTimeOfTrainingModules = this.repository.getMinimumTimeOfTrainingModules(userId);
		Double maximumTimeOfTrainingModules = this.repository.getMaximumTimeOfTrainingModules(userId);

		if (averageTimeOfTrainingModules != null && deviationTimeOfTrainingModules != null && minimumTimeOfTrainingModules != null && maximumTimeOfTrainingModules != null) {

			final DeveloperDashboard dashboard = new DeveloperDashboard();

			dashboard.setTrainingModulesWithUpdateMoment(trainingModulesWithUpdateMoment);
			dashboard.setTrainingSessionsWithLink(trainingSessionsWithLink);
			dashboard.setAverageTimeOfTrainingModules(averageTimeOfTrainingModules);
			dashboard.setDeviationTimeOfTrainingModules(deviationTimeOfTrainingModules);
			dashboard.setMinimumTimeOfTrainingModules(minimumTimeOfTrainingModules);
			dashboard.setMaximumTimeOfTrainingModules(maximumTimeOfTrainingModules);

			super.getBuffer().addData(dashboard);
		} else {

			final DeveloperDashboard dashboard = new DeveloperDashboard();

			dashboard.setTrainingModulesWithUpdateMoment(trainingModulesWithUpdateMoment);
			dashboard.setTrainingSessionsWithLink(trainingSessionsWithLink);
			dashboard.setAverageTimeOfTrainingModules(0.0);
			dashboard.setDeviationTimeOfTrainingModules(0.0);
			dashboard.setMinimumTimeOfTrainingModules(0.0);
			dashboard.setMaximumTimeOfTrainingModules(0.0);

			super.getBuffer().addData(dashboard);
		}
	}

	@Override
	public void unbind(final DeveloperDashboard object) {
		Dataset dataset;

		dataset = super.unbind(object, "trainingModulesWithUpdateMoment", "trainingSessionsWithLink", "averageTimeOfTrainingModules", "deviationTimeOfTrainingModules", "minimumTimeOfTrainingModules", "maximumTimeOfTrainingModules");

		super.getResponse().addData(dataset);
	}

}
