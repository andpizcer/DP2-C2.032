
package acme.features.client.dashboard;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;

@Repository
public interface ClientDashboardRepository extends AbstractRepository {

	@Query("select count(pl) from ProgressLog pl where pl.contract.client.id = :id and pl.completeness < 25 and pl.draftMode = false")
	Integer progressLogsWithCompletenessRateBelow25(int id);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client.id = :id and pl.completeness >= 25 and pl.completeness < 50 and pl.draftMode = false")
	Integer progressLogsWithCompletenessRateBetween25And50(int id);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client.id = :id and pl.completeness >= 50 and pl.completeness < 75 and pl.draftMode = false")
	Integer progressLogsWithCompletenessRateBetween50And75(int id);

	@Query("select count(pl) from ProgressLog pl where pl.contract.client.id = :id and pl.completeness >= 75 and pl.draftMode = false")
	Integer progressLogsWithCompletenessRateAbove75(int id);

	@Query("select avg(c.budget.amount) from Contract c where c.client.id = :id and c.draftMode = false and c.budget.currency = :d")
	Double contractsBudgetAverage(int id, String d);

	@Query("select stddev(c.budget.amount) from Contract c where c.client.id = :id and c.draftMode = false and c.budget.currency = :d")
	Double contractsBudgetDeviation(int id, String d);

	@Query("select min(c.budget.amount) from Contract c where c.client.id = :id and c.draftMode = false and c.budget.currency = :d")
	Double contractsMinBudget(int id, String d);

	@Query("select max(c.budget.amount) from Contract c where c.client.id = :id and c.draftMode = false and c.budget.currency = :d")
	Double contractsMaxBudget(int id, String d);

	@Query("select c.budget.currency from Contract c where c.client.id = :id and c.draftMode = false")
	Set<String> findAllCurrencies(int id);

}
