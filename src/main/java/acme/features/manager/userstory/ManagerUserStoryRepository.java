
package acme.features.manager.userstory;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.assignments.Assignment;
import acme.entities.projects.Project;
import acme.entities.userStories.UserStory;
import acme.roles.Manager;

@Repository
public interface ManagerUserStoryRepository extends AbstractRepository {

	@Query("select u from UserStory u where u.id = :id")
	UserStory findUserStoryById(int id);

	@Query("select a from Assignment a where a.userStory.id = :userStoryId")
	Collection<Assignment> findAssignmentByUserStoryId(int userStoryId);

	@Query("select a.userStory from Assignment a where a.project.id = :projectId")
	Collection<UserStory> findUserStoriesByProjectId(int projectId);

	@Query("select p from Project p where p.id = :projectId")
	Project findProjectById(int projectId);

	@Query("select u from UserStory u where u.manager.id = :managerId")
	Collection<UserStory> findUserStoriesByManagerId(int managerId);

	@Query("select m from Manager m where m.id = :managerId")
	Manager findManagerById(int managerId);
}
