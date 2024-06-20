
package acme.features.developer.trainingSession;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.modules.Module;
import acme.entities.sessions.Session;
import acme.roles.Developer;

@Repository
public interface DeveloperSessionRepository extends AbstractRepository {

	@Query("select d from Developer d where d.id = :id")
	Developer findDeveloperById(int id);

	@Query("select m from Module m where m.id = :id")
	Module findOneModuleById(int id);

	@Query("select s from Session s where s.id = :id")
	Session findOneSessionById(int id);

	@Query("select s from Session s where s.code = :code")
	Session findOneSessionByCode(String code);

	@Query("select s from Session s where s.developer.id = :id")
	Collection<Session> findAllSessionsByDeveloperId(int id);

	@Query("select s from Session s where s.trainingModule.id = :id")
	Collection<Session> findAllSessionsByModuleId(int id);

	@Query("select s.trainingModule from Session s where s.id = :id")
	Module findOneModuleBySessionId(int id);

}
