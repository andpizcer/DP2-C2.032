
package acme.entities.banner;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Banner extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@NotNull
	private BannerType			type;

	@Temporal(TemporalType.TIMESTAMP)
	@Past
	@NotNull
	private Date				instantiationUpdateMoment;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date				displayPeriodStart;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	private Date				displayPeriodEnd;

	@NotBlank
	@URL
	@Length(max = 255)
	private String				picture;

	@NotBlank
	@Length(max = 75)
	private String				slogan;

	@NotBlank
	@URL
	@Length(max = 255)
	private String				link;
}
