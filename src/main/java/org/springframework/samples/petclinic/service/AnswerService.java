
package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Answer;
import org.springframework.samples.petclinic.repository.AnswerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnswerService {

	private AnswerRepository answerRepo;


	@Autowired
	public AnswerService(final AnswerRepository answerRepository) {
		this.answerRepo = answerRepository;
	}

	@Transactional
	public Iterable<Answer> findAll() {
		return this.answerRepo.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Answer> findAnswerById(final int answerId) {
		return this.answerRepo.findById(answerId);
	}

	@Transactional
	public Collection<Answer> findAnswerByAnnouncement(final int announcementId) {
		return this.answerRepo.findAnswersByAnnouncement(announcementId);
	}
}
