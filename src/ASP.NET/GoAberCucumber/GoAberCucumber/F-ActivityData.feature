Feature: Manage Activity Data
	In order to edit or delete activity data
	I should be logged in
	And go to the admin section

Background: Open the system
	Given I navigate to the homepage
	Then I click on "Log in"
	Given I am logged in
	Then I click on "Admin"
	Then I click on "Manage Activity Data"

Scenario: Create new data
	Then I click on "Create New"
	And I fill in "value" with "1234"
	And I fill in "date" with "10/10/2015"
	Then I press submit
	Then I should see "1234"

Scenario: Delete data
	Then I click on delete team for element 2
	And I press submit
	Then I should not see "1234"