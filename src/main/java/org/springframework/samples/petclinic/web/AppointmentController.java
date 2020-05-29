
package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.StreamSupport;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Hairdresser;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.AppointmentService;
import org.springframework.samples.petclinic.service.HairdresserService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AppointmentController {

	private AppointmentService	appointmentService;
	private HairdresserService	hairdresserService;
	private OwnerService		ownerService;
	private PetService			petService;

	private static final String MESSAGE = "message";
	private static final String EXCEPTION = "exception";
	private static final String APPOINTMENT = "appointment";


	@Autowired
	public AppointmentController(final AppointmentService appointmentService, final HairdresserService hairdresserService, final OwnerService ownerService, final PetService petService) {
		this.appointmentService = appointmentService;
		this.hairdresserService = hairdresserService;
		this.ownerService = ownerService;
		this.petService = petService;
	}

	//AUXILIARES
	@ModelAttribute("allPets")
	public Iterable<Pet> populateAllPets() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		return this.petService.findPets(userName);
	}

	@GetMapping("/appointments")
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

	@GetMapping("/appointments/{appointmentId}")
	public String mostrarAppointment(final ModelMap modelMap, @PathVariable("appointmentId") final int appointmentId) {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Appointment appointment = null;

		try {
			appointment = this.appointmentService.findAppointmentById(appointmentId).get();
			if (!authentication.getName().equals(appointment.getOwner().getUser().getUsername())) {
				modelMap.addAttribute(AppointmentController.MESSAGE, "You cannot access another user's appointment");
				return AppointmentController.EXCEPTION;
			} else {
				String vista = "appointments/appointmentDetails";
				modelMap.addAttribute(AppointmentController.APPOINTMENT, appointment);
				return vista;
			}
		} catch (NoSuchElementException e) {
			modelMap.addAttribute(AppointmentController.MESSAGE, "Appointment not found");
			return AppointmentController.EXCEPTION;
		}
	}

	@GetMapping(path = "/hairdressers/{hairdresserId}/appointments/new")
	public String createAppointment(@PathVariable("hairdresserId") final Integer hairdresserId, final ModelMap modelMap) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Hairdresser hairdresser = null;
		Iterable<Pet> allPets = null;
		Collection<Appointment> appointmentsByOwner = null;

		try {
			allPets = this.petService.findPets(authentication.getName());
		} catch (NoSuchElementException e) {
		}

		try {
			hairdresser = this.hairdresserService.findHairdresserById(hairdresserId).get();
		} catch (NoSuchElementException e) {
			modelMap.addAttribute(AppointmentController.MESSAGE, "There are errors validating data");
			return AppointmentController.EXCEPTION;
		}

		try {
			appointmentsByOwner = this.appointmentService.findAppointmentsByOwner(this.ownerService.findOwnerByUserName(authentication.getName()));
		} catch (NoSuchElementException e) {
		}

		// ------------Validación de reglas de negocio--------------//

		// Owner no puede crear un appointment si no tiene mascotas
		int numeroPets = 0;
		if (allPets != null) {
			numeroPets = (int) StreamSupport.stream(allPets.spliterator(), false).count();
		}
		if (numeroPets == 0) {
			modelMap.addAttribute(AppointmentController.MESSAGE, "You have no pets to make an appointment");
			return AppointmentController.EXCEPTION;
		}

		// Owner no puede crear un appointment si no ha pagado las anteriores
		if (appointmentsByOwner != null && StreamSupport.stream(appointmentsByOwner.spliterator(), false).count() != 0) {
			for (Appointment a : appointmentsByOwner) {
				if (a.getIsPaid() != true) {
					modelMap.addAttribute(AppointmentController.MESSAGE, "You have to pay previous appointments");
					return AppointmentController.EXCEPTION;
				}
			}
		}

		// Owner no puede crear un appointment a un hairdresser que no está activo
		if (!hairdresser.getActive()) {
			modelMap.addAttribute(AppointmentController.MESSAGE, "You cannot create an appointment for a inactive hairdresser");
			return AppointmentController.EXCEPTION;
		}

		String vista = "appointments/editAppointment";
		Appointment appointment = new Appointment();
		appointment.setHairdresser(hairdresser);
		modelMap.addAttribute(AppointmentController.APPOINTMENT, appointment);
		modelMap.addAttribute("hairdresser", hairdresser);
		return vista;
	}

	@PostMapping(path = "/hairdressers/{hairdresserId}/appointments/new")
	public String saveAppointment(@PathVariable("hairdresserId") final Integer hairdresserId, @Valid final Appointment appointment, final BindingResult result, final ModelMap modelMap) {
		String vista = "redirect:/appointments";

		if (result.hasErrors()) {
			modelMap.addAttribute("hairdresser", this.hairdresserService.findHairdresserById(hairdresserId).get());
			modelMap.put(AppointmentController.APPOINTMENT, appointment);
			return "appointments/editAppointment";
		} else {
			appointment.setHairdresser(this.hairdresserService.findHairdresserById(hairdresserId).get());
			appointment.setOwner(appointment.getPet().getOwner());

			// ------------ Validación reglas de negocio ------------ //
			// Owner no puede coger una cita a la misma hora en la que el peluquero ya tenga otra cita
			Hairdresser hairdresser = this.hairdresserService.findHairdresserById(hairdresserId).get();
			Collection<Appointment> appointmentsByHairdresser = this.appointmentService.findAppointmentsByHairdresser(hairdresser);
			for (Appointment a : appointmentsByHairdresser) {
				LocalDateTime fechaExistente = a.getDate();
				if (appointment.getDate().isEqual(fechaExistente)) {
					// !appointment.getDate().isAfter(fechaExistente.plusMinutes(30)) ||
					//   !appointment.getDate().isBefore(fechaExistente.minusMinutes(30))
					modelMap.addAttribute(AppointmentController.MESSAGE, "Hairdresser already has an appointment at that time");
					return AppointmentController.EXCEPTION;
				}
			}
			//Owner no puede coger cita mas de una vez por mascota al día
			Integer petId = Integer.parseInt(result.getFieldValue("pet.id").toString());
			Pet pet = this.petService.findPetById(petId);
			Collection<Appointment> appointmentsByPet = this.appointmentService.findAppointmentsByPet(pet);
			for (Appointment a : appointmentsByPet) {
				LocalDate fechaExistente = LocalDate.from(a.getDate());
				if (fechaExistente.isEqual(LocalDate.from(appointment.getDate()))) {
					modelMap.addAttribute(AppointmentController.MESSAGE, "You cannot make more than one appointment per day for the same pet");
					return AppointmentController.EXCEPTION;
				}
			}

			try {
				this.appointmentService.saveAppointment(appointment);
			} catch (Exception e) {
				modelMap.addAttribute(AppointmentController.MESSAGE, "Error: " + e.getMessage());
				return AppointmentController.EXCEPTION;
			}
			modelMap.addAttribute(AppointmentController.MESSAGE, "Appointment successfully saved!");
		}

		return vista;
	}

	@GetMapping(path = "/appointments/delete/{appointmentId}")
	public String deleteAppointment(@PathVariable("appointmentId") final Integer appointmentId, final ModelMap modelMap) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String vista = "redirect:/appointments";
		Optional<Appointment> appointment = null;
		try {
			appointment = this.appointmentService.findAppointmentById(appointmentId);
			if (!authentication.getName().equals(this.appointmentService.findAppointmentById(appointmentId).get().getOwner().getUser().getUsername())) {
				modelMap.addAttribute(AppointmentController.MESSAGE, "You cannot delete another user's appointment");
				return AppointmentController.EXCEPTION;
			} else {
				this.appointmentService.deleteAppointment(appointment.get());
				modelMap.addAttribute(AppointmentController.MESSAGE, "Appointment successfully deleted");
			}
		} catch (Exception e) {
			modelMap.addAttribute(AppointmentController.MESSAGE, "Error: " + e.getMessage());
			return AppointmentController.EXCEPTION;
		}
		return vista;
	}

}
