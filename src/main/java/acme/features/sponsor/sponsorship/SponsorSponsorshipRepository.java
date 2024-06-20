
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.invoices.Invoice;
import acme.entities.projects.Project;
import acme.entities.sponsorships.Sponsorship;
import acme.roles.Sponsor;

@Repository
public interface SponsorSponsorshipRepository extends AbstractRepository {

	@Query("SELECT s FROM Sponsorship s WHERE s.sponsor.id = :sponsorId")
	Collection<Sponsorship> findManySponsorshipsBySponsorId(int sponsorId);

	@Query("SELECT s FROM Sponsorship s WHERE s.id = :id")
	Sponsorship findOneSponsorshipById(int id);

	@Query("SELECT p FROM Project p WHERE p.published = true")
	Collection<Project> findAllPublishedProjects();

	@Query("SELECT p FROM Project p WHERE p.id = :id")
	Project findOneProjectById(int id);

	@Query("SELECT s FROM Sponsor s WHERE s.id = :id")
	Sponsor findOneSponsorById(int id);

	@Query("SELECT i FROM Invoice i WHERE i.sponsorship.id = :sponsorshipId")
	Collection<Invoice> findManyInvoicesBySponsorshipId(int sponsorshipId);

	@Query("SELECT s FROM Sponsorship s WHERE s.code = :code")
	Sponsorship findOneSponsorshipByCode(String code);

}
