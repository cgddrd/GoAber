Feature: Manage teams
	In order to edit or delete teams
	I should be logged in as an administrator
	And go to the admin section

Background: Open the system
	Given I navigate to the homepage
	Then I click on "Log in"
	Given I am logged in
	Then I click on "Admin"
	Then I click on "Manage Teams"

Scenario: Create new team
	Then I click on "Create New"
	And I fill in "name" with "Test team"
	Then I press submit
	Then I should see "Test team"

Scenario: Edit team name
	Then I click on edit for element 3
	And I fill in "name" with "new name"
	Then I press submit
	Then I should not see "Test team"

Scenario: Delete team
	Then I click on delete team for element 4
	And I press submit
	Then I should not see "new name"