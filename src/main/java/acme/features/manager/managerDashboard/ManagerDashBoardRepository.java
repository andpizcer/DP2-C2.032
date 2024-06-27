
package acme.features.manager.managerDashboard;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.userStories.Priority;

@Repository
public interface ManagerDashBoardRepository extends AbstractRepository {

	@Query("select distinct p.cost.currency from Project p where p.manager.id = :managerId")
	Set<String> currenciesOfProjectsOfManager(int managerId);

	@Query("select count(u) from UserStory u where u.priority = :priority and u.manager.id = :managerId")
	Integer countUserStoriesOfPriority(Priority priority, int managerId);

	@Query("select avg(u.estimatedCost) from UserStory u where u.manager.id = :managerId")
	Double averageEstimatedCostUserStories(int managerId);

	@Query("select stddev(u.estimatedCost) from UserStory u where u.manager.id = :managerId")
	Double deviationEstimatedCostUserStories(int managerId);

	@Query("select min(u.estimatedCost) from UserStory u where u.manager.id = :managerId")
	Integer minEstimatedCostUserStories(int managerId);

	@Query("select max(u.estimatedCost) from UserStory u where u.manager.id = :managerId")
	Integer maxEstimatedCostUserStories(int managerId);

	@Query("select avg(p.cost.amount) from Project p where p.manager.id = :managerId and p.cost.currency= :currency")
	Double averageCostProjects(int managerId, String currency);

	@Query("select stddev(p.cost.amount) from Project p where p.manager.id = :managerId and p.cost.currency= :currency")
	Double deviationCostProjects(int managerId, String currency);

	@Query("select min(p.cost.amount) from Project p where p.manager.id = :managerId and p.cost.currency= :currency")
	Double minCostProjects(int managerId, String currency);

	@Query("select max(p.cost.amount) from Project p where p.manager.id = :managerId and p.cost.currency= :currency")
	Double maxCostProjects(int managerId, String currency);
}
