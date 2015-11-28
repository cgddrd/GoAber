using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace GoAber.Models
{
    public class WebServiceAuth
    {
        public WebServiceAuth()
        {
            status_flag = true;
        }

        [Key]
        public string authtoken { get; set; }
        public string appname { get; set; }
        public string ApplicationUserId { get; set; }
        public Nullable<DateTime> expire { get; set; }
        public bool status_flag { get; set; }
        public ApplicationUser user { get; set; }


    }
}