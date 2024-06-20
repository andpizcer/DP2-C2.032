
package acme.entities.auditRecords;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.entities.codeAudits.CodeAudits;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class AuditRecord extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "AU-[0-9]{4}-[0-9]{3}")
	private String				code;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Past
	private Date				periodStart;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Past
	private Date				periodEnd;

	@NotNull
	private AuditRecordMark		mark;

	@URL
	@Length(max = 255)
	private String				link;

	@NotNull
	@ManyToOne
	private CodeAudits			codeAudits;
}
