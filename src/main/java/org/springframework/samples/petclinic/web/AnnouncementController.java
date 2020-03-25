
package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Announcement;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.AnnouncementService;
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
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/announcements")
public class AnnouncementController {

	@Autowired
	private AnnouncementService	announcementService;

	@Autowired
	private PetService			petService;

	@Autowired
	private OwnerService		ownerService;


	@GetMapping()
	public String mostrarAnnouncements(final ModelMap modelMap) {

		String vista = "announcements/announcementsList";
		boolean isempty = false;
		try {
			Iterable<Announcement> announcements = this.announcementService.findAll();
			modelMap.addAttribute("announcements", announcements);
			modelMap.addAttribute("isanonymoususer", SecurityContextHolder.getContext().getAuthentication().getName().equals("anonymousUser"));

		} catch (NoSuchElementException e) {
			isempty = true;
			modelMap.addAttribute("isempty", isempty);
		}

		return vista;
	}

	@GetMapping("/{announcementId}")
	public String mostrarAnnouncement(final ModelMap modelMap, @PathVariable("announcementId") final int announcementId) {

		Announcement announcement = null;
		try {
			announcement = this.announcementService.findAnnouncementById(announcementId).get();
			String vista = "announcements/announcementDetails";
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			modelMap.addAttribute("announcement", announcement);
			modelMap.addAttribute("isanonymoususer", authentication.getName().equals("anonymousUser"));

			modelMap.addAttribute("ismine", announcement.getOwner().getUser().getUsername().equals(authentication.getName()));
			return vista;

		} catch (NoSuchElementException e) {
			modelMap.addAttribute("message", "Announcement not found");
			return "exception";
		}
	}

	@GetMapping(path = "new")
	public String createAnnouncement(final ModelMap modelMap) {
		try {
			String view = "announcements/editAnnouncement";
			Announcement announcement = new Announcement();
			modelMap.addAttribute("announcement", announcement);
			return view;
		} catch (NoSuchElementException e) {
			modelMap.addAttribute("message", "There are errors validating data");
			return "exception";
		}
	}

	@PostMapping(path = "new")
	public String saveAnnouncement(@Valid final Announcement announcement, final BindingResult result, final ModelMap modelMap) {
		String view = "redirect:/announcements";
		if (result.hasErrors()) {
			modelMap.addAttribute("announcement", announcement);
			return "announcements/editAnnouncement";
		} else {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String userName = authentication.getName();
			Owner owner = this.ownerService.findOwnerByUserName(userName);
			announcement.setOwner(owner);
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
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String view = "redirect:/announcements";
		Optional<Announcement> announcement = null;

		try {
			announcement = this.announcementService.findAnnouncementById(announcementId);
			if (announcement.isPresent() && authentication.getName().equals(this.announcementService.findAnnouncementById(announcementId).get().getOwner().getUser().getUsername())) {
				this.announcementService.deleteAnnouncement(announcement.get());
				modelMap.addAttribute("message", "Announcement successfully deleted");
			} else {
				modelMap.addAttribute("message", "You cannot delete another user's announcement details");
				return "exception";
			}
		} catch (NoSuchElementException e) {
			modelMap.addAttribute("message", "Announcement not found");
			return "exception";
		}
		return view;
	}

	@GetMapping(path = "/update/{announcementId}")
	public String iniactualizarAnnouncements(@PathVariable("announcementId") final int announcementId, final ModelMap modelMap) {
		try {
			String vista = "announcements/editAnnouncement";
			Announcement announcement = this.announcementService.findAnnouncementById(announcementId).get();
			Owner owner = announcement.getOwner();
			org.springframework.samples.petclinic.model.User user = owner.getUser();
			String userName = user.getUsername();
			modelMap.addAttribute("announcement", announcement);
			modelMap.addAttribute("user", userName);
			return vista;
		} catch (NoSuchElementException e) {
			modelMap.addAttribute("message", "There are errors validating data");
			return "exception";
		}
	}

	@PostMapping(path = "/update/{announcementId}")
	public String postactualizarAnnouncements(@Valid final Announcement announcement, @PathVariable("announcementId") final int announcementId, final BindingResult results, final ModelMap modelMap) {

		String vista = "announcements/announcementDetails";
		try {
			if (results.hasErrors()) {
				modelMap.addAttribute("announcement", announcement);
				vista = "announcements/editAnnouncement";
			} else {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				String userName = authentication.getName();
				Owner owner = this.ownerService.findOwnerByUserName(userName);
				announcement.setOwner(owner);
				announcement.setId(announcementId);
				this.announcementService.saveAnnouncement(announcement);
				modelMap.addAttribute("message", "Announcement successfully updated");
			}
		} catch (NoSuchElementException e) {
			modelMap.addAttribute("message", "Announcement not found");
			return "exception";
		}
		return vista;
	}

}
