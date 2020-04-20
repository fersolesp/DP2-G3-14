Feature: Cannot delete appointment which is today
   I cannot delete todays appointments
   
  Scenario: I can delete appointment which is not today
    Given I am logged in the system as "owner1" with password "0wn3r"
	When I add a new appointment for pet "Leo" with date "2020/08/04 20:00"
    Then I can delete appointment which is not today
    
  Scenario: I cannot delete todays appointment
    Given I am logged in the system as "owner1" with password "0wn3r"
    When I create an appointment for today for pet "Leo"
    Then I cannot delete today appointment