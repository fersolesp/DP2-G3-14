
package org.springframework.samples.petclinic.web.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class PetControllerE2ETest {

	@Autowired
	private MockMvc mockMvc;


	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pets/new", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("pet"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormSuccess() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/new", 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty").param("type", "hamster")//
			.param("birthDate", "2015/02/12").param("dangerous", "false").param("isVaccinated", "true"))//
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())//
			.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/{petId}/edit", 1, 1).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty").param("birthDate", "2015/02/12"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("pet")).andExpect(MockMvcResultMatchers.model().attributeHasErrors("pet")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("pet")).andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(value = "george")
	@Test
	void testProcessCreationFormDangerousAnimalOwnerWithoutLicense() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/new", //
			1, 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty").param("type", "hamster")//
			.param("birthDate", "2015/02/12").param("dangerous", "true").param("isVaccinated", "true"))//
			.andExpect(MockMvcResultMatchers.model()//
				.attribute("message", "You can't add a new dangerous pet if you don't have the dangerous animals license"))
			.andExpect(MockMvcResultMatchers.status().isOk())//
			.andExpect(MockMvcResultMatchers.view().name("/exception"));
	}

	@WithMockUser(value = "george")
	@Test
	void testProcessCreationFormOwnerWithoutNumerousAnimalsLivesInCity() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/new", //
			1, 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty").param("type", "hamster")//
			.param("birthDate", "2015/02/12").param("dangerous", "true").param("isVaccinated", "true"))//
			.andExpect(MockMvcResultMatchers.model()//
				.attribute("message", "You can't add a new pet if you have three pets without the numerous pets license"))
			.andExpect(MockMvcResultMatchers.status().isOk())//
			.andExpect(MockMvcResultMatchers.view().name("/exception"));
	}

	@WithMockUser(value = "george")
	@Test
	void testProcessCreationFormOwnerWithoutNumerousAnimalsAndNotLivesInCity() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/new", //
			1, 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty").param("type", "hamster")//
			.param("birthDate", "2015/02/12").param("dangerous", "true").param("isVaccinated", "true"))//
			.andExpect(MockMvcResultMatchers.model()//
				.attribute("message", "You can't add a new pet if you have three pets without the numerous pets license"))
			.andExpect(MockMvcResultMatchers.status().isOk())//
			.andExpect(MockMvcResultMatchers.view().name("/exception"));
	}

	@WithMockUser(value = "george")
	@Test
	void testProcessCreationFormPetDuplication() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/new", //
			1, 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Puppy").param("type", "hamster")//
			.param("birthDate", "2015/02/12").param("dangerous", "true").param("isVaccinated", "true"))//
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())//
			.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/pets/{petId}/edit", 1, 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeExists("pet"))
			.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/{petId}/edit", 1, 1).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty").param("type", "hamster").param("birthDate", "2015/02/12"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection()).andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/{petId}/edit", 1, 1).with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty").param("birthDate", "2015/02/12"))
			.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("owner")).andExpect(MockMvcResultMatchers.model().attributeHasErrors("pet")).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"));
	}

	@WithMockUser(value = "george")
	@Test
	void testProcessUpdateFormPetDuplication() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/{petId}/edit", //
			1, 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Puppy").param("type", "hamster")//
			.param("birthDate", "2015/02/12").param("dangerous", "true").param("isVaccinated", "true"))//
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())//
			.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"));
	}

	@WithMockUser(value = "george")
	@Test
	void testProcessUpdateFormDangerousAnimalOwnerWithoutLicense() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/{ownerId}/pets/{petId}/edit", //
			1, 1)//
			.with(SecurityMockMvcRequestPostProcessors.csrf()).param("name", "Betty").param("type", "hamster")//
			.param("birthDate", "2015/02/12").param("dangerous", "true").param("isVaccinated", "true"))//
			.andExpect(MockMvcResultMatchers.model()//
				.attribute("message", "You can't add a new dangerous pet if you don't have the dangerous animals license"))
			.andExpect(MockMvcResultMatchers.status().isOk())//
			.andExpect(MockMvcResultMatchers.view().name("/exception"));
	}
}
