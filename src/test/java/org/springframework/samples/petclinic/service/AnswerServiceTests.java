
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Announcement;
import org.springframework.samples.petclinic.model.Answer;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class AnswerServiceTests {

	@Autowired
	protected AnswerService			answerService;

	@Autowired
	protected OwnerService			ownerService;

	@Autowired
	protected PetService			petService;

	@Autowired
	protected AnnouncementService	announcementService;


	@Test
	void shouldFindAllAnswers() {
		Collection<Answer> answers = (Collection<Answer>) this.answerService.findAll();
		org.assertj.core.api.Assertions.assertThat(answers.size()).isEqualTo(3);
	}

	@Test
	void shouldFindAnswersByAnnouncement() {
		Announcement announcement = this.announcementService.findAnnouncementById(1).get();
		Collection<Answer> answers = (Collection<Answer>) this.answerService.findAnswerByAnnouncement(announcement);
		org.assertj.core.api.Assertions.assertThat(answers.size()).isEqualTo(2);
	}

	@Test
	void shouldNotFindAnswerGivingAnnouncementWithoutThem() {
		Announcement announcement = this.announcementService.findAnnouncementById(3).get();
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.answerService.findAnswerByAnnouncement(announcement);
		});
	}

	@Test
	void shouldNotFindAnswersWithIncorrectId() {
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			this.answerService.findAnswerById(200);
		});
	}

	@Test
	void shouldFindSingleAnswerById() {
		Answer answer = this.answerService.findAnswerById(1);
		org.assertj.core.api.Assertions.assertThat(answer.getName()).isEqualTo("Respuesta1");
		org.assertj.core.api.Assertions.assertThat(answer.getDate()).isEqualTo("2010-03-09");
		org.assertj.core.api.Assertions.assertThat(answer.getDescription()).isEqualTo("Hola");
		org.assertj.core.api.Assertions.assertThat(answer.getOwner()).isNotNull();
		org.assertj.core.api.Assertions.assertThat(answer.getAnnouncement()).isNotNull();
	}

	@Test
	@Transactional
	public void shouldInsertAnswer() {
		Collection<Answer> answers = (Collection<Answer>) this.answerService.findAll();
		int found = answers.size();

		Answer answer = new Answer();
		Announcement announcement = this.announcementService.findAnnouncementById(1).get();
		Owner owner2 = this.ownerService.findOwnerById(2);
		answer.setOwner(owner2);
		answer.setAnnouncement(announcement);
		answer.setDate(LocalDate.of(2020, 3, 1));
		answer.setDescription("Hola");
		answer.setName("Respuesta4");
		answer.setId(4);

		this.answerService.saveAnswer(answer);
		org.assertj.core.api.Assertions.assertThat(answer.getId().longValue()).isNotEqualTo(0);

		answers = (Collection<Answer>) this.answerService.findAll();
		org.assertj.core.api.Assertions.assertThat(answers.size()).isEqualTo(found + 1);
	}

}
