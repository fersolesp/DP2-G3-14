
package org.springframework.samples.petclinic.model;

import java.util.Date;

import javax.persistence.Entity;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Data
public class Payment extends NamedEntity {

	private Double	amount;

	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private Date	date;
}
