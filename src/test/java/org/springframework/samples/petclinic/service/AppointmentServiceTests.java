
package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class)) //copia el data.sql para utilizarlos en las pruebas :)
public class AppointmentServiceTests {

	@Autowired
	protected AppointmentService	appointmentService;
	@Autowired
	protected OwnerService			ownerService;


	@ParameterizedTest
	@CsvSource({
		"owner1", "owner2", "owner4"
	})
	void shouldFindAppointmentsGivingOwner(final String ownername) {
		Owner owner = this.ownerService.findOwnerByUserName(ownername);
		Collection<Appointment> appointments = this.appointmentService.findAppointmentsByOwner(owner);
		org.assertj.core.api.Assertions.assertThat(appointments).hasSize(1);
	}

	@ParameterizedTest

	@CsvSource({
		"owner8", "owner9", "owner10"
	})
	void shouldNotFindAppointmentsGivingOwnerWithoutThem(final String ownername) {
		Owner owner = this.ownerService.findOwnerByUserName(ownername);
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.appointmentService.findAppointmentsByOwner(owner);
		});
	}

	@ParameterizedTest

	@CsvSource({
		"1,Cita1,Cita para Leo", "2,Cita2,Cita para Basil", "3,Cita3,Cita para Rosy"
	})
	void shouldFindAppointment(final int id, final String name, final String description) {
		Optional<Appointment> appointment = this.appointmentService.findAppointmentById(id);
		org.assertj.core.api.Assertions.assertThat(appointment).isPresent();
		org.assertj.core.api.Assertions.assertThat(appointment.get().getName()).isEqualTo(name);
		org.assertj.core.api.Assertions.assertThat(appointment.get().getDescription()).isEqualTo(description);

	}

	@ParameterizedTest

	@CsvSource({
		"200", "-1", "0"
	})
	void shouldNotFindAppointment(final int id) {
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.appointmentService.findAppointmentById(id);
		});

	}

	@ParameterizedTest

	@CsvSource({
		"3,owner3", "4,owner3"
	})
	@Transactional
	void shouldDeleteAppointment(final int id, final String ownername) {

		Owner owner = this.ownerService.findOwnerByUserName(ownername);

		Collection<Appointment> appointments = this.appointmentService.findAppointmentsByOwner(owner);
		Optional<Appointment> appointment = this.appointmentService.findAppointmentById(id);
		int count = appointments.size();

		org.assertj.core.api.Assertions.assertThat(appointment.isPresent());
		this.appointmentService.deleteAppointment(appointment.get());
		org.assertj.core.api.Assertions.assertThat(!appointment.isPresent());
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.appointmentService.findAppointmentById(id);
		});

		appointments = this.appointmentService.findAppointmentsByOwner(owner);
		org.assertj.core.api.Assertions.assertThat(appointments.size()).isEqualTo(count - 1);

	}

	@Test
	@Transactional
	void shouldNotDeleteAppointment() {

		Assertions.assertThrows(DataAccessException.class, () -> {
			this.appointmentService.deleteAppointment(null);
		});

	}

}
