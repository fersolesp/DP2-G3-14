
package org.springframework.samples.petclinic.web;

import java.util.Map;

import org.springframework.samples.petclinic.service.CatFactService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {

	CatFactService catFactService;


	@GetMapping({
		"/", "/welcome"
	})
	public String welcome(final Map<String, Object> model) {
		String catFact = this.catFactService.findcatFactService();
		model.put("catFact", catFact);
		return "welcome";
	}
}
