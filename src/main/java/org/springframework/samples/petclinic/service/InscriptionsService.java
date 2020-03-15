
package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Inscription;
import org.springframework.samples.petclinic.repository.InscriptionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InscriptionsService {

	InscriptionRepository inscriptionRepository;


	@Autowired
	public InscriptionsService(final InscriptionRepository inscriptionRepository) {
		this.inscriptionRepository = inscriptionRepository;
	}

	public Iterable<Inscription> findInscriptionsByOwner(final org.springframework.samples.petclinic.model.Owner owner) {
		return this.inscriptionRepository.findInscriptionsByOwner(owner);
	}

	public Optional<Inscription> findInscriptionById(final int inscriptionId) {
		return this.inscriptionRepository.findById(inscriptionId);
	}

	@Transactional
	public void saveInscription(final Inscription inscription) throws DataAccessException {
		this.inscriptionRepository.save(inscription);
	}

	@Transactional
	public void deleteInscription(final Inscription inscription) {
		this.inscriptionRepository.delete(inscription);
	}
}
