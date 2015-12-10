Feature: Manage communities
	In order to edit or delete communities
	I should be logged in as an administrator
	And go to the admin section

Background: Open the system
	Given I navigate to the homepage
	Then I click on "Log in"
	Given I am logged in
	Then I click on "Admin"
	Then I click on "Manage Communities"

Scenario: Create new community
	Then I click on "Create New"
	And I fill in "name" with "Test Community"
	Then I press submit
	Then I should see "Failed to add community, check the endpoints are set correctly"

Scenario: Edit team community
	Then I click on edit for element 2
	And I fill in "name" with "new name"
	Then I press submit
	Then I should not see "BangorUni"

Scenario: Delete community
	Then I click on delete team for element 3
	And I press submit
	Then I should not see "new name"