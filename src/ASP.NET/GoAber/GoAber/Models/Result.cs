using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace GoAber.Models
{
    public class Result
    {
        public Result()
        {

        }
        public int Id {get;set;}
        public int categoryUnitId { get; set; }
        [Display(Name = "result_categoryUnit", ResourceType = typeof(Resources.Resources))]
        public virtual CategoryUnit categoryUnit { get; set; }
        public Nullable<int> value { get; set; }
        [Display(Name = "result_challengeId", ResourceType = typeof(Resources.Resources))]
        public string challengeId { get; set; }
        public virtual Challenge challenge { get; set; }
        [Display(Name = "result_communityId", ResourceType = typeof(Resources.Resources))]
        public int communityId { get; set; }
        public virtual Community community { get; set; }

    }
}