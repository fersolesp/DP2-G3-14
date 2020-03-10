
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
public class Answer extends NamedEntity {

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private LocalDate		date;

	private String			description;

	@ManyToOne
	private Announcement	announcement;

	@ManyToOne
	private Owner			owner;
}
