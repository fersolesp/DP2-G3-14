Feature: Answer creation
   I want to create an answer of an announcement
  
  Scenario Outline: I create an answer
    Given I am logged in the system as <user> with password <password>
    When I add a new answer for announcement <announcement> with answer <name>, date <date> and description <description>
    Then the answer appears in my answers
	
	Examples: 
      | user      | password  |   announcement    |          name        	|        date 		 |    description 	  |
      | "owner11" | "0wn3r"  |  "Anuncio2"   	| "respuesta ejemplo 1" | "2020/08/04 20:00" | "this is a text"   |