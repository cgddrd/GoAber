using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GoAber.WebService.ChallengesWS
{
    public class ResultData
    {
        public ResultData()
        {

        }

        public int categoryUnitId { get; set; }
        public int value { get; set; }
        public string challengeId { get; set; }
        public string authtoken { get; set; }
    }
}