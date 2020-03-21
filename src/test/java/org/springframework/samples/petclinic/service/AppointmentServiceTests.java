package org.springframework.samples.petclinic.service;


import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class)) //copia el data.sql para utilizarlos en las pruebas :)
public class AppointmentServiceTests {

	@Autowired
	protected AppointmentService appointmentService;
	@Autowired
	protected OwnerService ownerService;	

	
	@Test
	void shouldFindAppointment() {
		Optional<Appointment> appointment = this.appointmentService.findAppointmentById(1);	
		assertThat(appointment).isPresent();
		assertThat(appointment.get().getName()).isEqualTo("Cita1");
	}
	
	@Test
	void shouldNotFindAppointment() {
		Optional<Appointment> appointment = this.appointmentService.findAppointmentById(200);	
		assertThat(appointment).isNotPresent();
	}
	
	@Test
	@Transactional
	void shouldDeleteAppointment() {
		
		Owner owner1 = this.ownerService.findOwnerByUserName("owner1");
		
		Collection<Appointment> appointments = this.appointmentService.findAppointmentsByOwner(owner1);
		Optional<Appointment> appointment = this.appointmentService.findAppointmentById(1);
		int count = appointments.size();
		
		assertThat(appointment.isPresent());
		this.appointmentService.deleteAppointment(appointment.get());
		assertThat(!appointment.isPresent());
		
		appointments = this.appointmentService.findAppointmentsByOwner(owner1);
		assertThat(appointments.size()).isEqualTo(count-1);

	}
	
	@Test
	@Transactional
	void shouldNotDeleteAppointment() {
		
		Owner owner1 = this.ownerService.findOwnerByUserName("owner1");
		
		Collection<Appointment> appointments = this.appointmentService.findAppointmentsByOwner(owner1);
		Optional<Appointment> appointment = this.appointmentService.findAppointmentById(200);
		int count = appointments.size();
		
		assertThat(!appointment.isPresent());
		
		appointments = this.appointmentService.findAppointmentsByOwner(owner1);
		assertThat(appointments.size()).isEqualTo(count);

	}
	
}
