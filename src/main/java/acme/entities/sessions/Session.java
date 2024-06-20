
package acme.entities.sessions;

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
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.entities.modules.Module;
import acme.roles.Developer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Session extends AbstractEntity {

	//Serialisation identifier

	private static final long	serialVersionUID	= 1L;

	//Attributes

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "TS-[A-Z]{1,3}-[0-9]{3}")
	private String				code;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				fechaInicio;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date				fechaFinal;

	@NotBlank
	@Length(max = 75)
	private String				location;

	@NotBlank
	@Length(max = 75)
	private String				instructor;

	@NotBlank
	@Email
	@Length(max = 255)
	private String				email;

	@URL
	@Length(max = 255)
	private String				link;

	private boolean				draftMode;

	//Relations

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Module				trainingModule;

	//Properties

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Developer			developer;

}
