Feature: Register user
	In order to edit my details
	I should be able to log in
	And should be able to edit details in management

Scenario: Open the system
	Given I navigate to the homepage
	Then I should see "Go Aber"

Scenario: Register User
	Given I navigate to the homepage
	Then I should see "Register"
	Then I click on "Register"
	And I fill in "Email" with "crh13@aber.ac.uk"
	And I fill in "Password" with "Atestpass!0"
	And I fill in "ConfirmPassword" with "Atestpass!0"
	And I fill in "Nickname" with "atestUser"
	And I fill in "DateOfBirth" with "01-01-1993"
	And I press enter inside "DateOfBirth"
	Then I should see "Log off"

Scenario: Log in user with missing email
	Given I navigate to the homepage
	Then I click on "Log in"
	And I fill in "Email" with "test@test.com"
	And I press enter inside "Password"
	Then I should see "The Email field is required"

Scenario: Log in user with missing password
	Given I navigate to the homepage
	Then I click on "Log in"
	And I fill in "Email" with "test@test.com"
	And I press enter inside "Password"
	Then I should see "The Password field is required"

Scenario: Log in user with invalid credentials
	Given I navigate to the homepage
	Then I click on "Log in"
	And I fill in "Email" with "test@test.com"
	And I fill in "Password" with "12345678"
	And I press enter inside "Password"
	Then I should see "Invalid login"

Scenario: Log in user with valid credentials
	Given I navigate to the homepage
	Then I click on "Log in"
	And I fill in "Email" with "admin@test.com"
	And I fill in "Password" with "Hello123!"
	And I press enter inside "Password"
	Then I should see "Hello adminuser!"

Scenario: Log out user
	Given I am logged in
	Then I click on "Log off"
	Then I should not see "Hello adminuser!"


