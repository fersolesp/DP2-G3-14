
package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Announcement;
import org.springframework.samples.petclinic.repository.AnnouncementRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AnnouncementService {

	private AnnouncementRepository announcementRepo;


	@Autowired
	public AnnouncementService(final AnnouncementRepository announcementRepository) {
		this.announcementRepo = announcementRepository;
	}

	@Transactional
	public int announcementsCount() {
		return (int) this.announcementRepo.count();
	}

	@Transactional
	public Iterable<Announcement> findAll() {
		return this.announcementRepo.findAll();
	}

	@Transactional(readOnly = true)
	public Announcement findAnnouncementById(final int announcementId) {
		return this.announcementRepo.findById(announcementId).get();
	}

	@Transactional
	public void save(final Announcement announcement) {
		this.announcementRepo.save(announcement);
	}

	//	@Transactional
	//	public void update(final Announcement announcement, final int announcementId) {
	//		announcement.getId().
	//	}

}
