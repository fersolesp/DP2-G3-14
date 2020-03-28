
package org.springframework.samples.petclinic.web;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AppointmentService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = AppointmentController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class AppointmentControllerTests {

	private static final int	TEST_OWNER_ID	= 1;

	@MockBean
	private AppointmentService	appointmentService;

	@MockBean
	private OwnerService		ownerService;

	@Autowired
	private MockMvc				mockMvc;

	private Owner				carlitos;


	public Appointment createDummyAppointment(final String name) {
		Appointment appointment = new Appointment();
		appointment.setName(name);
		return appointment;
	}

	@BeforeEach
	void setup() {

		this.carlitos = new Owner();
		this.carlitos.setId(AppointmentControllerTests.TEST_OWNER_ID);
		this.carlitos.setFirstName("Carlitos");
		this.carlitos.setLastName("Fernández");
		this.carlitos.setAddress("Avenida de la Palmera, Nº56");
		this.carlitos.setCity("Sevilla");
		this.carlitos.setTelephone("955767651");
		User carlitosuser = new User();
		carlitosuser.setUsername("carlitos");
		this.carlitos.setUser(carlitosuser);
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldShowAppointments() throws Exception {
		Appointment appointment1 = this.createDummyAppointment("Cita1");
		Appointment appointment2 = this.createDummyAppointment("Cita2");

		Mockito.when(this.appointmentService.findAppointmentsByOwner(ArgumentMatchers.any())).thenReturn(Lists.newArrayList(appointment1, appointment2));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("appointments"))
			.andExpect(MockMvcResultMatchers.view().name("appointments/appointmentsList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldNotShowAppointmentsWhenYouHaveNotAppointments() throws Exception {

		Mockito.when(this.appointmentService.findAppointmentsByOwner(ArgumentMatchers.any())).thenThrow(NoSuchElementException.class);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("isempty"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("appointments")).andExpect(MockMvcResultMatchers.view().name("appointments/appointmentsList"));
	}

	@WithMockUser(value = "carlitos")
	@Test
	void shouldShowAppointmentDetails() throws Exception {
		Appointment appointment1 = this.createDummyAppointment("Cita1");
		appointment1.setOwner(this.carlitos);

		Mockito.when(this.appointmentService.findAppointmentById(1)).thenReturn(Optional.of(appointment1));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments/{appointmentId}", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("appointment"))
			.andExpect(MockMvcResultMatchers.view().name("appointments/appointmentDetails"));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldNotShowAppointmentWhenNotFound() throws Exception {

		Mockito.when(this.appointmentService.findAppointmentById(1)).thenThrow(NoSuchElementException.class);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments/{appointmentId}", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "Appointment not found"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("appointment")).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldNotShowAnotherUserAppointment() throws Exception {
		Appointment appointment1 = this.createDummyAppointment("Cita1");
		appointment1.setOwner(this.carlitos);

		Mockito.when(this.appointmentService.findAppointmentById(1)).thenReturn(Optional.of(appointment1));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments/{appointmentId}", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "You cannot access another user's appointment"))
			.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "carlitos")
	@Test
	void shouldDeleteAppointment() throws Exception {
		Appointment appointment1 = this.createDummyAppointment("Cita1");
		appointment1.setOwner(this.carlitos);

		Mockito.when(this.appointmentService.findAppointmentById(1)).thenReturn(Optional.of(appointment1));
		Mockito.doNothing().when(this.appointmentService).deleteAppointment(appointment1);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments/delete/{appointmentId}", 1)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/appointments"));
	}

	@WithMockUser(value = "carlitos")
	@Test
	void shouldNotDeleteAppointmentWhenNotFound() throws Exception {
		Appointment appointment1 = this.createDummyAppointment("Cita1");
		appointment1.setOwner(this.carlitos);

		Mockito.when(this.appointmentService.findAppointmentById(200)).thenThrow(NoSuchElementException.class);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments/delete/{appointmentId}", 200)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "Appointment not found"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("appointment")).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldNotDeleteAppointmentOfOtherUser() throws Exception {
		Appointment appointment1 = this.createDummyAppointment("Cita1");
		appointment1.setOwner(this.carlitos);

		Mockito.when(this.appointmentService.findAppointmentById(1)).thenReturn(Optional.of(appointment1));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments/delete/{appointmentId}", 1)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("message", "You cannot delete another user's appointment")).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

}
