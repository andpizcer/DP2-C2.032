
package acme.features.developer.trainingModule;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.modules.Module;
import acme.entities.projects.Project;
import acme.entities.sessions.Session;
import acme.roles.Developer;

@Repository
public interface DeveloperModuleRepository extends AbstractRepository {

	@Query("select d from Developer d where d.id = :id")
	Developer findDeveloperById(int id);

	@Query("select m from Module m where m.id = :id")
	Module findOneModuleById(int id);

	@Query("select m from Module m where m.code = :code")
	Module findOneModuleByCode(String code);

	@Query("select m from Module m where m.developer.id = :id")
	Collection<Module> findAllModulesByDeveloperId(int id);

	@Query("select p from Project p")
	Collection<Project> getAllProjects();

	@Query("select p from Project p where p.published = true")
	Collection<Project> getAllPublishedProjects();

	@Query("select p from Project p where p.id = :id")
	Project getProjectById(int id);

	@Query("select s from Session s where s.trainingModule.id = :id")
	Collection<Session> findSessionsByModuleId(int id);

}
