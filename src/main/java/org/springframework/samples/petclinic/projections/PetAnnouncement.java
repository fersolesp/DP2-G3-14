package org.springframework.samples.petclinic.projections;


public interface PetAnnouncement {
	String getId();
	String getName();
	String getPetName();
	String getCanBeAdopted();
	String getType();
}
