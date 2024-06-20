
package acme.entities.sponsorships;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.client.data.datatypes.Money;
import acme.entities.projects.Project;
import acme.roles.Sponsor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Sponsorship extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Column(unique = true)
	@Pattern(regexp = "[A-Z]{1,3}-[0-9]{3}")
	@NotBlank
	private String				code;

	@Temporal(TemporalType.TIMESTAMP)
	@Past
	@NotNull
	private Date				moment;

	@Temporal(TemporalType.DATE)
	@NotNull
	private Date				startDate;

	@Temporal(TemporalType.DATE)
	@NotNull
	private Date				endDate;

	@NotNull
	private Money				amount;

	@NotNull
	private SponsorshipType		type;

	@NotNull
	private boolean				draftMode;

	@Email
	private String				email;

	@URL
	private String				link;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Sponsor				sponsor;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Project				project;

}
