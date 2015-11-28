using System;
using System.Collections.Generic;
using System.ComponentModel;
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
        [DisplayName("Activity type")]
        public virtual CategoryUnit categoryUnit { get; set; }
        public Nullable<int> value { get; set; }
        public string challengeId { get; set; }
        public virtual Challenge challenge { get; set; }
        public int communityId { get; set; }
        public virtual Community community { get; set; }

    }
}