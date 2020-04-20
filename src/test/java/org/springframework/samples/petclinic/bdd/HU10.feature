Feature: Cannot make appointment without paying 
   I cannot make an appointment without paying previous ones
  
  Scenario Outline: I cannot make an appointment
    Given I am logged in the system as <name> with password <password>
    When I add a new appointment for pet <pet> with date <date>
    Then I cannot create an appointment without paying previous ones
	
	Examples: 
      | name     | password |   pet    |        date        |
      | "owner1" | "0wn3r"  |  "Leo"   | "2020/08/04 20:00" |
      | "owner2" | "0wn3r"  | "Basil"  | "2020/08/03 20:00" |