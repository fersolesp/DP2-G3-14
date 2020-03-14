
package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Inscription;
import org.springframework.samples.petclinic.repository.InscriptionRepository;
import org.springframework.stereotype.Service;

@Service
public class InscriptionsService {

	InscriptionRepository inscriptionRepository;


	@Autowired
	public InscriptionsService(final InscriptionRepository inscriptionRepository) {
		this.inscriptionRepository = inscriptionRepository;
	}

	public Iterable<Inscription> findInscriptionsByOwnerId() {
		return this.inscriptionRepository.findAll();
	}

	public Optional<Inscription> findInscriptionById(final int inscriptionId) {
		return this.inscriptionRepository.findById(inscriptionId);
	}
}
