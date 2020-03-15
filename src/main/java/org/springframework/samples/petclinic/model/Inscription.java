
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
public class Inscription extends NamedEntity {

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate	date;

	private Boolean		isPaid;

	@ManyToOne
	private Payment		payment;

	@ManyToOne
	private Pet			pet;

	@ManyToOne
	private Owner		owner;

	@OneToOne
	private Course		course;
}
