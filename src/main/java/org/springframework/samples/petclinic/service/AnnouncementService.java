
package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Announcement;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.AnnouncementRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnnouncementService {

	private AnnouncementRepository	announcementRepo;
	private PetRepository			petRepo;


	@Autowired
	public AnnouncementService(final AnnouncementRepository announcementRepository, final PetRepository petRepository) {
		this.announcementRepo = announcementRepository;
		this.petRepo = petRepository;
	}

	@Transactional
	public Iterable<Announcement> findAll() {
		return this.announcementRepo.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Announcement> findAnnouncementById(final int announcementId) {
		return this.announcementRepo.findById(announcementId);
	}

	//	@Transactional
	//	public void update(final Announcement announcement, final int announcementId) {
	//		announcement.getId().
	//	}

	@Transactional
	public void saveAnnouncement(final Announcement announcement) throws DataAccessException {
		this.announcementRepo.save(announcement);
	}

	@Transactional(readOnly = true)
	public Collection<PetType> findPetTypes() throws DataAccessException {
		return this.petRepo.findPetTypes();
	}

	@Transactional
	public void deleteAnnouncement(final Announcement announcement) {
		this.announcementRepo.delete(announcement);
	}

}
