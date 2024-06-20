
package acme.entities.progressLogs;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import org.hibernate.validator.constraints.Length;

import acme.client.data.AbstractEntity;
import acme.entities.contracts.Contract;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(indexes = {
	@Index(columnList = "contract_id, draftMode"), //
	@Index(columnList = "contract_id")
})
public class ProgressLog extends AbstractEntity {

	// Serialisation identifier -----------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "PG-[A-Z]{1,2}-[0-9]{4}")
	private String				recordId;

	@NotNull
	@Positive
	@Digits(integer = 3, fraction = 2)
	@Max(100)
	private double				completeness;

	@NotBlank
	@Length(max = 100)
	private String				comment;

	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	private Date				registrationMoment;

	@NotBlank
	@Length(max = 75)
	private String				responsiblePerson;

	private boolean				draftMode;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Contract			contract;

}
