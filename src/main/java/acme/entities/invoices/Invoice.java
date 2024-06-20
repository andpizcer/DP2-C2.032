
package acme.entities.invoices;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.client.data.datatypes.Money;
import acme.entities.sponsorships.Sponsorship;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Invoice extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	@Column(unique = true)
	@Pattern(regexp = "IN-[0-9]{4}-[0-9]{4}")
	@NotBlank
	private String				code;

	@Temporal(TemporalType.TIMESTAMP)
	@Past
	@NotNull
	private Date				registrationTime;

	@Temporal(TemporalType.DATE)
	@NotNull
	private Date				dueDate;

	@NotNull
	private Money				quantity;

	@NotNull
	private boolean				draftMode;

	@Range(min = 0, max = 100)
	@NotNull
	private Double				tax;

	@URL
	private String				link;

	@ManyToOne(optional = false)
	@NotNull
	@Valid
	private Sponsorship			sponsorship;


	@Transient
	public Money totalAmount() {
		Money ret = new Money();
		ret.setAmount(this.quantity.getAmount() * (1 + this.tax / 100));
		ret.setCurrency(this.quantity.getCurrency());
		return ret;
	}

}
