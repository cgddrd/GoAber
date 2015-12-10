Feature: Register user
	In order to edit my details
	I should be able to log in
	And should be able to edit details in management

Scenario: View user account details
	Given I navigate to the homepage
	Then I click on "Log in"
	And I fill in "Email" with "admin@test.com"
	And I fill in "Password" with "Hello123!"
	And I press "Log in"
	And I click on "Hello adminuser!"
	Then I should see "Your Account Details"

Scenario: Edit user nickname
	Given I navigate to the homepage
	Then I click on "Hello adminuser!"
	Then I click on "Manage Account Details"
	And I fill in "Nickname" with "admin"
	And I press "Save"
	Then I should see "admin"
	But I should not see "adminuser"

Scenario: Edit user email
	Given I navigate to the homepage
	Then I click on "Hello adminuser!"
	Then I click on "Manage Account Details"
	And I fill in "Email" with "admin@admin.com"
	And I press "Save"
	Then I should see "admin@admin.com"
	But I should not see "admin@test.com"

Scenario: Edit user date of birth
	Given I navigate to the homepage
	Then I click on "Hello adminuser!"
	Then I click on "Manage Account Details"
	And I fill in "DateOfBirth" with "02-02-1999"
	And I press "Save"
	Then I should see "02/02/1999"
	But I should not see "06/12/2015"

Scenario: Change password with incorrect old password
	Given I navigate to the homepage
	Then I click on "Hello adminuser!"
	Then I click on "Manage Account Details"
	Then I click on "Change your password"
	And I fill in "OldPassword" with "dgfdfbdfb!"
	And I fill in "NewPassword" with "NewPass123!"
	And I fill in "ConfirmPassword" with "NewPass123!"
	And I press "Change password"
	Then I should see "Incorrect password"

Scenario: Change password with no new password
	Given I navigate to the homepage
	Then I click on "Hello adminuser!"
	Then I click on "Manage Account Details"
	Then I click on "Change your password"
	And I fill in "OldPassword" with "Hello123!"
	And I press "Change password"
	Then I should see "The New password field is required"

Scenario: Change password with none matching confirm
	Given I navigate to the homepage
	Then I click on "Hello adminuser!"
	Then I click on "Manage Account Details"
	Then I click on "Change your password"
	And I fill in "OldPassword" with "dgfdfbdfb!"
	And I fill in "NewPassword" with "NewPass123!"
	And I fill in "ConfirmPassword" with "NewPass123456!"
	And I press "Change password"
	Then I should see "The new password and confirmation password do not match"

Scenario: Change password
	Given I navigate to the homepage
	Then I click on "Hello adminuser!"
	Then I click on "Manage Account Details"
	Then I click on "Change your password"
	And I fill in "OldPassword" with "Hello123!"
	And I fill in "NewPassword" with "NewPass123!"
	And I fill in "ConfirmPassword" with "NewPass123!"
	And I press "Change password"
	Then I should see "Your password has been changed"

Scenario: Delete account go back
	Given I navigate to the homepage
	Then I click on "Hello adminuser!"
	Then I click on "Manage Account Details"
	Then I click on "Delete"
	Then I press "Back to List"
	Then I should see "Your Account Details"

Scenario: Delete account
	Given I navigate to the homepage
	Then I click on "Hello adminuser!"
	Then I click on "Manage Account Details"
	Then I click on "Delete"
	Then I press "Yes, delete my account."
	Then I should see "Log in"


