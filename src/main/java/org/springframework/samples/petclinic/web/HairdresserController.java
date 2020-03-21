package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Hairdresser;
import org.springframework.samples.petclinic.service.HairdresserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hairdressers")
public class HairdresserController {
	
	@Autowired
	private HairdresserService hairdresserService;
	
	@GetMapping()
	public String mostrarHairdressers(final ModelMap modelMap) {
		String vista = "hairdressers/hairdressersList";
		Iterable<Hairdresser> hairdressers = this.hairdresserService.findAll();
		modelMap.addAttribute("hairdressers",hairdressers);
		return vista;
	}
	
	@GetMapping("/{hairdresserId}")
	public String mostrarHairdresser(final ModelMap modelMap, @PathVariable("hairdresserId") final int hairdresserId) {
		String vista = "hairdressers/hairdresserDetails";
		Hairdresser hairdresser = this.hairdresserService.findHairdresserById(hairdresserId).get();
		modelMap.addAttribute("hairdresser",hairdresser);
		return vista;
	}

}
