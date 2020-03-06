
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Announcement extends NamedEntity {

	private String	petName;
	private String	description;
	private boolean	canBeAdopted;

	@ManyToOne
	@JoinColumn(name = "type_id")
	private PetType	type;

	@ManyToOne
	@JoinColumn(name = "owner_id")
	private Owner	owner;
}
