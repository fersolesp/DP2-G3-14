package org.springframework.samples.petclinic.service;

import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.repository.AppointmentRepository;
import org.springframework.samples.petclinic.repository.HairdresserRepository;
import org.springframework.samples.petclinic.repository.PaymentRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {
	
	@Autowired
	private AppointmentRepository appointmentRepo;
	@Autowired
	private HairdresserRepository hairdresserRepo;
	@Autowired
	private PetRepository petRepo;
	@Autowired
	private PaymentRepository paymentRepo;
	
	
	@Autowired
	public AppointmentService(final AppointmentRepository appointmentRepository, 
								final HairdresserRepository hairdresserRepository, 
								final PetRepository petRepository,
								final PaymentRepository paymentRepository) {
		
		this.appointmentRepo = appointmentRepository;
		this.hairdresserRepo = hairdresserRepository;
		this.petRepo = petRepository;
		this.paymentRepo = paymentRepository;
		
	}
	
	@Transactional
	public Iterable<Appointment> findAll() {
		return this.appointmentRepo.findAll();
	}

	@Transactional(readOnly = true)
	public Optional<Appointment> findAppointmentById(final int appointmentId) {
		return this.appointmentRepo.findById(appointmentId);
	}
	
	@Transactional
	public void saveAppointment(final Appointment appointment) throws DataAccessException {
		this.appointmentRepo.save(appointment);
	}
	
	@Transactional
	public void deleteAppointment(final Appointment appointment) throws DataAccessException {
		this.appointmentRepo.delete(appointment);
	}

}
