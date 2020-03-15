
package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Inscription;
import org.springframework.samples.petclinic.service.InscriptionsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inscriptions")
public class InscriptionController {

	@Autowired
	private InscriptionsService inscriptionService;


	@GetMapping()
	public String mostrarAnnouncements(final ModelMap modelMap) {
		String vista = "inscriptions/inscriptionsList";
		Iterable<Inscription> inscriptions = this.inscriptionService.findInscriptionsByOwnerId();
		modelMap.addAttribute("inscriptions", inscriptions);
		return vista;
	}

	@GetMapping("/{inscriptionId}")
	public String mostrarAnnouncement(final ModelMap modelMap, @PathVariable("inscriptionId") final int inscriptionId) {
		String vista = "inscriptions/inscriptionDetails";
		Inscription inscription = this.inscriptionService.findInscriptionById(inscriptionId).get();
		modelMap.addAttribute("inscription", inscription);
		return vista;
	}

}
