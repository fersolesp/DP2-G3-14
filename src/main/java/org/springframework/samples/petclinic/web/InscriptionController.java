
package org.springframework.samples.petclinic.web;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Course;
import org.springframework.samples.petclinic.model.Inscription;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.CourseService;
import org.springframework.samples.petclinic.service.InscriptionsService;
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
public class InscriptionController {

	@Autowired
	private InscriptionsService	inscriptionService;

	@Autowired
	private PetService			petService;

	@Autowired
	private CourseService		courseService;

	@Autowired
	private OwnerService		ownerService;


	@GetMapping("/inscriptions")
	public String mostrarInscriptions(final ModelMap modelMap) {
		String vista = "inscriptions/inscriptionsList";
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		Owner owner = this.ownerService.findOwnerByUserName(userName);
		Iterable<Inscription> inscriptions = this.inscriptionService.findInscriptionsByOwner(owner);
		modelMap.addAttribute("inscriptions", inscriptions);
		return vista;
	}

	@GetMapping("/inscriptions/{inscriptionId}")
	public String mostrarInscription(final ModelMap modelMap, @PathVariable("inscriptionId") final int inscriptionId) {
		String vista = "inscriptions/inscriptionDetails";
		Inscription inscription = this.inscriptionService.findInscriptionById(inscriptionId).get();
		modelMap.addAttribute("inscription", inscription);
		return vista;
	}

	@GetMapping(path = "/courses/{courseId}/inscription/new")
	public String createInscription(final ModelMap modelMap, @PathVariable("courseId") final int courseId) {
		String view = "inscriptions/editInscription";
		Inscription inscription = new Inscription();
		modelMap.addAttribute("inscription", inscription);
		return view;
	}

	@PostMapping(path = "/courses/{courseId}/inscription/new")
	public String saveInscription(@Valid final Inscription inscription, @PathVariable("courseId") final int courseId, final BindingResult result, final ModelMap modelMap) {

		String view = "redirect:/inscriptions";
		if (result.hasErrors()) {
			modelMap.addAttribute("inscription", inscription);
			return "inscriptions/editInscription";
		} else {
			Course course = this.courseService.findCourseById(courseId).get();
			inscription.setOwner(inscription.getPet().getOwner());
			inscription.setCourse(course);
			inscription.setIsPaid(false);
			this.inscriptionService.saveInscription(inscription);
			modelMap.addAttribute("message", "Inscription successfully saved!");
		}
		return view;
	}

	@ModelAttribute("pets")
	public Iterable<Pet> populatePet() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		return this.petService.findPets(userName);
	}

	@GetMapping(path = "/inscriptions/delete/{inscriptionId}")
	public String deleteInscription(@PathVariable("inscriptionId") final Integer inscriptionId, final ModelMap modelMap) {
		String view = "redirect:/inscriptions";
		Optional<Inscription> inscription = this.inscriptionService.findInscriptionById(inscriptionId);
		if (inscription.isPresent()) {
			this.inscriptionService.deleteInscription(inscription.get());
			modelMap.addAttribute("message", "Inscription successfully deleted");
		} else {
			modelMap.addAttribute("message", "Inscription not found");
		}
		return view;
	}

}
