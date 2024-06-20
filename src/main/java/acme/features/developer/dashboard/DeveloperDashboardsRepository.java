
package acme.features.developer.dashboard;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.roles.Developer;

@Repository
public interface DeveloperDashboardsRepository extends AbstractRepository {

	@Query("select d from Developer d where d.userAccount.id = :id")
	Developer findDeveloperByUserId(int id);

	@Query("select count(m) from Module m where m.developer.userAccount.id = :id and m.updateMoment is not null and m.draftMode = false")
	Integer getTrainingModulesWithUpdateMoment(int id);

	@Query("select count(s) from Session s where s.trainingModule.developer.userAccount.id = :id and s.link is not null and s.draftMode = false")
	Integer getTrainingSessionsWithLink(int id);

	@Query("select avg(m.totalTime) from Module m where m.developer.userAccount.id = :id and m.draftMode = false")
	Double getAverageTimeOfTrainingModules(int id);

	@Query("select stddev(m.totalTime) from Module m where m.developer.userAccount.id = :id and m.draftMode = false")
	Double getDeviationTimeOfTrainingModules(int id);

	@Query("select min(m.totalTime) from Module m where m.developer.userAccount.id = :id and m.draftMode = false")
	Double getMinimumTimeOfTrainingModules(int id);

	@Query("select max(m.totalTime) from Module m where m.developer.userAccount.id = :id and m.draftMode = false")
	Double getMaximumTimeOfTrainingModules(int id);

}
