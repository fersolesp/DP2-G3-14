package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Hairdresser;
import org.springframework.stereotype.Repository;

@Repository
public interface HairdresserRepository extends CrudRepository<Hairdresser, Integer>{

	@Override
	Collection<Hairdresser> findAll() throws DataAccessException;
	
}
