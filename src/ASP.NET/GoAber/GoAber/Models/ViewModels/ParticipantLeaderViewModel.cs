using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Web;

namespace GoAber.Models.ViewModels
{
    public class ParticipantLeaderViewModel
    {
        public ApplicationUser User { get; set; }
        public double Total { get; set; }
    }
}