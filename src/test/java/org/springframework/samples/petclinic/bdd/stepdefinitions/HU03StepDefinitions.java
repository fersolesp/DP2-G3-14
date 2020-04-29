
package org.springframework.samples.petclinic.bdd.stepdefinitions;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class HU03StepDefinitions extends AbstractStep {

	@LocalServerPort
	private int port;


	@When("I add a new answer for announcement {string} with answer {string}, date {string} and description {string}")
	public void iAddANewAnswer(final String announcement, final String name, final String date, final String description) {
		this.getDriver().get("http://localhost:" + this.port);
		this.getDriver().manage().window().setSize(new Dimension(1550, 838));
		this.getDriver().findElement(By.cssSelector(".navbar-right > li:nth-child(1) > a")).click();
		this.getDriver().findElement(By.cssSelector("ul.nav:nth-child(1) > li:nth-child(3) > a:nth-child(1) > span:nth-child(2)")).click();
		this.getDriver().findElement(By.linkText("Anuncio1")).click();
		this.getDriver().findElement(By.linkText("Answer to the announcement")).click();
		{
			WebElement element = this.getDriver().findElement(By.cssSelector(".btn-default"));
			Actions builder = new Actions(this.getDriver());
			builder.moveToElement(element).perform();
		}
		this.getDriver().findElement(By.id("date")).click();
		this.getDriver().findElement(By.linkText("23")).click();
		this.getDriver().findElement(By.id("description")).click();
		this.getDriver().findElement(By.id("description")).sendKeys("Buenas, estoy interesdo.");
		this.getDriver().findElement(By.cssSelector(".btn-default")).click();
	}

	@Then("The page returns to announcements' page")
	public void returnsToAnnouncementPage() {
		this.getDriver().get("http://localhost/:" + this.port);
		this.getDriver().findElement(By.cssSelector("li:nth-child(3) span:nth-child(2)")).click();
		int announcementsNumber = this.getDriver().findElements(By.xpath("//table[@id='announcementsTable']/tbody/tr")).size();
		Assert.assertEquals(announcementsNumber, 3);
		this.stopDriver();
	}
}
