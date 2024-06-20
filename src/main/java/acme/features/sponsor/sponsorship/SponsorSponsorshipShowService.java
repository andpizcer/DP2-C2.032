
package acme.features.sponsor.sponsorship;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.client.data.models.Dataset;
import acme.client.services.AbstractService;
import acme.client.views.SelectChoices;
import acme.entities.projects.Project;
import acme.entities.sponsorships.Sponsorship;
import acme.entities.sponsorships.SponsorshipType;
import acme.roles.Sponsor;

@Service
public class SponsorSponsorshipShowService extends AbstractService<Sponsor, Sponsorship> {

	@Autowired
	private SponsorSponsorshipRepository repository;


	@Override
	public void authorise() {
		int id;
		Sponsorship sponsorship;
		Sponsor owner;
		boolean status;

		id = super.getRequest().getData("id", int.class);
		sponsorship = this.repository.findOneSponsorshipById(id);
		owner = sponsorship.getSponsor();
		//User must own this sponsorship
		status = this.getRequest().getPrincipal().hasRole(owner);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Sponsorship object;
		int id;

		id = super.getRequest().getData("id", int.class);
		object = this.repository.findOneSponsorshipById(id);

		super.getBuffer().addData(object);
	}

	@Override
	public void unbind(final Sponsorship object) {
		assert object != null;

		Collection<Project> availableProjects;
		SelectChoices projectChoices;
		SelectChoices typeChoices;
		Dataset dataset;

		availableProjects = this.repository.findAllPublishedProjects();
		projectChoices = SelectChoices.from(availableProjects, "code", object.getProject());
		typeChoices = SelectChoices.from(SponsorshipType.class, object.getType());

		dataset = super.unbind(object, "code", "moment", "startDate", "endDate", "amount", "type", "draftMode", "email", "link");
		dataset.put("projects", projectChoices);
		dataset.put("project", projectChoices.getSelected().getKey());
		dataset.put("types", typeChoices);
		dataset.put("type", typeChoices.getSelected().getKey());

		super.getResponse().addData(dataset);
	}
}
