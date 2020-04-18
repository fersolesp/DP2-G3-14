
package org.springframework.samples.petclinic.ui;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class InscriptionUITest {

	@LocalServerPort
	private int				port;

	private WebDriver		driver;
	private String			baseUrl;
	private String			userName;
	private boolean			acceptNextAlert		= true;
	private StringBuffer	verificationErrors	= new StringBuffer();


	@BeforeEach
	public void setUp() throws Exception {
		String pathToGeckoDriver = ".\\src\\main\\resources";
		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver + "\\geckodriver.exe");
		this.driver = new FirefoxDriver();
		this.baseUrl = "https://www.google.com/";
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	//Historia de Usuario ?
	@ParameterizedTest
	@CsvSource({
		"owner1,Curso para gatos,Leo,2020/04/03"
	})
	public void testCanNotCreateInscriptionIfThereIsAPetOnTheSameCourse(final String owner, final String course, final String pet, final String date) throws Exception {
		this.as(owner, "0wn3r")//
			.whenImLoggedInTheSystem().thenICanNotCreateInscriptionIfThereIsAPetOnTheSameCourse(course, pet, date);
	}

	//Hisotia de Usuario 16
	@ParameterizedTest
	@CsvSource({
		"owner4,Curso para gatos,Iggy,2020/04/03"
	})
	public void testCreateInscription(final String owner, final String course, final String pet, final String date) {
		this.as(owner, "0wn3r")//
			.whenImLoggedInTheSystem().thenICanCreteInscription(course, pet, date);
	}

	//Historia de Usuario 17
	@ParameterizedTest
	@CsvSource({
		"owner2,Curso para gatos,Basil,2020/04/03"
	})
	public void testCanNotCreateInscriptionIfPetNotVaccinated(final String owner, final String course, final String pet, final String date) {
		this.as(owner, "0wn3r")//
			.whenImLoggedInTheSystem().thenICanNotCreateInscriptionIfPetIsNotVaccinated(course, pet, date);
	}

	//Historia de Usuario 18
	@ParameterizedTest
	@CsvSource({
		"owner3,Curso para gatos"
	})
	public void testCanNotCreateInscriptionIfThereIsUnpaidCourses(final String owner, final String course) {
		this.as(owner, "0wn3r")//
			.whenImLoggedInTheSystem().thenICanNotCreateInscriptionIfThereIsUnpaidCourse(course);
	}

	//Definición de métodos

	private InscriptionUITest as(final String user, final String password) {
		this.userName = user;
		this.driver.get("http://localhost:" + this.port);
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		this.driver.findElement(By.id("username")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys(user);
		this.driver.findElement(By.id("password")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys(password);
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		Assert.assertEquals(user.toUpperCase(), this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).getText());
		return this;
	}

	private InscriptionUITest whenImLoggedInTheSystem() {
		Assert.assertEquals(this.userName.toUpperCase(), this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).getText());
		return this;
	}
	private InscriptionUITest whenIHaveNoPets(final String ownerName) {
		this.driver.get("http://localhost:" + this.port);
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li/a/span[2]")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.linkText(ownerName)).click();
		int petsNumber = this.driver.findElements(By.xpath("//table[@id='pets']/tbody/tr")).size();
		Assert.assertEquals(petsNumber, 0);
		return this;
	}

	private InscriptionUITest thenICanCreteInscription(final String course, final String pet, final String date) {
		this.driver.get("http://localhost:" + this.port);
		this.driver.manage().window().setSize(new Dimension(816, 588));
		this.driver.findElement(By.cssSelector("li:nth-child(6) span:nth-child(2)")).click();
		this.driver.findElement(By.linkText(course)).click();
		this.driver.findElement(By.linkText("Create Inscription")).click();
		this.driver.findElement(By.id("date")).click();
		this.driver.findElement(By.id("date")).clear();
		this.driver.findElement(By.id("date")).sendKeys(date);
		WebElement dropdown = this.driver.findElement(By.id("pet"));
		dropdown.findElement(By.xpath("//option[. = '" + pet + "']")).click();
		this.driver.findElement(By.cssSelector("option")).click();
		this.driver.findElement(By.cssSelector(".btn-default")).click();

		return this;

	}

	private InscriptionUITest thenICanNotCreateInscriptionIfPetIsNotVaccinated(final String course, final String pet, final String date) {
		this.driver.get("http://localhost:" + this.port);
		this.driver.findElement(By.cssSelector("li:nth-child(6) > a")).click();
		this.driver.findElement(By.linkText(course)).click();
		this.driver.findElement(By.linkText("Create Inscription")).click();
		this.driver.findElement(By.id("date")).click();
		this.driver.findElement(By.linkText("17")).click();
		this.driver.findElement(By.id("date")).click();
		this.driver.findElement(By.id("date")).clear();
		this.driver.findElement(By.id("date")).sendKeys(date);
		WebElement dropdown = this.driver.findElement(By.id("pet"));
		dropdown.findElement(By.xpath("//option[. = '" + pet + "']")).click();
		this.driver.findElement(By.cssSelector("option")).click();
		this.driver.findElement(By.cssSelector(".btn-default")).click();

		Assert.assertEquals("Error: You can not sign up a pet if it is not vaccinated", this.driver.findElement(By.xpath("//body/div/div/p[2]")).getText());
		return this;
	}

	private InscriptionUITest thenICanNotCreateInscriptionIfThereIsAPetOnTheSameCourse(final String course, final String pet, final String date) {
		this.driver.get("http://localhost:" + this.port);
		this.driver.findElement(By.cssSelector("li:nth-child(6) span:nth-child(2)")).click();
		this.driver.findElement(By.linkText(course)).click();
		this.driver.findElement(By.linkText("Create Inscription")).click();
		this.driver.findElement(By.id("date")).click();
		this.driver.findElement(By.id("date")).clear();
		this.driver.findElement(By.id("date")).sendKeys(date);
		WebElement dropdown = this.driver.findElement(By.id("pet"));
		dropdown.findElement(By.xpath("//option[. = '" + pet + "']")).click();
		this.driver.findElement(By.cssSelector("option")).click();
		this.driver.findElement(By.cssSelector(".btn-default")).click();

		Assert.assertEquals("Error: You can not sign up your pet in the same course twice", this.driver.findElement(By.xpath("//body/div/div/p[2]")).getText());
		return this;
	}

	private InscriptionUITest thenICanNotCreateInscriptionIfThereIsUnpaidCourse(final String course) {
		this.driver.get("http://localhost:" + this.port);
		this.driver.findElement(By.cssSelector("li:nth-child(6) > a")).click();
		this.driver.findElement(By.linkText("Curso para gatos")).click();
		this.driver.findElement(By.linkText("Create Inscription")).click();
		Assert.assertEquals("You have to pay previous courses inscriptions", this.driver.findElement(By.xpath("//body/div/div/p[2]")).getText());
		return this;
	}

	//Métodos del WebDriver

	@AfterEach
	public void tearDown() throws Exception {
		this.driver.quit();
		String verificationErrorString = this.verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			Assert.fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(final By by) {
		try {
			this.driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			this.driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = this.driver.switchTo().alert();
			String alertText = alert.getText();
			if (this.acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			this.acceptNextAlert = true;
		}
	}
}
