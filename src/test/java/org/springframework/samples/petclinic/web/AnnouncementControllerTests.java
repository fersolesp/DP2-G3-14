
package org.springframework.samples.petclinic.web;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Announcement;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AnnouncementService;
import org.springframework.samples.petclinic.service.AnswerService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = AnnouncementController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class AnnouncementControllerTests {

	private static final int	TEST_OWNER_ID			= 1;
	private static final int	TEST_ANNOUNCEMENT_ID	= 1;

	@MockBean
	private PetService			petService;

	@MockBean
	private AnnouncementService	announcementService;

	@MockBean
	private AnswerService		answerService;

	@MockBean
	private OwnerService		ownerService;

	@Autowired
	private MockMvc				mockMvc;

	private Owner				george;


	public Announcement createDummyAnnouncement(final String name) {
		Announcement announcement = new Announcement();
		announcement.setName(name);
		return announcement;
	}

	@BeforeEach
	void setup() {

		this.george = new Owner();
		this.george.setId(AnnouncementControllerTests.TEST_OWNER_ID);
		this.george.setFirstName("George");
		this.george.setLastName("Franklin");
		this.george.setAddress("110 W. Liberty St.");
		this.george.setCity("Madison");
		this.george.setTelephone("6085551023");
		User georgeuser = new User();
		georgeuser.setUsername("george");
		this.george.setUser(georgeuser);
		this.george.setId(1);

	}

	// mostrarAnnouncements

	@WithMockUser(value = "spring")
	@Test
	void shouldShowAnnouncements() throws Exception {
		Announcement announcement1 = this.createDummyAnnouncement("announcement1");
		Announcement announcement2 = this.createDummyAnnouncement("announcement2");

		Mockito.when(this.announcementService.findAll()).thenReturn(Lists.newArrayList(announcement1, announcement2));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("announcements"))
			.andExpect(MockMvcResultMatchers.view().name("announcements/announcementsList"));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldNotShowAnnouncements() throws Exception {
		Mockito.when(this.announcementService.findAll()).thenThrow(NoSuchElementException.class);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("isempty"))
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("announcements")).andExpect(MockMvcResultMatchers.view().name("announcements/announcementsList"));
	}

	// mostrarAnnouncement

	@WithMockUser(value = "george")
	@Test
	void shouldShowAnnouncementDetails() throws Exception {
		Announcement announcement1 = this.createDummyAnnouncement("Announcement1");
		announcement1.setOwner(this.george);
		Owner owner = this.george;
		owner.setPositiveHistory(true);

		Mockito.when(this.announcementService.findAnnouncementById(1)).thenReturn(Optional.of(announcement1));
		Mockito.when(this.ownerService.findOwnerByUserName("george")).thenReturn(owner);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}", 1))//
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model()//
				.attributeExists("positiveHistory"))
			.andExpect(MockMvcResultMatchers.model()//
				.attributeExists("announcement"))
			.andExpect(MockMvcResultMatchers.view().name("announcements/announcementDetails"));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldNotShowAnnouncementDetails() throws Exception {
		Mockito.when(this.announcementService.findAnnouncementById(1)).thenThrow(NoSuchElementException.class);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}", AnnouncementControllerTests.TEST_ANNOUNCEMENT_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("announcement")).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldNotShowAnnouncementWhenNotFound() throws Exception {
		Mockito.when(this.announcementService.findAnnouncementById(1)).thenThrow(NoSuchElementException.class);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	// Get createAnnouncement

	@WithMockUser(value = "george")
	@Test
	void shouldCreateAnnouncement() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/new", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("announcement"))
			.andExpect(MockMvcResultMatchers.view().name("announcements/editAnnouncement"));
	}

	// Post createAnnouncement

	@WithMockUser(value = "george")
	@Test
	void shouldSaveAnnouncement() throws Exception {
		Announcement dummyAnnouncement = this.createDummyAnnouncement("announcement");
		PetType petType = new PetType();
		petType.setName("Dog");

		dummyAnnouncement.setType(petType);
		dummyAnnouncement.setCanBeAdopted(true);
		dummyAnnouncement.setPetName("Bibi");

		this.mockMvc.perform(MockMvcRequestBuilders.post("/announcements/new", 1).with(SecurityMockMvcRequestPostProcessors.csrf())//
			.param("canBeAdopted", "true").param("petName", "Bibi")).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/announcements"));
	}

	@WithMockUser(value = "george")
	@Test
	void shouldNotSaveAnnouncementWithFormErrors() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/announcements/new", 1).with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("announcement")).andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("announcement", "petName"))
			//.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("announcement", "type"))//
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("announcement", "canBeAdopted")).andExpect(MockMvcResultMatchers.view().name("announcements/editAnnouncement"));
	}

	// deleteAnnouncement

	@WithMockUser(value = "george")
	@Test
	void shouldDeleteAnnouncement() throws Exception {
		Announcement announcement1 = this.createDummyAnnouncement("Announcement1");
		announcement1.setOwner(this.george);

		Mockito.when(this.announcementService.findAnnouncementById(1)).thenReturn(Optional.of(announcement1));
		Mockito.doNothing().when(this.announcementService).deleteAnnouncement(announcement1);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/delete/{announcementId}", 1)).andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/announcements"));
	}

	@WithMockUser(value = "spring")
	@Test
	void shouldNotDeleteAnnouncementOfOtherUser() throws Exception {
		Announcement announcement1 = this.createDummyAnnouncement("Announcement1");
		announcement1.setOwner(this.george);

		Mockito.when(this.announcementService.findAnnouncementById(1)).thenReturn(Optional.of(announcement1));
		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/delete/{announcementId}", 1)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attribute("message", "You cannot delete another user's announcement details")).andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "george")
	@Test
	void shouldNotDeleteAnnouncementWhenNotFound() throws Exception {
		Announcement announcement1 = this.createDummyAnnouncement("Announcement1");
		announcement1.setOwner(this.george);

		Mockito.when(this.announcementService.findAnnouncementById(200)).thenThrow(NoSuchElementException.class);
		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/delete/{announcementId}", 200)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attribute("message", "Announcement not found"))
			.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	//updateAnnouncement

	@WithMockUser(value = "george")
	@Test
	void shouldUpdateAnnouncement() throws Exception {
		Announcement announcement1 = this.createDummyAnnouncement("Announcement1");
		announcement1.setOwner(this.george);

		Mockito.when(this.announcementService.findAnnouncementById(1)).thenReturn(Optional.of(announcement1));
		Mockito.when(this.ownerService.findOwnerByUserName("george")).thenReturn(this.george);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/update/{announcementId}", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("announcement"))
			.andExpect(MockMvcResultMatchers.view().name("announcements/editAnnouncement"));
	}

	@WithMockUser(value = "george")
	@Test
	void shouldNotUpdateAnotherOwnerAnnouncement() throws Exception {
		Announcement announcement1 = this.createDummyAnnouncement("Announcement1");
		Owner owner = new Owner();
		User user = new User();
		user.setUsername("owner");
		owner.setUser(user);
		announcement1.setOwner(owner);

		Mockito.when(this.announcementService.findAnnouncementById(1)).thenReturn(Optional.of(announcement1));
		Mockito.when(this.ownerService.findOwnerByUserName("george")).thenReturn(this.george);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/update/{announcementId}", 1))//
			.andExpect(MockMvcResultMatchers.status().isOk())//
			.andExpect(MockMvcResultMatchers.model()//
				.attribute("message", "You can't update another owner's announcement"))//
			.andExpect(MockMvcResultMatchers.view().name("/exception"));
	}

	@WithMockUser(value = "george")
	@Test
	void shouldNotUpdateAnnouncementWithIncorrectId() throws Exception {

		Mockito.when(this.announcementService.findAnnouncementById(200)).thenThrow(NoSuchElementException.class);

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/update/{announcementId}", 200))//
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model()//
				.attributeExists("message"))
			.andExpect(MockMvcResultMatchers.model().attribute("message", "There are errors validating data"))//
			.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "george")
	@Test
	void shouldSaveUpdateAnnouncement() throws Exception {
		Announcement announcement1 = this.createDummyAnnouncement("Announcement1");
		PetType petType = new PetType();
		petType.setName("Dog");
		announcement1.setOwner(this.george);
		announcement1.setCanBeAdopted(false);
		announcement1.setPetName("Puppy");
		announcement1.setDescription("This is a text");
		announcement1.setType(petType);

		Mockito.when(this.announcementService.findAnnouncementById(1)).thenReturn(Optional.of(announcement1));
		Mockito.when(this.ownerService.findOwnerByUserName("george")).thenReturn(this.george);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/announcements/update/{announcementId}", 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf())//
			.param("canBeAdopted", "true"))//
			.andExpect(MockMvcResultMatchers.view().name("announcements/announcementDetails"));

	}

	@WithMockUser(value = "george")
	@Test
	void shouldNotSaveUpdateAnnouncementWithIncorretId() throws Exception {
		Announcement announcement1 = this.createDummyAnnouncement("Announcement1");
		PetType petType = new PetType();
		petType.setName("Dog");
		announcement1.setOwner(this.george);
		announcement1.setCanBeAdopted(true);
		announcement1.setDescription("This is a text");
		announcement1.setType(petType);

		Mockito.when(this.announcementService.findAnnouncementById(200)).thenThrow(NoSuchElementException.class);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/announcements/update/{announcementId}", 200)//
			.with(SecurityMockMvcRequestPostProcessors.csrf())//
			.param("canBeAdopted", "true").param("description", "this is a text").param("petType", "Dog"))//
			//.andExpect(MockMvcResultMatchers.model().attribute("message", "Announcement not found"))//
			.andExpect(MockMvcResultMatchers.view().name("exception"));

	}

}
