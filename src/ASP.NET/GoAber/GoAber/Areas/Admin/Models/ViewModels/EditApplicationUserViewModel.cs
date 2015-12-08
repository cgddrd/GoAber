using System.Collections.Generic;
using System.Web.Mvc;
using GoAber.Models;
using GoAber.Models.ViewModels;

namespace GoAber.Areas.Admin.Models.ViewModels
{
    /// <summary>
    /// Custom view-model class for passing through both user information and a collection of available Team names.
    /// </summary>
    public class EditApplicationUserViewModel
    {
        public ApplicationUser User { get; set; }
        public string RoleName { get; set; }
        public SelectList TeamItems { get; set; }
    }
}