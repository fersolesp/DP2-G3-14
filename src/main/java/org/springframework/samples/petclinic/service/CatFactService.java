
package org.springframework.samples.petclinic.service;

import org.springframework.samples.petclinic.model.CatFact;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CatFactService {

	public String findcatFactService() {
		RestTemplate template = new RestTemplate();
		CatFact catFact = template.getForObject("https://meowfacts.herokuapp.com/", CatFact.class);
		return catFact.getData()[0];
	}
}
