
package org.springframework.samples.petclinic.web;

import java.util.stream.Collectors;
import java.util.stream.Stream;

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
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AnnouncementService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ContextConfiguration("/applicationContext.xml")
@WebMvcTest(controllers = CourseController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class AnnouncementControllerTest {

	private static final int	TEST_ANNOUNCEMENT_ID	= 1;

	private static final int	TEST_OWNER_ID			= 2;

	@MockBean
	private AnnouncementService	announcementService;

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
		this.george.setId(AnnouncementControllerTest.TEST_OWNER_ID);
		this.george.setFirstName("George");
		this.george.setLastName("Franklin");
		this.george.setAddress("110 W. Liberty St.");
		this.george.setCity("Madison");
		this.george.setTelephone("6085551023");
		User georgeuser = new User();
		georgeuser.setUsername("george");
		this.george.setUser(georgeuser);

		//		this.lillie = new Pet();
		//		this.lillie.setName("Lillie");
		//		this.lillie.setOwner(this.george);

	}

	@WithMockUser(value = "spring")
	@Test
	void shouldShowAnnouncement() throws Exception {
		Announcement announcement1 = this.createDummyAnnouncement("Announcement1");
		Announcement announcement2 = this.createDummyAnnouncement("Announcement2");

		Mockito.when(this.announcementService.findAll()).thenReturn(Stream.of(announcement1, announcement2).collect(Collectors.toList()));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("announcements"))
			.andExpect(MockMvcResultMatchers.view().name("announcements/announcementsList"));
	}

	//	@WithMockUser(value = "spring")
	//	@Test
	//	void shouldNotShowAnnouncement() throws Exception {
	//		Mockito.when(this.announcementService.findAll()).thenThrow(NoSuchElementException.class);
	//
	//		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("isempty"))
	//			.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("announcements")).andExpect(MockMvcResultMatchers.view().name("announcements/announcementsList"));
	//	}
	//
	//	@WithMockUser(value = "spring")
	//	@Test
	//	void shouldShowAnnouncementDetails() throws Exception {
	//		Announcement announcement1 = this.createDummyAnnouncement("Announcement1");
	//		Mockito.when(this.announcementService.findAnnouncementById(AnnouncementControllerTest.TEST_ANNOUNCEMENT_ID)).thenReturn(Optional.of(announcement1));
	//
	//		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}", AnnouncementControllerTest.TEST_ANNOUNCEMENT_ID)).andExpect(MockMvcResultMatchers.status().isOk())
	//			.andExpect(MockMvcResultMatchers.model().attributeExists("announcement")).andExpect(MockMvcResultMatchers.view().name("announcements/announcementDetails"));
	//	}
	//
	//	@WithMockUser(value = "spring")
	//	@Test
	//	void shouldNotShowAnnouncementDetails() throws Exception {
	//		Mockito.when(this.announcementService.findAnnouncementById(AnnouncementControllerTest.TEST_ANNOUNCEMENT_ID)).thenThrow(NoSuchElementException.class);
	//
	//		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/{announcementId}", AnnouncementControllerTest.TEST_ANNOUNCEMENT_ID)).andExpect(MockMvcResultMatchers.status().isOk())
	//			.andExpect(MockMvcResultMatchers.model().attributeExists("message")).andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("announcement")).andExpect(MockMvcResultMatchers.view().name("exception"));
	//	}
	//
	//	@WithMockUser(value = "george")
	//	@Test
	//	void shouldCreateAnnouncement() throws Exception {
	//
	//		Announcement dummyAnnouncement = this.createDummyAnnouncement("dummyannouncement");
	//
	//		Mockito.when(this.announcementService.findAnnouncementById(1)).thenReturn(Optional.of(dummyAnnouncement));
	//		Mockito.when(this.ownerService.findOwnerByUserName("george")).thenReturn(this.george);
	//
	//		this.mockMvc.perform(MockMvcRequestBuilders.get("/announcements/new", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("announcement"))
	//			.andExpect(MockMvcResultMatchers.view().name("announcements/editAnnouncement"));
	//	}
}
