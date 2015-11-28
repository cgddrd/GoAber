using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Web;

namespace GoAber.Models.ViewModels
{
    public class ParticipantLeaderViewModel
    {
        [DisplayName("Nick Name")]
        public string NickName { get; set; }
        public double Total { get; set; }
    }
}