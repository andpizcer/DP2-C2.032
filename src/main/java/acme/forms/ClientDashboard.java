
package acme.forms;

import java.util.Collection;
import java.util.Map;

import acme.client.data.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDashboard extends AbstractForm {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private int					progressLogsWithCompletenessRateBelow25;
	private int					progressLogsWithCompletenessRateBetween25And50;
	private int					progressLogsWithCompletenessRateBetween50And75;
	private int					progressLogsWithCompletenessRateAbove75;

	private Map<String, Double>	contractsBudgetAverageByCurrency;
	private Map<String, Double>	contractsBudgetDeviationByCurrency;

	private Map<String, Double>	contractsMinBudgetByCurrency;
	private Map<String, Double>	contractsMaxBudgetByCurrency;

	private Collection<String>	currencies;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
