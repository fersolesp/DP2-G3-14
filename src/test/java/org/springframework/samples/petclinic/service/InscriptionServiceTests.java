
package org.springframework.samples.petclinic.service;


import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;

import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.samples.petclinic.model.Course;
import org.springframework.samples.petclinic.model.Inscription;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.util.InscriptionAssert;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class InscriptionServiceTests {

	@Autowired
	protected InscriptionsService	inscriptionsService;

	@Autowired
	protected OwnerService			ownerService;

	@Autowired
	protected CourseService			courseService;


	@ParameterizedTest
	@CsvSource({
		"owner1", "owner2", "owner3"
	})
	void shouldFindInscriptionsGivingOwner(final String ownername) {
		Owner owner = this.ownerService.findOwnerByUserName(ownername);
		Iterable<Inscription> inscriptions = this.inscriptionsService.findInscriptionsByOwner(owner);
		org.assertj.core.api.Assertions.assertThat(inscriptions).hasSize(1);
	}

	@ParameterizedTest
	@CsvSource({
		"owner8", "owner9"
	})
	void shouldNotFindInscriptionsGivingOwnerWithoutThem(final String ownername) {
		Owner owner = this.ownerService.findOwnerByUserName(ownername);
		Assertions.assertThrows(NoSuchElementException.class, ()->{this.inscriptionsService.findInscriptionsByOwner(owner);});

	}

	@ParameterizedTest
	@CsvSource({
		"1,Inscription1", "2,Inscription2", "3,Inscription3"
	})
	void shouldFindInscriptionWithCorrectId(final int id, final String nameInscription) {
		Optional<Inscription> inscription = this.inscriptionsService.findInscriptionById(id);
		org.assertj.core.api.Assertions.assertThat(inscription).isPresent();

		//Assert personalizado
		InscriptionAssert.assertThat(inscription.get()).hasName(nameInscription);
	}

	@Test
	void shouldNotFindInscriptionWithIncorrectId() {
		Assertions.assertThrows(NoSuchElementException.class, ()->{this.inscriptionsService.findInscriptionById(200);});
	}

	@Test
	@Transactional
	public void shouldInsertInscriptionIntoDatabaseAndGenerateId() throws Exception {
		Owner owner3 = this.ownerService.findOwnerById(3);
		Integer found = IterableUtil.sizeOf(this.inscriptionsService.findInscriptionsByOwner(owner3));
		Pet pet3 = owner3.getPet("Rosy");
		Course course = this.courseService.findCourseById(2).get();

		Inscription inscription = new Inscription();
		inscription.setName("TestInscription");
		inscription.setOwner(owner3);
		inscription.setPet(pet3);
		inscription.setCourse(course);
		inscription.setDate(LocalDate.now().minusMonths(1));
		inscription.setIsPaid(false);

		this.inscriptionsService.saveInscription(inscription);

		org.assertj.core.api.Assertions.assertThat(IterableUtil.sizeOf(this.inscriptionsService.findInscriptionsByOwner(owner3))).isEqualTo(found + 1);

		//Assert personalizado
		InscriptionAssert.assertThat(inscription).idNotNull();
	}

	@Test
	@Transactional
	public void shouldNotInsertInscriptionOfPetInACourseForAnotherTypeOfPet() throws Exception {

		Owner owner3 = this.ownerService.findOwnerById(3);
		//Rosy es un perro
		Pet pet3 = owner3.getPet("Rosy");
		//Este curso es para gatos
		Course course = this.courseService.findCourseById(1).get();

		Inscription inscription = new Inscription();
		inscription.setName("TestInscription");
		inscription.setOwner(owner3);
		inscription.setPet(pet3);
		inscription.setCourse(course);
		inscription.setDate(LocalDate.now().minusMonths(1));
		inscription.setIsPaid(false);

		Assertions.assertThrows(Exception.class,()->{this.inscriptionsService.saveInscription(inscription);},"You can not sign up a pet in other pet type course");

	}

	@Test
	@Transactional
	public void shouldNotInsertInscriptionOfPetNotVaccinated() throws Exception {

		Owner owner3 = this.ownerService.findOwnerById(3);
		//Jewel no está vacunado
		Pet pet3 = owner3.getPet("Jewel");
		Course course = this.courseService.findCourseById(1).get();

		Inscription inscription = new Inscription();
		inscription.setName("TestInscription");
		inscription.setOwner(owner3);
		inscription.setPet(pet3);
		inscription.setCourse(course);
		inscription.setDate(LocalDate.now().minusMonths(1));
		inscription.setIsPaid(false);

		Assertions.assertThrows(Exception.class,()->{this.inscriptionsService.saveInscription(inscription);},"You can not sign up a pet if it is not vaccinated");

	}

	@Test
	@Transactional
	public void shouldInsertInscriptionOfDangerousPetInDangerousPetCourse() throws Exception {
		Owner owner10 = this.ownerService.findOwnerById(10);
		Integer found = 0;
		try {
			found = IterableUtil.sizeOf(this.inscriptionsService.findInscriptionsByOwner(owner10));
		} catch (NoSuchElementException e) {
		}
		//Lucky es una mascota peligrosa
		Pet pet12 = owner10.getPet("Lucky");
		//Este curso admite mascotas peligrosas
		Course course = this.courseService.findCourseById(3).get();

		Inscription inscription = new Inscription();
		inscription.setName("TestInscription");
		inscription.setOwner(owner10);
		inscription.setPet(pet12);
		inscription.setCourse(course);
		inscription.setDate(LocalDate.now().minusMonths(1));
		inscription.setIsPaid(false);

		this.inscriptionsService.saveInscription(inscription);

		org.assertj.core.api.Assertions.assertThat(IterableUtil.sizeOf(this.inscriptionsService.findInscriptionsByOwner(owner10))).isEqualTo(found + 1);

		//Assert personalizado
		InscriptionAssert.assertThat(inscription).idNotNull();

	}

	@Test
	@Transactional
	public void shouldNotInsertInscriptionOfDangerousPetInNotDangerousPetCourse() throws Exception {

		Owner owner3 = this.ownerService.findOwnerById(3);
		//Jewel es una mascota peligrosa
		Pet pet3 = owner3.getPet("Jewel");
		//Este curso no admite mascotas peligrosas
		Course course = this.courseService.findCourseById(1).get();

		Inscription inscription = new Inscription();
		inscription.setName("TestInscription");
		inscription.setOwner(owner3);
		inscription.setPet(pet3);
		inscription.setCourse(course);
		inscription.setDate(LocalDate.now().minusMonths(1));
		inscription.setIsPaid(false);

		Assertions.assertThrows(Exception.class,()->{this.inscriptionsService.saveInscription(inscription);},"You can not sign up a dangerous pet in a not-dangerous pet course");

	}

	@Test
	@Transactional
	public void shouldNotInsertRepeatedInscription() throws Exception {

		Owner owner1 = this.ownerService.findOwnerById(1);
		Pet pet1 = owner1.getPet("Leo");
		//Leo ya está inscrito en este curso
		Course course = this.courseService.findCourseById(1).get();

		Inscription inscription = new Inscription();
		inscription.setName("TestInscription");
		inscription.setOwner(owner1);
		inscription.setPet(pet1);
		inscription.setCourse(course);
		inscription.setDate(LocalDate.now().minusMonths(1));
		inscription.setIsPaid(false);

		Assertions.assertThrows(Exception.class,()->{this.inscriptionsService.saveInscription(inscription);},"You can not sign up your pet in the same course twice");

	}


	@ParameterizedTest
	@CsvSource({
		"1", "2", "3"
	})
	@Transactional
	public void shouldDeleteInscriptionFromDatabase(final int id) {
		Inscription inscription = this.inscriptionsService.findInscriptionById(id).get();
		this.inscriptionsService.deleteInscription(inscription);
		Assertions.assertThrows(NoSuchElementException.class,()->{ this.inscriptionsService.findInscriptionById(id);});
	}

	@Test
	public void shouldNotDeleteInscriptionFromDatabaseWhenNull() {
		Assertions.assertThrows(InvalidDataAccessApiUsageException.class, ()->{this.inscriptionsService.deleteInscription(null);});
	}

	@ParameterizedTest
	@CsvSource({
		"1", "2", "3"
	})
	void shouldFindInscriptionsGivingCourse(final Integer id) {
		Course course = this.courseService.findCourseById(id).get();
		Iterable<Inscription> inscriptions = this.inscriptionsService.findInscriptionsByCourse(course);
		org.assertj.core.api.Assertions.assertThat(inscriptions).hasSize(1);
	}

	@Test
	void shouldNotFindInscriptionsGivingCourseWithoutThem() {
		Course course = this.courseService.findCourseById(4).get();
		Assertions.assertThrows(NoSuchElementException.class, ()->{this.inscriptionsService.findInscriptionsByCourse(course);});

	}

}
