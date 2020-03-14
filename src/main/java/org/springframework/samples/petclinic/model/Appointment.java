package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "appointments")
public class Appointment extends NamedEntity{

	@NotEmpty
	private String description;
	
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm")
	private LocalDateTime date;
	
	@ManyToOne
	private Hairdresser hairdresser;
	
	@ManyToOne
	private Pet pet;
	
	@OneToOne(optional = true)
	private Payment payment;
	
}
