using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GoAber.WebService.ChallengesWS
{
    public class ChallengeData
    {
        public string id { get; set; }
        public int categoryUnitId { get; set; }

        public DateTime startTime { get; set; }

        public DateTime endTime { get; set; }

        public string name { get; set; }

        public int communityId { get; set; }
    }
}