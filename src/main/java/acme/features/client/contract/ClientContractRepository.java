
package acme.features.client.contract;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.contracts.Contract;
import acme.entities.progressLogs.ProgressLog;
import acme.entities.projects.Project;
import acme.roles.Client;

@Repository
public interface ClientContractRepository extends AbstractRepository {

	@Query("select c from Contract c where c.client.id = :id")
	Collection<Contract> findAllContractsByClientId(int id);

	@Query("select c from Contract c where c.id = :id")
	Contract findOneContractById(int id);

	@Query("select c.project from Contract c where c.id = :id")
	Collection<Project> findOneProjectByContractId(int id);

	@Query("select p from Project p where p.published = :published")
	Collection<Project> findAllPublishedProjects(boolean published);

	@Query("select c.client from Contract c where c.id = :id")
	Client findOneClientByContractId(int id);

	@Query("select cl from Client cl where cl.id = :id")
	Client findOneClientById(int id);

	@Query("select p from Project p where p.id = :id")
	Project findOneProjectById(int id);

	@Query("select c from Contract c where c.code = :code")
	Contract findOneContractByCode(String code);

	@Query("select pl from ProgressLog pl where pl.contract.id = :id")
	Collection<ProgressLog> findAllProgressLogsByContractId(int id);

	@Query("select c from Contract c where c.project.id = :id")
	Collection<Contract> findAllContractstByProjectId(int id);

	@Query("select sum(c.budget.amount) from Contract c where c.project.id = :id and c.draftMode = false")
	Double sumAllBudgetsOfPublishContracts(int id);
}
