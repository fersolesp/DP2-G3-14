
package org.springframework.samples.petclinic.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CatFact {

	String data;


	public CatFact() {
	}

	public String getData() {
		return this.data;
	}

	public void setData(final String data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "{" + "data='" + this.data + '}';
	}
}
