Feature: Account Management
	In order to see my dashboard
	And to enter various settings
	I should log in or regsiter

Background: 
	Given I navigate to the homepage
	Then I click on "Log in"
	Given I am logged in

Scenario: View user account details
	Then I click on "Hello adminuser!"
	Then I should see "Your Account Details"

Scenario: Edit user nickname
	Then I click on "Hello adminuser!"
	Then I click on "Manage Account Details"
	And I fill in "Nickname" with "admin"
	And I press enter inside "Nickname"
	Then I should see "admin"
	But I should not see "adminuser"

Scenario: Edit user date of birth
	Then I click on "Hello admin!"
	Then I click on "Manage Account Details"
	And I fill in "DateOfBirth" with "02-02-1999"
	And I press enter inside "DateOfBirth"
	Then I should see "2/2/1999"
	But I should not see "06/12/2015"

Scenario: Change password with incorrect old password
	Then I click on "Hello admin!"
	Then I click on "Manage Account Details"
	Then I click on "Change your password"
	And I fill in "OldPassword" with "dgfdfbdfb!"
	And I fill in "NewPassword" with "NewPass123!"
	And I fill in "ConfirmPassword" with "NewPass123!"
	And I press enter inside "ConfirmPassword"
	Then I should see "Incorrect password"

Scenario: Change password with no new password
	Then I click on "Hello admin!"
	Then I click on "Manage Account Details"
	Then I click on "Change your password"
	And I fill in "OldPassword" with "Hello123!"
	And I press enter inside "OldPassword"
	Then I should see "The New password field is required"

Scenario: Change password with none matching confirm
	Then I click on "Hello admin!"
	Then I click on "Manage Account Details"
	Then I click on "Change your password"
	And I fill in "OldPassword" with "dgfdfbdfb!"
	And I fill in "NewPassword" with "NewPass123!"
	And I fill in "ConfirmPassword" with "NewPass123456!"
	And I press enter inside "ConfirmPassword"
	Then I should see "The new password and confirmation password do not match"

Scenario: Change password
	Then I click on "Hello admin!"
	Then I click on "Manage Account Details"
	Then I click on "Change your password"
	And I fill in "OldPassword" with "Hello123!"
	And I fill in "NewPassword" with "NewPass123!"
	And I fill in "ConfirmPassword" with "NewPass123!"
	And I press enter inside "ConfirmPassword"
	Then I should see "Your password has been changed"
	Then I click on "Change your password"
	And I fill in "OldPassword" with "NewPass123!"
	And I fill in "NewPassword" with "Hello123!"
	And I fill in "ConfirmPassword" with "Hello123!"
	And I press enter inside "ConfirmPassword"

Scenario: Revoke details changes
	Then I click on "Hello admin!"
	Then I click on "Manage Account Details"
	And I fill in "Nickname" with "adminuser"
	And I press enter inside "Nickname"

Scenario: Delete account go back
	Then I click on "Hello adminuser!"
	Then I click on "Manage Account Details"
	Then I click on "Delete"
	Then I click on "Back to List"
	Then I should see "Your Account Details"

Scenario: Delete account
	Then I click on "Hello adminuser!"
	Then I click on "Manage Account Details"
	Then I click on "Delete"
	Then I press delete
	Then I should see "Log in"