// Tabla intermedia entre Project y UserStory

package acme.entities.assignments;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import acme.client.data.AbstractEntity;
import acme.entities.projects.Project;
import acme.entities.userStories.UserStory;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Assignment extends AbstractEntity {

	private static final long	serialVersionUID	= 1L;

	//PROPERTIES
	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Project				project;

	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private UserStory			userStory;
}
