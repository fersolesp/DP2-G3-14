
package org.springframework.samples.petclinic.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Announcement;
import org.springframework.samples.petclinic.service.AnnouncementService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/announcements")
public class AnnouncementController {

	@Autowired
	private AnnouncementService announcementService;


	@GetMapping()
	public String mostrarAnnouncements(final ModelMap modelMap) {
		String vista = "announcements/announcementsList";
		Iterable<Announcement> announcements = this.announcementService.findAll();
		modelMap.addAttribute("announcements", announcements);
		return vista;
	}

	@GetMapping("/{announcementId}")
	public String mostrarAnnouncement(final ModelMap modelMap, @PathVariable("announcementId") final int announcementId) {
		String vista = "announcements/announcementDetails";
		Announcement announcement = this.announcementService.findAnnouncementById(announcementId);
		modelMap.addAttribute("announcement", announcement);
		return vista;
	}

	@PostMapping(path = "/save")
	public String salvarAnnouncement(@Valid final Announcement announcement, final BindingResult result, final ModelMap modelMap) {
		String vista = "announcements/announcementsList";
		if (result.hasErrors()) {
			modelMap.addAttribute("announcement", announcement);
			return "announcements/editAnnouncemet";
		} else {
			this.announcementService.save(announcement);
			modelMap.addAttribute("message", "Announcement succesfully saved!");
		}
		return vista;
	}

	//	@GetMapping(path = "/update/{announcementId}")
	//	public String actualizarAnnouncements(@PathParam("announcementId") final int announcementId, final ModelMap modelMap) {
	//		String vista = "announcements/announcementsList";
	//		modelMap.
	//		return vista;
	//	}

}
