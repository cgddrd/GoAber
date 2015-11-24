using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GoAber.Models.ViewModels
{
    public class ViewUserSummary
    {
        public ApplicationUser User { get; set; }
        public IEnumerable<CategoryViewModel> CategoryViews { get; set; }
    }
}