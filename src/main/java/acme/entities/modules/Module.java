
package acme.entities.modules;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.entities.projects.Project;
import acme.roles.Developer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Module extends AbstractEntity {

	//Serialisation identifier

	private static final long	serialVersionUID	= 1L;

	//Attributes

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "[A-Z]{1,3}-[0-9]{3}")
	private String				code;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Past
	private Date				creationMoment;

	@NotBlank
	@Length(max = 100)
	private String				details;

	@NotNull
	private ModuleDifficulty	difficultyLevel;

	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date				updateMoment;

	@URL
	@Length(max = 255)
	private String				link;

	@NotNull
	@Min(1)
	@Max(10000)
	private Integer				totalTime;

	private boolean				draftMode;

	//Relation

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Project				project;

	//Properties

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Developer			developer;

}
