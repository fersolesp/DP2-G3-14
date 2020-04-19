package org.springframework.samples.petclinic.bdd.stepdefinitions;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.boot.web.server.LocalServerPort;

import lombok.extern.java.Log;

@Log
public class LoginStepsDefinitions extends AbstractStep{

	@LocalServerPort
	private int port;

	public static void loginAs(final String user, final String password, final int port, final WebDriver driver) {
		driver.get("http://localhost:"+port);
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		driver.findElement(By.id("username")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(user);
		driver.findElement(By.id("password")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Assert.assertEquals(user.toUpperCase(), driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).getText());
	}
}