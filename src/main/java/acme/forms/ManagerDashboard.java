/*
 * The system must handle manager dashboards with the following data: total number of
 * “must”, “should”, “could”, and “won’t” user stories; average, deviation, minimum,
 * and maximum estimated cost of the user stories; average, deviation, minimum, and
 * maximum cost of the projects.
 */

package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

// CLASE QUE NO PERSISTE EN BASE DE DATOS Y QUE SE UTILIZARÁ EN SERVICIOS

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	//USERSTORIES
	Integer						mustStories;
	Integer						shouldStories;
	Integer						couldStories;
	Integer						wontStories;

	//ESTIMATED COST IN USERSTORIES
	Double						averageEstCost;
	Double						deviationEstCost;
	Integer						minimumEstCost;
	Integer						maximumEstCost;

	//PROJECT COST
	/*
	 * Double averageCost;
	 * Double deviationCost;
	 * Double minimumCost;
	 * Double maximumCost;
	 */

	Map<String, List<Double>>	metricsOfProjectsByCurrency;

}
