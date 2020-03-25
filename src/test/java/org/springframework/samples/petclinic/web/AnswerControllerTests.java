
package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Announcement;
import org.springframework.samples.petclinic.model.Answer;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AnnouncementService;
import org.springframework.samples.petclinic.service.AnswerService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = AnswerController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class AnswerControllerTests {

	private static final int	TEST_OWNER_ID	= 1;

	@MockBean
	private AnswerService		answerService;

	@MockBean
	private PetService			petService;

	@MockBean
	private AnnouncementService	announcementService;

	@MockBean
	private OwnerService		ownerService;

	@Autowired
	private MockMvc				mockMvc;

	private Owner				george;


	public Answer createDummyAnswer(final String name) {
		Answer answer = new Answer();
		answer.setName(name);
		return answer;
	}

	public Announcement createDummyAnnouncement(final String name) {
		Announcement announcement = new Announcement();
		announcement.setName(name);
		return announcement;
	}

	@BeforeEach
	void setup() {

		this.george = new Owner();
		this.george.setId(AnswerControllerTests.TEST_OWNER_ID);
		this.george.setFirstName("George");
		this.george.setLastName("Franklin");
		this.george.setAddress("110 W. Liberty St.");
		this.george.setCity("Madison");
		this.george.setTelephone("6085551023");
		User georgeuser = new User();
		georgeuser.setUsername("george");
		this.george.setUser(georgeuser);
	}

	// mostrarAnswers

	@WithMockUser(value = "george")
	@Test
	void shouldShowAnswers() throws Exception {
		Answer answer1 = this.createDummyAnswer("Answer1");
		Answer answer2 = this.createDummyAnswer("Answer2");
		Announcement dummyAnnouncement = this.createDummyAnnouncement("Announcement1");
		dummyAnnouncement.setOwner(this.george);

		Mockito.when(this.announcementService.findAnnouncementById(1)).thenReturn(Optional.of(dummyAnnouncement));
		Mockito.when(this.ownerService.findOwnerByUserName("george")).thenReturn(this.george);
		Mockito.when(this.answerService.findAnswerByAnnouncement(dummyAnnouncement))//
			.thenReturn(Lists.newArrayList(answer1, answer2));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answers", 1))//
			.andExpect(MockMvcResultMatchers.status().isOk())//
			.andExpect(MockMvcResultMatchers.model().attributeExists("answers"))//
			.andExpect(MockMvcResultMatchers.view().name("answers/answersList"));
	}

	@WithMockUser(value = "george")
	@Test
	void shouldNotShowAnswersWhenYouHaveNotAnswers() throws Exception {

		Announcement dummyAnnouncement = this.createDummyAnnouncement("Announcement1");
		dummyAnnouncement.setOwner(this.george);

		Mockito.when(this.announcementService.findAnnouncementById(1))//
			.thenReturn(Optional.of(dummyAnnouncement));
		Mockito.when(this.answerService.findAnswerByAnnouncement(ArgumentMatchers.any()))//
			.thenThrow(NoSuchElementException.class);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answers", 1))//
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model()//
				.attributeExists("isempty"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("answers"))//
			.andExpect(MockMvcResultMatchers.view().name("answers/answersList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldNotShowAnotherUserAnswer() throws Exception {
		Announcement dummyAnnouncement = this.createDummyAnnouncement("Anuncio1");
		dummyAnnouncement.setOwner(this.george);

		Mockito.when(this.announcementService.findAnnouncementById(1))//
			.thenReturn(Optional.of(dummyAnnouncement));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answers", 1))//
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model()//
				.attribute("message", "You cannot access another user's announcement answers"))//
			.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	// Get createInscription

	@WithMockUser(value = "george")
	@Test
	void shouldCreateAnswer() throws Exception {

		Announcement dummyAnnouncement = this.createDummyAnnouncement("dummyAnnouncement");

		Mockito.when(this.announcementService.findAnnouncementById(1)).thenReturn(Optional.of(dummyAnnouncement));
		Mockito.when(this.ownerService.findOwnerByUserName("george")).thenReturn(this.george);
		Mockito.when(this.answerService.findAnswerByAnnouncement(dummyAnnouncement))//
			.thenReturn(new ArrayList<Answer>());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answer/new", 1))//
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model()//
				.attributeExists("answer"))
			.andExpect(MockMvcResultMatchers.view().name("answers/editAnswer"));
	}

	@WithMockUser(value = "george")
	@Test
	void shouldNotCreateAnswerWhenAnnouncementNotFound() throws Exception {

		Mockito.when(this.announcementService.findAnnouncementById(1)).thenThrow(NoSuchElementException.class);
		//Mockito.when(this.ownerService.findOwnerByUserName("george")).thenReturn(this.george);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answer/new", 1))//
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model()//
				.attribute("message", "There are errors validating data"))
			.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "george")
	@Test
	void shouldNotCreateAnswerWhenAnnouncementAnswersNotFound() throws Exception {

		Announcement dummyAnnouncement = this.createDummyAnnouncement("dummyAnnouncement");
		dummyAnnouncement.setOwner(this.george);

		Mockito.when(this.announcementService.findAnnouncementById(1)).thenReturn(Optional.of(dummyAnnouncement));
		Mockito.when(this.ownerService.findOwnerByUserName("george")).thenReturn(this.george);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answer/new", 1))//
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model()//
				.attribute("message", "There are errors validating data"))
			.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	// Post createInscription

	@WithMockUser(value = "spring")
	@Test
	void shouldSaveAnswer() throws Exception {
		Announcement dummyAnnouncement = this.createDummyAnnouncement("dummyAnnouncement");

		Mockito.when(this.announcementService.findAnnouncementById(1)).thenReturn(Optional.of(dummyAnnouncement));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}/answer/new", 1))//
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("answers/editAnswer"));
	}

}
