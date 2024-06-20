/*
 * A project aggregates several user stories elicited by the same manager.
 * The system must store the following data about them: a code (pattern “[A-Z]{3}-[0-9]{4}”,
 * not blank, unique), a title (not blank, shorter than 76 characters), an abstract
 * (not blank, shorter than 101 characters), an indication on whether it has fatal errors,
 * e.g., panics, a cost (positive or nought), and an optional link with further information.
 * Projects containing fatal errors must be rejected by the system.
 */

package acme.entities.projects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.client.data.datatypes.Money;
import acme.roles.Manager;
import lombok.Getter;
import lombok.Setter;

// Se añadirán más atributos como remaining cost necesarios para otras entidades

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "published"),
	@Index(columnList = "code")
})
public class Project extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	//ATRIBUTES
	@NotBlank
	@Column(unique = true)
	@Pattern(regexp = "[A-Z]{3}-[0-9]{4}")
	private String				code;

	@NotBlank
	@Length(max = 75)
	private String				title;

	@NotBlank
	@Length(max = 100)
	private String				abstractInfo;

	@NotNull
	private Boolean				indication;

	@NotNull
	@Valid
	private Money				cost;

	private boolean				published;

	//Optional attribute
	@URL
	@Length(max = 255)
	private String				link;

	//PROPERTIES
	@NotNull
	@Valid
	@ManyToOne(optional = false)
	public Manager				manager;
}
