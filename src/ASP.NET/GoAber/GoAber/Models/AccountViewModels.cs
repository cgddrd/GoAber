using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace GoAber.Models
{
    public class ExternalLoginConfirmationViewModel
    {
        [Required]
        [Display(Name = "account_Email", ResourceType = typeof(Resources.Resources))]
        public string Email { get; set; }
    }

    public class ExternalLoginListViewModel
    {
        public string ReturnUrl { get; set; }
    }

    public class SendCodeViewModel
    {
        public string SelectedProvider { get; set; }
        public ICollection<System.Web.Mvc.SelectListItem> Providers { get; set; }
        public string ReturnUrl { get; set; }
        public bool RememberMe { get; set; }
    }

    public class VerifyCodeViewModel
    {
        [Required]
        public string Provider { get; set; }

        [Required]
        [Display(Name = "account_Code", ResourceType = typeof(Resources.Resources))]
        public string Code { get; set; }
        public string ReturnUrl { get; set; }

        [Display(Name = "account_RememberBroswer", ResourceType = typeof(Resources.Resources))]
        public bool RememberBrowser { get; set; }

        public bool RememberMe { get; set; }
    }

    public class ForgotViewModel
    {
        [Required]
        [Display(Name = "account_Email", ResourceType = typeof(Resources.Resources))]
        public string Email { get; set; }
    }

    public class LoginViewModel
    {
        [Required]
        [Display(Name = "account_Email", ResourceType = typeof(Resources.Resources))]
        [EmailAddress]
        public string Email { get; set; }

        [Required]
        [DataType(DataType.Password)]
        [Display(Name = "account_password", ResourceType = typeof(Resources.Resources))]
        public string Password { get; set; }

        [Display(Name = "account_RemeberMe", ResourceType = typeof(Resources.Resources))]
        public bool RememberMe { get; set; }
    }

    public class RegisterViewModel
    {
        [Required]
        [EmailAddress]
        [Display(Name = "account_Email", ResourceType = typeof(Resources.Resources))]
        public string Email { get; set; }

        [Required]
        [StringLength(100, ErrorMessage = "The {0} must be at least {2} characters long.", MinimumLength = 6)]
        [DataType(DataType.Password)]
        [Display(Name = "account_password", ResourceType = typeof(Resources.Resources))]
        public string Password { get; set; }

        [DataType(DataType.Password)]
        [Display(Name = "account_ConfirmPassword", ResourceType = typeof(Resources.Resources))]
        [Compare("Password", ErrorMessage = "The password and confirmation password do not match.")]
        public string ConfirmPassword { get; set; }

        public string Name { get; set; }

        [Display(Name = "account_Nickname", ResourceType = typeof(Resources.Resources))]
        public string Nickname { get; set; }

        [Display(Name = "account_DoB", ResourceType = typeof(Resources.Resources))]
        [DataType(DataType.Date)]
        public DateTime DateOfBirth { get; set; }

        [Required]
        [Display(Name = "account_Team", ResourceType = typeof(Resources.Resources))]
        public int TeamId { get; set; }
    }

    public class ResetPasswordViewModel
    {
        [Required]
        [EmailAddress]
        [Display(Name = "account_Email", ResourceType = typeof(Resources.Resources))]
        public string Email { get; set; }

        [Required]
        [StringLength(100, ErrorMessage = "The {0} must be at least {2} characters long.", MinimumLength = 6)]
        [DataType(DataType.Password)]
        [Display(Name = "account_password", ResourceType = typeof(Resources.Resources))]
        public string Password { get; set; }

        [DataType(DataType.Password)]
        [Display(Name = "account_ConfirmPassword", ResourceType = typeof(Resources.Resources))]
        [Compare("Password", ErrorMessage = "The password and confirmation password do not match.")]
        public string ConfirmPassword { get; set; }

        public string Code { get; set; }
    }

    public class ForgotPasswordViewModel
    {
        [Required]
        [EmailAddress]
        [Display(Name = "account_Email", ResourceType = typeof(Resources.Resources))]
        public string Email { get; set; }
    }
}
