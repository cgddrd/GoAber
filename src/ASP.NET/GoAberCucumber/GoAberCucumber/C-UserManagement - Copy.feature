Feature: Manage users
	In order to edit or delete users
	I should be logged in as an administrator
	And go to the admin section

Background: Open the system
	Given I navigate to the homepage
	Then I click on "Log in"
	Given I am logged in
	Then I click on "Admin"
	Then I click on "Manage Users"

Scenario: Delete User
	Then I click on delete user for element 3
	And I press submit
	Then I should not see "crh13@aber.ac.uk"

Scenario: Check user is not an admin
	Then I click on "Log off"
	Then I click on "Log in"
	And I fill in "Email" with "user1@test.com"
	And I fill in "Password" with "Hello123!"
	Then I press enter inside "Password"
	And I should not see "Admin"

Scenario: Change user into an admin
	Then I click on edit for element 3
	Then I select "Administrator" from "RoleName"
	And I press submit

Scenario: Check user is an admin
	Then I click on "Log off"
	Then I click on "Log in"
	And I fill in "Email" with "user1@test.com"
	And I fill in "Password" with "Hello123!"
	Then I press enter inside "Password"
	And I should see "Admin"