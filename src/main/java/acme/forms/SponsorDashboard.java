
package acme.forms;

import java.util.Map;

import acme.client.data.AbstractForm;
import acme.client.data.datatypes.Money;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SponsorDashboard extends AbstractForm {

	private static final long	serialVersionUID	= 1L;

	Integer						lowTaxInvoices;
	Integer						linkedSponsorships;

	Map<String, Money>			averageAmountByCurrency;
	Map<String, Money>			amountDeviationByCurrency;
	Map<String, Money>			minAmountByCurrency;
	Map<String, Money>			maxAmountByCurrency;

	Map<String, Money>			averageQuantityByCurrency;
	Map<String, Money>			quantityDeviationByCurrency;
	Map<String, Money>			minQuantityByCurrency;
	Map<String, Money>			maxQuantityByCurrency;

}
