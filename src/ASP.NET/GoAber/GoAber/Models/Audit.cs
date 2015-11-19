using GoAber.Scheduling;
using GoAber.Scheduling.Interfaces;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;


// based on : http://rion.io/2013/03/03/implementing-audit-trails-using-asp-net-mvc-actionfilters/
namespace GoAber.Models
{
    public class Audit
    {
        public Guid AuditId { get; set; }
        [DisplayName("IP Address")]
        public string IpAddress { get; set; }
        [DisplayName("URL Accessed")]
        public string UrlAccessed { get; set; }
        [DisplayName("Request Parameters")]
        public string RequestParams { get; set; }
        public DateTime Timestamp { get; set; }

        public string ApplicationUserId { get; set; }
        public virtual ApplicationUser user { get; set; }
    }
}