
package acme.features.manager.assignment;

import java.util.Collection;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.assignments.Assignment;
import acme.entities.projects.Project;
import acme.entities.userStories.UserStory;
import acme.roles.Manager;

@Repository
public interface ManagerAssignmentRepository extends AbstractRepository {

	@Query("select us from UserStory us where us.id = :id")
	UserStory findUserStoryById(int id);

	@Query("select m from Manager m where m.id = :id")
	Manager findManagerById(int id);

	@Query("select p from Project p where p.id = :id")
	Project findProjectById(int id);

	@Query("select a from Assignment a where a.userStory.id = :id")
	Collection<Assignment> findAssignmentByUserStoryId(int id);

	@Query("select p from Project p left join Assignment a on a.id = a.project.id and a.userStory.id = :userStoryId where a.userStory.id is null and p.published = false and p.manager.id = :managerId ")
	Collection<Project> findUnpublishedProjectsOfManagerUnlinkedToUserStory(int managerId, int userStoryId);

	@Query("select a.project from Assignment a where a.userStory.id = :userStoryId and a.project.published = false")
	Collection<Project> findUnpublishedProjectsLinkedToUserStory(int userStoryId);

	@Modifying
	@Query("delete from Assignment a where a.project.id = :projectId and a.userStory.id = :userStoryId")
	void deleteAssignment(int projectId, int userStoryId);

}
