using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Web;

namespace GoAber.Models.ViewModels
{
    public class GroupLeaderViewModel
    {
        public string Name { get; set; }
        [DisplayName("Number of Members")]
        public int NumMembers { get; set; }
        public double Total { get; set; }
    }
}