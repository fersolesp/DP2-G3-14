
package org.springframework.samples.petclinic.service;

import org.springframework.samples.petclinic.model.CatFact;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CatFactService {

	public String findcatFactService() {
		RestTemplate template = new RestTemplate();
		CatFact catFact = template.getForObject("https://meowfacts.herokuapp.com/", CatFact.class);
		return catFact.getData();
	}
}
