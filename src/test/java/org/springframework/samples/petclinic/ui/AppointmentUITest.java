//
//package org.springframework.samples.petclinic.ui;
//import org.junit.Assert;
//import org.openqa.selenium.By;
//
//
//
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.IntStream;
//
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//import org.openqa.selenium.*;
//import org.openqa.selenium.firefox.FirefoxDriver;
//import org.openqa.selenium.support.ui.Select;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.web.server.LocalServerPort;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import com.google.common.collect.Lists;
//
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class AppointmentUITest {
//
//	@LocalServerPort
//	private int port;
//
//	private WebDriver driver;
//	private String userName;
//	private String baseUrl;
//	private boolean acceptNextAlert = true;
//	private StringBuffer verificationErrors = new StringBuffer();
//
//	@BeforeEach
//	public void setUp() throws Exception {
//		String pathToGeckoDriver=".\\src\\main\\resources";
//		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver + "\\geckodriver.exe");
//		this.driver = new FirefoxDriver();
//		this.baseUrl = "https://www.google.com/";
//		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
//	}
//
//	// Historia de usuario 09
//
//	@ParameterizedTest
//	@CsvSource({"owner1,0wn3r,Leo,2020/08/04 20:00","owner2,0wn3r,Basil,2020/08/03 20:00"})
//	public void testCreateAppointment(final String owner, final String pass, final String pet,final String date) throws Exception {
//		this.as(owner, pass)
//		.whenImLoggedInTheSystem()
//		.thenICanCreateAppointment(pet, date);
//	}
//
//	@Test
//	public void testCanNotCreateAppointmentWithoutPets() throws Exception {
//		this.as("owner11", "0wn3r")
//		.whenImLoggedInTheSystem()
//		.whenIHaveNoPets("Antonio Chaves")
//		.thenICanNotCreateAppointmentWihtoutPets();
//	}
//
//	// Historia de usuario 10
//	// El primer escenario ya se ha realizado en la historia de usuario 9
//
//	@ParameterizedTest
//	@CsvSource({"owner1,0wn3r,Leo,2020/08/04 20:00","owner2,0wn3r,Basil,2020/08/03 20:00"})
//	public void testCanNotCreateAppointmentWithoutPayingOthersAppointments(final String owner, final String pass, final String pet,final String date) throws Exception {
//		this.as(owner, pass)
//		.whenImLoggedInTheSystem()
//		.thenICanCreateAppointment(pet, date)
//		.thenICanNotCreateAppointmentWihtoutPayingOtherAppointments();
//	}
//
//	//Historia de usuario 11
//	// El primer escenario ya se ha realizado en la historia de usuario 9
//
//	@ParameterizedTest
//	@CsvSource({"owner1,0wn3r,Leo,2020/07/20 10:50","owner2,0wn3r,Basil,2020/07/03 18:00"})
//	public void testCanNotCreateAppointmentForSamePetOnSameDay(final String owner, final String pass, final String pet,final String date) throws Exception {
//		this.as(owner, pass)
//		.whenImLoggedInTheSystem()
//		.whenIHaveAnAppointment(pet,date)
//		.thenICanNotCreateAppointmentForSamePetOnSameDay(pet,date);
//	}
//
//	//Historia de usuario 12
//	// El primer escenario ya se ha realizado en la historia de usuario 9
//
//	@ParameterizedTest
//	@CsvSource({"owner1,0wn3r,Leo,2020/07/20 10:50","owner2,0wn3r,Basil,2020/07/03 18:00"})
//	public void testCanNotCreateAppointmentForUnactiveHairdresser(final String owner, final String pass, final String pet,final String date) throws Exception {
//		this.as(owner, pass)
//		.whenImLoggedInTheSystem()
//		.thenICanNotCreateAppointmentForInactiveHairdresser(pet,date);
//	}
//
//	//Historia de usuario 13
//
//	@Test
//	public void testShowHairdressersAndTheirSpecialities() throws Exception {
//		this.as("owner1", "0wn3r")
//		.whenImLoggedInTheSystem()
//		.thenICanSeeHairdressersAndTheirSpecialities();
//	}
//
//	//Historia de usuario 14
//
//	@Test
//	public void testCanDeleteAppointmentWhichIsNotToday() throws Exception {
//		String date = LocalDateTime.now().plus(30, ChronoUnit.DAYS).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
//		this.as("owner1", "0wn3r")
//		.whenImLoggedInTheSystem()
//		.thenICanCreateAppointment("Leo", date)
//		.thenICanDeleteAppointmentWhichIsNotToday();
//	}
//
//	@Test
//	public void testCanNotDeleteAppointmentWhichIsToday() throws Exception {
//		String date = LocalDateTime.now().plus(30, ChronoUnit.MINUTES).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"));
//		this.as("owner1", "0wn3r")
//		.whenImLoggedInTheSystem()
//		.thenICanCreateAppointment("Leo", date)
//		.thenICanNotDeleteTodaysAppointment();
//	}
//
//
//	//Historia de usuario 15
//	// El primer escenario ya se ha realizado en la historia de usuario 9
//
//	@Test
//	public void testCanNotCreateAppointmentForADateWithOtherAppointment() throws Exception {
//		this.as("owner1", "0wn3r")
//		.whenImLoggedInTheSystem()
//		.thenICanNotCreateAppointmentForADateWithOtherAppointment("Leo","2020/07/20 20:50");
//	}
//
//	// Definición de métodos
//
//	private AppointmentUITest as(final String user,final String password) {
//		this.userName = user;
//		this.driver.get("http://localhost:"+this.port);
//		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
//		this.driver.findElement(By.id("username")).click();
//		this.driver.findElement(By.id("username")).clear();
//		this.driver.findElement(By.id("username")).sendKeys(user);
//		this.driver.findElement(By.id("password")).click();
//		this.driver.findElement(By.id("password")).clear();
//		this.driver.findElement(By.id("password")).sendKeys(password);
//		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
//		return this;
//	}
//
//	private AppointmentUITest whenImLoggedInTheSystem() {
//		Assert.assertEquals(this.userName.toUpperCase(), this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).getText());
//		return this;
//	}
//
//	private AppointmentUITest thenICanCreateAppointment(final String pet,final String date) {
//		this.driver.get("http://localhost:"+this.port);
//		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[5]/a/span[2]")).click();
//		this.driver.findElement(By.linkText("George Primero")).click();
//		this.driver.findElement(By.linkText("Add New Appointment")).click();
//		this.driver.findElement(By.id("name")).click();
//		this.driver.findElement(By.id("name")).clear();
//		this.driver.findElement(By.id("name")).sendKeys("CreateAppointmentUITest");
//		this.driver.findElement(By.id("description")).click();
//		this.driver.findElement(By.id("description")).clear();
//		this.driver.findElement(By.id("description")).sendKeys("Description example");
//		this.driver.findElement(By.id("date")).click();
//		this.driver.findElement(By.id("date")).clear();
//		this.driver.findElement(By.id("date")).sendKeys(date);
//		new Select(this.driver.findElement(By.id("pet"))).selectByVisibleText(pet);
//		this.driver.findElement(By.xpath("//option[@value='"+pet+"']")).click();
//		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
//		Assert.assertEquals("Description example", this.driver.findElement(By.linkText("Description example")).getText());
//		this.driver.findElement(By.linkText("Description example")).click();
//		Assert.assertEquals("CreateAppointmentUITest", this.driver.findElement(By.xpath("//td")).getText());
//		return this;
//	}
//
//	private AppointmentUITest whenIHaveNoPets(final String ownerName) {
//		this.driver.get("http://localhost:"+this.port);
//		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li/a/span[2]")).click();
//		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
//		this.driver.findElement(By.linkText(ownerName)).click();
//		int petsNumber = this.driver.findElements(By.xpath("//table[@id='pets']/tbody/tr")).size();
//		Assert.assertEquals(petsNumber, 0);
//		return this;
//	}
//
//	private AppointmentUITest thenICanNotCreateAppointmentWihtoutPets() {
//		this.driver.get("http://localhost:"+this.port);
//		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[5]/a/span[2]")).click();
//		this.driver.findElement(By.linkText("George Primero")).click();
//		this.driver.findElement(By.linkText("Add New Appointment")).click();
//		Assert.assertEquals("You have no pets to make an appointment", this.driver.findElement(By.xpath("//body/div/div/p[2]")).getText());
//		return this;
//	}
//
//	private AppointmentUITest thenICanNotCreateAppointmentWihtoutPayingOtherAppointments() {
//		this.driver.get("http://localhost:"+this.port);
//		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[5]/a/span[2]")).click();
//		this.driver.findElement(By.linkText("George Primero")).click();
//		this.driver.findElement(By.linkText("Add New Appointment")).click();
//		Assert.assertEquals("You have to pay previous appointments", this.driver.findElement(By.xpath("//body/div/div/p[2]")).getText());
//		return this;
//	}
//
//	private AppointmentUITest whenIHaveAnAppointment(final String pet, final String date) {
//		String date2 = new String(date);
//		date2 = date2.replace("/", "-");
//		date2 = date2.split(" ")[0];
//		this.driver.get("http://localhost:"+this.port);
//		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[4]/a/span[2]")).click();
//		Assert.assertEquals(date2, this.driver.findElement(By.xpath("//table[@id='appointmentsTable']/tbody/tr/td[2]")).getText().split(" ")[0]);
//		Assert.assertEquals(pet, this.driver.findElement(By.xpath("//table[@id='appointmentsTable']/tbody/tr/td[3]")).getText());
//		return this;
//	}
//	private AppointmentUITest thenICanNotCreateAppointmentForSamePetOnSameDay(final String pet,final String date) {
//		this.driver.get("http://localhost:"+this.port);
//		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[5]/a/span[2]")).click();
//		this.driver.findElement(By.linkText("George Primero")).click();
//		this.driver.findElement(By.linkText("Add New Appointment")).click();
//		this.driver.findElement(By.id("name")).click();
//		this.driver.findElement(By.id("name")).clear();
//		this.driver.findElement(By.id("name")).sendKeys("CreateAppointmentUITest");
//		this.driver.findElement(By.id("description")).click();
//		this.driver.findElement(By.id("description")).clear();
//		this.driver.findElement(By.id("description")).sendKeys("Description example");
//		this.driver.findElement(By.id("date")).click();
//		this.driver.findElement(By.id("date")).clear();
//		this.driver.findElement(By.id("date")).sendKeys(date);
//		new Select(this.driver.findElement(By.id("pet"))).selectByVisibleText(pet);
//		this.driver.findElement(By.xpath("//option[@value='"+pet+"']")).click();
//		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
//		Assert.assertEquals("You cannot make more than one appointment per day for the same pet", this.driver.findElement(By.xpath("//body/div/div/p[2]")).getText());
//		return this;
//	}
//
//	private AppointmentUITest thenICanNotCreateAppointmentForInactiveHairdresser(final String pet,final String date) {
//		this.driver.get("http://localhost:"+this.port);
//		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[5]/a/span[2]")).click();
//		this.driver.findElement(By.linkText("George Cuarto")).click();
//		boolean buttonNewAppointment = this.driver.findElements(By.linkText("Add New Appointment")).size()!=0;
//		Assert.assertEquals(buttonNewAppointment, false);
//		return this;
//	}
//
//	private AppointmentUITest thenICanSeeHairdressersAndTheirSpecialities() {
//		this.driver.get("http://localhost:"+this.port);
//		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[5]/a/span[2]")).click();
//
//		int haidressersNumber = this.driver.findElements(By.xpath("//table[@id='hairdressersTable']/tbody/tr")).size();
//		Assert.assertEquals(haidressersNumber, 6);
//
//		List<String> hairdressers = Lists.newArrayList("George Primero","George Segundo","George Tercero","George Cuarto","George Quinto","George Sexto");
//		List<String> specialities = Lists.newArrayList("CATS","HAMSTERS","BIRDS","HEDGEHOJS","RABBITS","OTTERS");
//
//		IntStream.range(0, 6).forEach((x)->{
//			Assert.assertEquals(hairdressers.get(x), this.driver.findElement(By.linkText(hairdressers.get(x))).getText());
//			Assert.assertEquals(specialities.get(x), this.driver.findElement(By.xpath("//table[@id='hairdressersTable']/tbody/tr["+(x+1)+"]/td[2]")).getText());
//		});
//		return this;
//	}
//
//	private AppointmentUITest thenICanNotDeleteTodaysAppointment() {
//		this.driver.get("http://localhost:"+this.port);
//		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[4]/a/span[2]")).click();
//		this.driver.findElement(By.linkText("Description example")).click();
//		this.driver.findElement(By.linkText("Delete Appointment")).click();
//
//		Assert.assertEquals("Error: You cannot delete an appointment whose date is today", this.driver.findElement(By.xpath("//body/div/div/p[2]")).getText());
//		return this;
//	}
//
//	private AppointmentUITest thenICanDeleteAppointmentWhichIsNotToday() {
//		this.driver.get("http://localhost:"+this.port);
//		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[4]/a/span[2]")).click();
//		int appointmentsNumber = this.driver.findElements(By.xpath("//table[@id='appointmentsTable']/tbody/tr")).size();
//		Assert.assertEquals(appointmentsNumber, 2);
//		this.driver.findElement(By.linkText("Description example")).click();
//		this.driver.findElement(By.linkText("Delete Appointment")).click();
//		appointmentsNumber = this.driver.findElements(By.xpath("//table[@id='appointmentsTable']/tbody/tr")).size();
//		Assert.assertEquals(appointmentsNumber, 1);
//		return this;
//	}
//
//	private AppointmentUITest thenICanNotCreateAppointmentForADateWithOtherAppointment(final String pet,final String date) {
//		this.driver.get("http://localhost:"+this.port);
//		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[5]/a/span[2]")).click();
//		this.driver.findElement(By.linkText("George Primero")).click();
//		this.driver.findElement(By.linkText("Add New Appointment")).click();
//		this.driver.findElement(By.id("name")).click();
//		this.driver.findElement(By.id("name")).clear();
//		this.driver.findElement(By.id("name")).sendKeys("CreateAppointmentUITest");
//		this.driver.findElement(By.id("description")).click();
//		this.driver.findElement(By.id("description")).clear();
//		this.driver.findElement(By.id("description")).sendKeys("Description example");
//		this.driver.findElement(By.id("date")).click();
//		this.driver.findElement(By.id("date")).clear();
//		this.driver.findElement(By.id("date")).sendKeys(date);
//		new Select(this.driver.findElement(By.id("pet"))).selectByVisibleText(pet);
//		this.driver.findElement(By.xpath("//option[@value='"+pet+"']")).click();
//		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
//		Assert.assertEquals("Hairdresser already has an appointment at that time", this.driver.findElement(By.xpath("//body/div/div/p[2]")).getText());
//		return this;
//	}
//
//	//Métodos del WebDriver
//
//	@AfterEach
//	public void tearDown() throws Exception {
//		this.driver.quit();
//		String verificationErrorString = this.verificationErrors.toString();
//		if (!"".equals(verificationErrorString)) {
//			Assert.fail(verificationErrorString);
//		}
//	}
//
//	private boolean isElementPresent(final By by) {
//		try {
//			this.driver.findElement(by);
//			return true;
//		} catch (NoSuchElementException e) {
//			return false;
//		}
//	}
//
//	private boolean isAlertPresent() {
//		try {
//			this.driver.switchTo().alert();
//			return true;
//		} catch (NoAlertPresentException e) {
//			return false;
//		}
//	}
//
//	private String closeAlertAndGetItsText() {
//		try {
//			Alert alert = this.driver.switchTo().alert();
//			String alertText = alert.getText();
//			if (this.acceptNextAlert) {
//				alert.accept();
//			} else {
//				alert.dismiss();
//			}
//			return alertText;
//		} finally {
//			this.acceptNextAlert = true;
//		}
//	}
//}
