
package org.springframework.samples.petclinic.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.samples.petclinic.model.CatFact;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CatFactService {

	@Cacheable("api")
	public String findcatFactService() {
		RestTemplate template = new RestTemplate();
		CatFact catFact = template.getForObject("https://arcane-ocean-65006.herokuapp.com/", CatFact.class);
		return catFact.getData()[0];
	}
}
