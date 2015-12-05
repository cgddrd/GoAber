using System.Collections.Generic;
using System.Web.Mvc;
using GoAber.Models;
using GoAber.Models.ViewModels;

namespace GoAber.Areas.Admin.Models.ViewModels
{
    public class EditApplicationUserViewModel
    {
        public ApplicationUser User { get; set; }
        public string RoleName { get; set; }
        public SelectList TeamItems { get; set; }

        //public IEnumerable<CategoryViewModel> CategoryViews { get; set; }
    }
}