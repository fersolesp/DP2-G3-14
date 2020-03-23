
package org.springframework.samples.petclinic.web;

import java.util.NoSuchElementException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Announcement;
import org.springframework.samples.petclinic.model.Answer;
import org.springframework.samples.petclinic.service.AnnouncementService;
import org.springframework.samples.petclinic.service.AnswerService;
import org.springframework.samples.petclinic.service.OwnerService;
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
@RequestMapping("/announcements/{announcementId}")
public class AnswerController {

	@Autowired
	private AnswerService		answerService;

	@Autowired
	private AnnouncementService	announcementService;

	@Autowired
	private OwnerService		ownerService;


	@ModelAttribute("announcement")
	public Announcement findAnnouncement(@PathVariable("announcementId") final int announcementId) {
		return this.announcementService.findAnnouncementById(announcementId).get();
	}

	@GetMapping(path = "/answer/new")
	public String createAnswer(final Announcement announcement, final ModelMap modelMap) {
		String view = "answers/editAnswer";
		try {
			Answer answer = new Answer();
			answer.setAnnouncement(announcement);
			modelMap.addAttribute("answer", answer);
		} catch (NoSuchElementException e) {
			modelMap.addAttribute("message", "There are errors validating data");
			return "/exception";
		}

		return view;
	}

	@PostMapping(value = "/answer/new")
	public String processCreationForm(final Announcement announcement, @Valid final Answer answer, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("answer", answer);
			return "answers/editAnswer";
		} else {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			answer.setAnnouncement(announcement);
			answer.setOwner(this.ownerService.findOwnerByUserName(auth.getName()));
			try {
				this.answerService.saveAnswer(answer);
				model.addAttribute("message", "Answer successfully saved");
			} catch (Exception e) {
				model.addAttribute("message", e.getMessage());
				return "exception";
			}
			return "redirect:/announcements/{announcementId}";
		}
	}

	@GetMapping("/answers")
	public String mostrarAnwers(final ModelMap modelMap, @PathVariable("announcementId") final Integer announcementId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String vista = "answers/answersList";
		boolean isempty = false;
		try {
			if (!authentication.getName().equals(this.announcementService.findAnnouncementById(announcementId).get().getOwner().getUser().getUsername())) {
				modelMap.addAttribute("message", "You cannot access another user's announcement answers");
				return "exception";
			} else {
				Optional<Announcement> announcement = this.announcementService.findAnnouncementById(announcementId);
				Iterable<Answer> answers = this.answerService.findAnswerByAnnouncement(announcement.get());
				modelMap.addAttribute("answers", answers);
			}
		} catch (NoSuchElementException e) {
			isempty = true;
			modelMap.addAttribute("isempty", isempty);
		}
		return vista;

	}
}
