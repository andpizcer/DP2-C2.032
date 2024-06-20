
package acme.components;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import acme.client.helpers.MomentHelper;
import acme.client.helpers.RandomHelper;
import acme.entities.banner.Banner;

@ControllerAdvice
public class BannerAdvisor {

	@Autowired
	BannerRepository repository;


	@ModelAttribute("banner")
	public Banner getBanner() {
		Date now;
		List<Banner> eligibleBanners;
		int count;
		Banner chosenBanner;

		now = MomentHelper.getCurrentMoment();
		eligibleBanners = this.repository.findManyBannersWithinTimeStamp(now);
		count = eligibleBanners.size();
		if (count == 0)
			return null;
		chosenBanner = eligibleBanners.get(RandomHelper.nextInt(count));
		return chosenBanner;
	}
}
