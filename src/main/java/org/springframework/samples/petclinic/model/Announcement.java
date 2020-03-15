
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class Announcement extends NamedEntity {

	private String	petName;
	private String	description;
	private boolean	canBeAdopted;

	@ManyToOne
	private PetType	type;

	@ManyToOne
	private Owner	owner;
}
