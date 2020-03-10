
package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Announcement;
import org.springframework.samples.petclinic.model.Answer;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.AnnouncementService;
import org.springframework.samples.petclinic.service.AnswerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/announcements")
public class AnnouncementController {

	@Autowired
	private AnnouncementService	announcementService;

	@Autowired
	private AnswerService		answerService;

	@Autowired
	private PetService			petService;


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
		Announcement announcement = this.announcementService.findAnnouncementById(announcementId).get();
		modelMap.addAttribute("announcement", announcement);
		return vista;
	}

	//	@GetMapping(path = "/update/{announcementId}")
	//	public String actualizarAnnouncements(@PathParam("announcementId") final int announcementId, final ModelMap modelMap) {
	//		String vista = "announcements/announcementsList";
	//		modelMap.
	//		return vista;
	//	}

	@GetMapping(path = "new")
	public String createAnnouncement(final ModelMap modelMap) {
		String view = "announcements/editAnnouncement";
		Announcement announcement = new Announcement();
		modelMap.addAttribute("announcement", announcement);
		return view;
	}

	@PostMapping(path = "save")
	public String saveAnnouncement(@Valid final Announcement announcement, final BindingResult result, final ModelMap modelMap) {
		String view = "redirect:/announcements";
		if (result.hasErrors()) {
			modelMap.addAttribute("announcement", announcement);
			return "announcements/editAnnouncement";
		} else {
			this.announcementService.saveAnnouncement(announcement);
			modelMap.addAttribute("message", "Announcement successfully saved!");
		}
		return view;
	}

	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.petService.findPetTypes();
	}

	@GetMapping(path = "delete/{announcementId}")
	public String deleteAnnouncement(@PathVariable("announcementId") final Integer announcementId, final ModelMap modelMap) {
		String view = "redirect:/announcements";
		Optional<Announcement> announcement = this.announcementService.findAnnouncementById(announcementId);
		if (announcement.isPresent()) {
			this.announcementService.deleteAnnouncement(announcement.get());
			modelMap.addAttribute("message", "Announcement successfully deleted");
		} else {
			modelMap.addAttribute("message", "Announcement not found");
		}
		return view;
	}

	@GetMapping("/{announcementId}/answers")
	public String mostrarAnwers(final ModelMap modelMap, @PathVariable("announcementId") final Integer announcementId) {
		String vista = "answers/answersList";
		Optional<Announcement> announcement = this.announcementService.findAnnouncementById(announcementId);
		Iterable<Answer> answers = this.answerService.findAnswerByAnnouncement(announcement.get());
		modelMap.addAttribute("answers", answers);
		return vista;
	}

}
