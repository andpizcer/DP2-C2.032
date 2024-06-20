/*
 * A user story is a document that a manager uses to represent the smallest unit of
 * work in a project. The system must store the following data about them: a title (not blank,
 * shorter than 76 characters), a description (not blank, shorter than 101 characters),
 * an estimated cost (in hours, positive, not nought), the acceptance criteria (not blank,
 * shorter than 101 characters), a priority (“Must”, “Should”, “Could”, or “Won’t”), and an
 * optional link with further information.
 */

package acme.entities.userStories;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.URL;

import acme.client.data.AbstractEntity;
import acme.roles.Manager;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
public class UserStory extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	//ATRIBUTES
	@NotBlank
	@Length(max = 75)
	private String				title;

	@NotBlank
	@Length(max = 100)
	private String				description;

	@NotNull
	@Range(min = 1, max = 100000)
	private Integer				estimatedCost;

	@NotBlank
	@Length(max = 100)
	private String				acceptanceCriteria;

	@NotNull
	private Priority			priority;

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
