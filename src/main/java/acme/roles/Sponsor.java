
package acme.roles;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractRole;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Sponsor extends AbstractRole {

	private static final long	serialVersionUID	= 1L;

	@NotBlank
	@Length(max = 75)
	String						name;

	@NotBlank
	@Length(max = 101)
	String						benefits;

	@URL
	String						link;

	@Email
	String						email;

}
