Feature: Cannot make appointment for same pet on same day
   I cannot make various appointments for a same pet on the same day
  
  Scenario Outline: I cannot make an appointment for same pet on same day
    Given I am logged in the system as <name> with password <password>
    And I have an appointment for pet <pet> on <date>
    When I try to add a new appointment for pet <pet> with date <date>
    Then I cannot create an appointment for pet <pet> on same date <date>
	
	Examples:
      | name     | password |   pet    |        date        |
      | "owner1" | "0wn3r"  |  "Leo"   | "2020/07/20 10:50" |
      | "owner2" | "0wn3r"  | "Basil"  | "2020/07/03 18:00" |