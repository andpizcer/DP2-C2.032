
package acme.features.sponsor.dashboard;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.data.datatypes.Money;
import acme.client.repositories.AbstractRepository;

@Repository
public interface SponsorDashboardRepository extends AbstractRepository {

	@Query("SELECT count(i) FROM Invoice i WHERE i.tax <= 21")
	int lowTaxInvoicesCount();

	@Query("SELECT count(s) FROM Sponsorship s WHERE s.link != null")
	int linkedSponsorshipsCount();

	//Sponsorship amount metrics

	@Query("SELECT s.amount.currency, avg(s.amount.amount) FROM Sponsorship s GROUP BY s.amount.currency")
	List<Object[]> averageAmountByCurrencyRaw();

	default Map<String, Money> averageAmountByCurrency() {
		List<Object[]> raw = this.averageAmountByCurrencyRaw();
		return this.convertObjectListToCurrencyMap(raw);
	}

	@Query("SELECT s.amount.currency, sqrt( avg(s.amount.amount*s.amount.amount) - avg(s.amount.amount)*avg(s.amount.amount) ) FROM Sponsorship s GROUP BY s.amount.currency")
	List<Object[]> amountDeviationByCurrencyRaw();

	default Map<String, Money> amountDeviationByCurrency() {
		List<Object[]> raw = this.amountDeviationByCurrencyRaw();
		return this.convertObjectListToCurrencyMap(raw);
	}

	@Query("SELECT s.amount.currency, min(s.amount.amount) FROM Sponsorship s GROUP BY s.amount.currency")
	List<Object[]> minAmountByCurrencyRaw();

	default Map<String, Money> minAmountByCurrency() {
		List<Object[]> raw = this.minAmountByCurrencyRaw();
		return this.convertObjectListToCurrencyMap(raw);
	}

	@Query("SELECT s.amount.currency, max(s.amount.amount) FROM Sponsorship s GROUP BY s.amount.currency")
	List<Object[]> maxAmountByCurrencyRaw();

	default Map<String, Money> maxAmountByCurrency() {
		List<Object[]> raw = this.maxAmountByCurrencyRaw();
		return this.convertObjectListToCurrencyMap(raw);
	} //Invoice quantity metrics

	@Query("SELECT i.quantity.currency, avg(i.quantity.amount) FROM Invoice i GROUP BY i.quantity.currency")
	List<Object[]> averageQuantityByCurrencyRaw();

	default Map<String, Money> averageQuantityByCurrency() {
		List<Object[]> raw = this.averageQuantityByCurrencyRaw();
		return this.convertObjectListToCurrencyMap(raw);
	}

	@Query("SELECT i.quantity.currency, sqrt( avg(i.quantity.amount*i.quantity.amount) - avg(i.quantity.amount)*avg(i.quantity.amount) ) FROM Invoice i GROUP BY i.quantity.currency")
	List<Object[]> quantityDeviationByCurrencyRaw();

	default Map<String, Money> quantityDeviationByCurrency() {
		List<Object[]> raw = this.quantityDeviationByCurrencyRaw();
		return this.convertObjectListToCurrencyMap(raw);
	}

	@Query("SELECT i.quantity.currency, min(i.quantity.amount) FROM Invoice i GROUP BY i.quantity.currency")
	List<Object[]> minQuantityByCurrencyRaw();

	default Map<String, Money> minQuantityByCurrency() {
		List<Object[]> raw = this.minQuantityByCurrencyRaw();
		return this.convertObjectListToCurrencyMap(raw);
	}

	@Query("SELECT i.quantity.currency, max(i.quantity.amount) FROM Invoice i GROUP BY i.quantity.currency")
	List<Object[]> maxQuantityByCurrencyRaw();

	default Map<String, Money> maxQuantityByCurrency() {
		List<Object[]> raw = this.maxQuantityByCurrencyRaw();
		return this.convertObjectListToCurrencyMap(raw);
	}

	//Util

	default Map<String, Money> convertObjectListToCurrencyMap(final List<Object[]> rawValues) {
		return rawValues.stream().collect(Collectors.toMap(oa -> (String) oa[0], this::moneyOf));
	}

	default Money moneyOf(final Object[] currencyAndAmount) {
		Money ret = new Money();
		ret.setCurrency((String) currencyAndAmount[0]);
		ret.setAmount((Double) currencyAndAmount[1]);
		return ret;
	}

}
