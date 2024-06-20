
package acme.features.manager.project;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.assignments.Assignment;
import acme.entities.auditRecords.AuditRecord;
import acme.entities.codeAudits.CodeAudits;
import acme.entities.contracts.Contract;
import acme.entities.invoices.Invoice;
import acme.entities.progressLogs.ProgressLog;
import acme.entities.projects.Project;
import acme.entities.sponsorships.Sponsorship;
import acme.entities.userStories.UserStory;
import acme.roles.Manager;

@Repository
public interface ManagerProjectRepository extends AbstractRepository {

	@Query("select m from Manager m where m.id = :id")
	Manager findManagerById(int id);

	@Query("select p from Project p where p.id = :id")
	Project findOneProjectById(int id);

	@Query("select p from Project p where p.manager.id = :id")
	Collection<Project> findAllProjectsManagerId(int id);

	@Query("select p from Project p where p.code = :code")
	Project findProjectByCode(String code);

	@Query("select a.userStory from Assignment a where a.project.id = :projectId")
	Collection<UserStory> findUserStoriesByProjectId(int projectId);

	@Query("select s from Sponsorship s where s.project.id = :projectId")
	Collection<Sponsorship> findSponsorshipsByProjectId(int projectId);

	@Query("select i from Invoice i where i.sponsorship.id = :id")
	Collection<Invoice> findInvoicesBySponsorshipId(int id);

	@Query("select p from ProgressLog p where p.contract.id = :id")
	Collection<ProgressLog> findProgressLogsByContractId(int id);

	@Query("select c from Contract c where c.project.id = :projectId")
	Collection<Contract> findContractsByProjectId(int projectId);

	@Query("select m from Module m where m.project.id = :projectId")
	Collection<acme.entities.modules.Module> findModulesByProjectId(int projectId);

	@Query("select a from AuditRecord a where a.codeAudits.id = :id")
	Collection<AuditRecord> findAuditRecordsByCodeAuditId(int id);

	@Query("select c from CodeAudits c where c.project.id = :projectId")
	Collection<CodeAudits> findCodeAuditsByProjectId(int projectId);

	@Query("select a from Assignment a where a.project.id = :projectId")
	Collection<Assignment> findAssignmentByProjectId(int projectId);

}
