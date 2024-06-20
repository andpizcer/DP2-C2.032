
package acme.features.sponsor.invoice;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.invoices.Invoice;
import acme.entities.sponsorships.Sponsorship;

@Repository
public interface SponsorInvoiceRepository extends AbstractRepository {

	@Query("SELECT s FROM Sponsorship s WHERE s.id = :id")
	Sponsorship findOneSponsorshipById(int id);

	@Query("SELECT i FROM Invoice i WHERE i.sponsorship.id = :sponsorshipId")
	Collection<Invoice> findManyInvoicesBySponsorshipId(int sponsorshipId);

	@Query("SELECT i FROM Invoice i WHERE i.id = :id")
	Invoice findOneInvoiceById(int id);

	@Query("SELECT s FROM Sponsorship s WHERE s.draftMode = true AND s.sponsor.id = :sponsorId")
	Collection<Sponsorship> findManySponsorshipsInDraftModeBySponsorId(int sponsorId);

	@Query("SELECT i FROM Invoice i WHERE i.code = :code")
	Invoice findOneInvoiceByCode(String code);
}
