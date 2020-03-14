package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "hairdressers")
public class Hairdresser extends Person{
	
	private HairdresserSpecialty specialties;
	
	private boolean active;

}
