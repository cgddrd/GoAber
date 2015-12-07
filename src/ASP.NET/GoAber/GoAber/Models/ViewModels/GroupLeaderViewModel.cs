using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace GoAber.Models.ViewModels
{
    public class GroupLeaderViewModel
    {
        public string Name { get; set; }
        [Display(Name = "Leader_NumMembers", ResourceType = typeof(Resources.Resources))]
        public int NumMembers { get; set; }
        public double Total { get; set; }
    }
}