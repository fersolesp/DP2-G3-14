package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Appointment;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, Integer>{

	@Override
	Collection<Appointment> findAll() throws DataAccessException;
	
	Collection<Appointment> findAppointmentsByOwner(Owner owner) throws DataAccessException;

}