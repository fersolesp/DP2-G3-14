package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.AppointmentService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {
	
	@Autowired
	private AppointmentService appointmentService;
	@Autowired
	private OwnerService ownerService;
	
	@GetMapping()
	public String mostrarAppointments(final ModelMap modelMap) {
		String vista = "appointments/appointmentsList";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		Owner owner = this.ownerService.findOwnerByUserName(userName);
		Collection<Appointment> appointments = this.appointmentService.findAppointmentsByOwner(owner);
		modelMap.addAttribute("appointments",appointments);
		return vista;
	}
	
	@GetMapping("/{appointmentId}")
	public String mostrarAppointment(final ModelMap modelMap, @PathVariable("appointmentId") final int appointmentId) {
		String vista = "appointments/appointmentDetails";
		Appointment appointment = this.appointmentService.findAppointmentById(appointmentId).get();
		modelMap.addAttribute("appointment",appointment);
		return vista;
	}
	
	@GetMapping(path = "new")
	public String createAppointment (@PathVariable("hairdresserId") final Integer hairdresserId, final ModelMap modelMap) {
		String vista = "appointments/editAppointment";
		Appointment appointment = new Appointment();
		modelMap.addAttribute("appointment", appointment);
		return vista;
	}
	
	@PostMapping(path = "save")
	public String saveAppointment (@Valid final Appointment appointment, final BindingResult result, final ModelMap modelMap) {
		String vista = "redirect:/appointments";
		if(result.hasErrors()) {
			modelMap.addAttribute("appointment", appointment);
			return "appointments/editAppointment";
		}else {
			this.appointmentService.saveAppointment(appointment);
			modelMap.addAttribute("message", "Appointment successfully saved!");
		}
		return vista;
	}

	
	@GetMapping(path ="delete/{appointmentId}")
	public String deleteAppointment(@PathVariable("appointmentId") final Integer appointmentId, final ModelMap modelMap) {
		String vista = "redirect:/appointments";
		Optional<Appointment> appointment = this.appointmentService.findAppointmentById(appointmentId);
		this.appointmentService.deleteAppointment(appointment.get());
		modelMap.addAttribute("message","Appointment successfully deleted!");
		return vista;
	}

}
