package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;

import lombok.Data;

@Data
@Entity
public class Payment extends NamedEntity{
	
	private Double amount;
	private LocalDate date;
	private String Concept;

}
