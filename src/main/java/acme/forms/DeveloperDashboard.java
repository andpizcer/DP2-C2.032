
package acme.forms;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeveloperDashboard extends AbstractForm {

	//Serialisation identifier

	private static final long	serialVersionUID	= 1L;

	//Attributes

	private Integer				trainingModulesWithUpdateMoment;
	private Integer				trainingSessionsWithLink;

	private Double				averageTimeOfTrainingModules;
	private Double				deviationTimeOfTrainingModules;
	private Double				minimumTimeOfTrainingModules;
	private Double				maximumTimeOfTrainingModules;
}
