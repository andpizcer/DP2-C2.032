
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

	@Query("SELECT count(i) FROM Invoice i WHERE i.tax <= 21 AND i.sponsorship.sponsor.id = :sponsorId")
	int lowTaxInvoicesCount(int sponsorId);

	@Query("SELECT count(s) FROM Sponsorship s WHERE s.link != null AND s.sponsor.id = :sponsorId")
	int linkedSponsorshipsCount(int sponsorId);

	//Sponsorship amount metrics

	@Query("SELECT s.amount.currency, avg(s.amount.amount) FROM Sponsorship s WHERE s.sponsor.id = :sponsorId GROUP BY s.amount.currency")
	List<Object[]> averageAmountByCurrencyRaw(int sponsorId);

	default Map<String, Money> averageAmountByCurrency(final int sponsorId) {
		List<Object[]> raw = this.averageAmountByCurrencyRaw(sponsorId);
		return this.convertObjectListToCurrencyMap(raw);
	}

	@Query("SELECT s.amount.currency, sqrt( avg(s.amount.amount*s.amount.amount) - avg(s.amount.amount)*avg(s.amount.amount) ) FROM Sponsorship s WHERE s.sponsor.id = :sponsorId GROUP BY s.amount.currency")
	List<Object[]> amountDeviationByCurrencyRaw(int sponsorId);

	default Map<String, Money> amountDeviationByCurrency(final int sponsorId) {
		List<Object[]> raw = this.amountDeviationByCurrencyRaw(sponsorId);
		return this.convertObjectListToCurrencyMap(raw);
	}

	@Query("SELECT s.amount.currency, min(s.amount.amount) FROM Sponsorship s WHERE s.sponsor.id = :sponsorId GROUP BY s.amount.currency")
	List<Object[]> minAmountByCurrencyRaw(int sponsorId);

	default Map<String, Money> minAmountByCurrency(final int sponsorId) {
		List<Object[]> raw = this.minAmountByCurrencyRaw(sponsorId);
		return this.convertObjectListToCurrencyMap(raw);
	}

	@Query("SELECT s.amount.currency, max(s.amount.amount) FROM Sponsorship s WHERE s.sponsor.id = :sponsorId GROUP BY s.amount.currency")
	List<Object[]> maxAmountByCurrencyRaw(int sponsorId);

	default Map<String, Money> maxAmountByCurrency(final int sponsorId) {
		List<Object[]> raw = this.maxAmountByCurrencyRaw(sponsorId);
		return this.convertObjectListToCurrencyMap(raw);
	} //Invoice quantity metrics

	@Query("SELECT i.quantity.currency, avg(i.quantity.amount) FROM Invoice i WHERE i.sponsorship.sponsor.id = :sponsorId GROUP BY i.quantity.currency")
	List<Object[]> averageQuantityByCurrencyRaw(int sponsorId);

	default Map<String, Money> averageQuantityByCurrency(final int sponsorId) {
		List<Object[]> raw = this.averageQuantityByCurrencyRaw(sponsorId);
		return this.convertObjectListToCurrencyMap(raw);
	}

	@Query("SELECT i.quantity.currency, sqrt( avg(i.quantity.amount*i.quantity.amount) - avg(i.quantity.amount)*avg(i.quantity.amount) ) FROM Invoice i WHERE i.sponsorship.sponsor.id = :sponsorId GROUP BY i.quantity.currency")
	List<Object[]> quantityDeviationByCurrencyRaw(int sponsorId);

	default Map<String, Money> quantityDeviationByCurrency(final int sponsorId) {
		List<Object[]> raw = this.quantityDeviationByCurrencyRaw(sponsorId);
		return this.convertObjectListToCurrencyMap(raw);
	}

	@Query("SELECT i.quantity.currency, min(i.quantity.amount) FROM Invoice i WHERE i.sponsorship.sponsor.id = :sponsorId GROUP BY i.quantity.currency")
	List<Object[]> minQuantityByCurrencyRaw(int sponsorId);

	default Map<String, Money> minQuantityByCurrency(final int sponsorId) {
		List<Object[]> raw = this.minQuantityByCurrencyRaw(sponsorId);
		return this.convertObjectListToCurrencyMap(raw);
	}

	@Query("SELECT i.quantity.currency, max(i.quantity.amount) FROM Invoice i WHERE i.sponsorship.sponsor.id = :sponsorId GROUP BY i.quantity.currency")
	List<Object[]> maxQuantityByCurrencyRaw(int sponsorId);

	default Map<String, Money> maxQuantityByCurrency(final int sponsorId) {
		List<Object[]> raw = this.maxQuantityByCurrencyRaw(sponsorId);
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
