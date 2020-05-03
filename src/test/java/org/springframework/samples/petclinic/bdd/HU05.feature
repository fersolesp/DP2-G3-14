Feature: Limit of pets by owner
   I dont want to add a pet if a owner has not a special permission
  
  Scenario: I add a pet with special permission
    Given I am logged in the system as "owner3" with password "0wn3r"
    When I add a new pet with name "misifu" and petType "cat" exceeding the limit with special permission
    Then the pet with name "misifu" and petType "cat" appears in my owners profile exceeding the limit with special permission

	Scenario Outline: I add a pet having enough space
    Given I am logged in the system as <user> with password <password>
    When I add a new pet with name <name> and petType <petType>
    Then the pet with name <name> and petType <petType> appears in my owners profile
    
	Examples: 
      | user     | password | name     | petType |
      | "owner4" | "0wn3r"  | "subaru" | "dog"   |
      
	Scenario: I add a pet without special permission
    Given I am logged in the system as "owner13" with password "0wn3r"
    When I add a new pet with name "stradivarius" and petType "cat" exceeding the limit
    Then the pet with name "stradivarius" and petType "cat" appears in my owners profile exceeding the limit