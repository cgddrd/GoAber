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
        [Display(Name = "Audit_IpAddress", ResourceType = typeof(Resources.Resources))]
        public string IpAddress { get; set; }
        [Display(Name = "Audit_UrlAccessed", ResourceType = typeof(Resources.Resources))]
        public string UrlAccessed { get; set; }
        [Display(Name = "Audit_RequestParams", ResourceType = typeof(Resources.Resources))]
        public string RequestParams { get; set; }
        public DateTime Timestamp { get; set; }

        public string ApplicationUserId { get; set; }
        public virtual ApplicationUser user { get; set; }
    }
}