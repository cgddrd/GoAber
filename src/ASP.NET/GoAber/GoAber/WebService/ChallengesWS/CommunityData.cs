using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GoAber.WebService.ChallengesWS
{
    public class CommunityData
    {
        public CommunityData()
        {

        }
        public string name { get; set; }
        public string domain { get; set; }
        public string challengesEndpoint { get; set; }
        public string authtoken { get; set; }
    }
}