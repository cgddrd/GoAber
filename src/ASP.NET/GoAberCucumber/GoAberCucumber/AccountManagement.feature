Feature: Account Management
	In order to see my dashboard
	And to enter various settings
	I should log in or regsiter

Scenario: Open the system
	Given I navigate to the homepage
	Then I should see "Go Aber"

Scenario: Register User
	Given I navigate to the homepage
	Then I click on "Register"
	And I fill in "Email" with "crh13@aber.ac.uk"
	And I fill in "Password" with "Atestpass!"
	And I fill in "ConfirmPassword" with "Atestpass!"
	And I fill in "Nickname" with "DateOfBirth" with "01-01-1993"
	And I press "Register"
	Then I should see "resgitered"

Scenario: Log in user with missing email
	Given I navigate to the homepage
	Then I click on "Log in"
	And I fill in "Email" with "test@test.com"
	And I press "Log in"
	Then I should see "The Email field is required"

Scenario: Log in user with missing password
	Given I navigate to the homepage
	Then I click on "Log in"
	And I fill in "Email" with "test@test.com"
	And I press "Log in"
	Then I should see "The Password field is required"

Scenario: Log in user with invalid credentials
	Given I navigate to the homepage
	Then I click on "Log in"
	And I fill in "Email" with "test@test.com"
	And I fill in "Password" with "12345678"
	And I press "Log in"
	Then I should see "Invalid login attempt."

Scenario: Log in user with valid credentials
	Given I navigate to the homepage
	Then I go to "Account/Login"
	And I fill in "Email" with "admin@test.com"
	And I fill in "Password" with "Hello123!"
	And I press "Log in"
	Then I should see "Hello adminuser!"

Scenario: Log out user
	Given I navigate to the homepage
	Then I click on "Log off"
	Then I should not see "Hello adminuser!"
