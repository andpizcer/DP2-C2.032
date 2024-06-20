/*
 * Risk.java
 * 
 * A risk, positive or negative, is a fact that has a certain impact on the development
 * of a project and must be considered by the administrator. The system must store the
 * following data about them: a reference (pattern “R-[0-9]{3}”), not blank, unique),
 * an identification date (in the past), an impact (positive real number), a probability,
 * a value (result of the multiplication of impact and probability), a description
 * (not blank, shorter than 101 characters), and an optional link with further information.
 */

package acme.entities.risks;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

// No se le añade multiplicidad. Igual que todos los requisitos de grupo. En la entrega tres
// en los aspectos funcionales se especifica.

@Entity
@Getter
@Setter
public class Risk extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	//ATRIBUTES
	@NotNull
	private RiskStatus			status;

	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "R-[0-9]{3}")
	private String				reference;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Past
	private Date				identificationDate;

	@NotNull
	@Range(min = 1, max = 100)
	private Integer				impact;

	@NotNull
	@Range(min = 0, max = 100)
	@Digits(integer = 3, fraction = 2)
	private Double				probability;

	@NotBlank
	@Length(max = 100)
	private String				description;

	//Optional attribute
	@URL
	@Length(max = 255)
	private String				link;


	//Atributo derivado
	public Double value() {
		return this.getProbability() * this.getImpact();
	}
}
