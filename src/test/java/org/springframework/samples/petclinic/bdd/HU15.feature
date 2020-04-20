Feature: Cannot create appointment for a date with another appointment
   I cannot create an appointment when the hairdresser has another one
   
  Scenario: I cannot create appointment when there's another one
    Given I am logged in the system as "owner1" with password "0wn3r"
		When I try to add a new appointment for pet "Leo" with date "2020/07/20 20:50"
    Then I cannot create appointment because there's another one