
package acme.components;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.client.repositories.AbstractRepository;
import acme.entities.banner.Banner;

@Repository
public interface BannerRepository extends AbstractRepository {

	@Query("SELECT b FROM Banner b WHERE b.displayPeriodStart <= :timestamp AND b.displayPeriodEnd > :timestamp")
	List<Banner> findManyBannersWithinTimeStamp(Date timestamp);

}
