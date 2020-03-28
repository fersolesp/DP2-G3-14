
package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.NoSuchElementException;
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
	private AppointmentService	appointmentService;
	@Autowired
	private OwnerService		ownerService;


	@GetMapping()
	public String mostrarAppointments(final ModelMap modelMap) {
		String vista = "appointments/appointmentsList";
		boolean isempty = false;
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		Owner owner = this.ownerService.findOwnerByUserName(userName);
		try {
			Collection<Appointment> appointments = this.appointmentService.findAppointmentsByOwner(owner);
			modelMap.addAttribute("appointments", appointments);

		} catch (NoSuchElementException e) {
			isempty = true;
			modelMap.addAttribute("isempty", isempty);
		}
		return vista;
	}

	@GetMapping("/{appointmentId}")
	public String mostrarAppointment(final ModelMap modelMap, @PathVariable("appointmentId") final int appointmentId) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Appointment appointment = null;

		try {
			appointment = this.appointmentService.findAppointmentById(appointmentId).get();
			if (!authentication.getName().equals(appointment.getOwner().getUser().getUsername())) {
				modelMap.addAttribute("message", "You cannot access another user's appointment");
				return "exception";
			} else {
				String vista = "appointments/appointmentDetails";
				modelMap.addAttribute("appointment", appointment);
				return vista;
			}
		} catch (NoSuchElementException e) {
			modelMap.addAttribute("message", "Appointment not found");
			return "exception";
		}
	}

	@GetMapping(path = "new")
	public String createAppointment(@PathVariable("hairdresserId") final Integer hairdresserId, final ModelMap modelMap) {
		String vista = "appointments/editAppointment";
		Appointment appointment = new Appointment();
		modelMap.addAttribute("appointment", appointment);
		return vista;
	}

	@PostMapping(path = "save")
	public String saveAppointment(@Valid final Appointment appointment, final BindingResult result, final ModelMap modelMap) {
		String vista = "redirect:/appointments";
		if (result.hasErrors()) {
			modelMap.addAttribute("appointment", appointment);
			return "appointments/editAppointment";
		} else {
			this.appointmentService.saveAppointment(appointment);
			modelMap.addAttribute("message", "Appointment successfully saved!");
		}
		return vista;
	}

	@GetMapping(path = "delete/{appointmentId}")
	public String deleteAppointment(@PathVariable("appointmentId") final Integer appointmentId, final ModelMap modelMap) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String vista = "redirect:/appointments";
		Optional<Appointment> appointment = null;
		try {
			appointment = this.appointmentService.findAppointmentById(appointmentId);
			if (!authentication.getName().equals(this.appointmentService.findAppointmentById(appointmentId).get().getOwner().getUser().getUsername())) {
				modelMap.addAttribute("message", "You cannot delete another user's appointment");
				return "exception";
			} else {
				this.appointmentService.deleteAppointment(appointment.get());
				modelMap.addAttribute("message", "Appointment successfully deleted");
			}
		} catch (NoSuchElementException e) {
			modelMap.addAttribute("message", "Appointment not found");
			return "exception";
		}
		return vista;
	}

}
