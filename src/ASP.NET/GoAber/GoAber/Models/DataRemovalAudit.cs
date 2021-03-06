//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

using System.ComponentModel.DataAnnotations.Schema;

namespace GoAber.Models
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel;
    using System.ComponentModel.DataAnnotations;
    using System.Data.Entity;
    using System.Web.Helpers;

    public class DataRemovalAudit
    {
        public DataRemovalAudit() { }

        public DataRemovalAudit(string message, ActivityData activityDataId, string user)
        {
            this.message = message;
            this.date = DateTime.UtcNow;
            this.ApplicationUserId = user;
            this.dataRemoved = "{\"activityData\": { \"Id\":" + activityDataId.Id;
            this.dataRemoved += "\"categoryUnitId\":" + activityDataId.categoryUnitId;
            this.dataRemoved += "\"ApplicationUserId\":" + activityDataId.ApplicationUserId;
            this.dataRemoved += "\"date\":" + activityDataId.date.ToString() + "} }";
            this.dataRemoved += "\"lastUpdated\":" + activityDataId.lastUpdated.ToString() + "} }";
            this.dataRemoved += "\"value\":" + activityDataId.value + "} }";
        }

        public int Id { get; set; }
        
        [Display(Name = "dataRemovalAudit_message", ResourceType = typeof(Resources.Resources))]
        public string message { get; set; }

        [Display(Name = "dataRemovalAudit_dataRemoved", ResourceType = typeof(Resources.Resources))]
        public string dataRemoved { get; set; }

        [Display(Name = "dataRemovalAudit_date", ResourceType = typeof(Resources.Resources))]
        [DataType(DataType.Date)]
        [DisplayFormat(ApplyFormatInEditMode = true, DataFormatString = "{0:yyyy-MM-dd}")]
        public Nullable<System.DateTime> date { get; set; }


        public string ApplicationUserId { get; set; }
        public virtual ApplicationUser User { get; set; }
    }
}
