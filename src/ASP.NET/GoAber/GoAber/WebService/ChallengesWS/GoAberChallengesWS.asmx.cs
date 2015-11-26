using GoAber.Models;
using GoAber.Services;
using GoAber.WebService.ChallengesWS;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Services;
using System.Xml.Serialization;

namespace GoAber.WebService
{
    /// <summary>
    /// Summary description for GoAberChallengesWS
    /// </summary>
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
    // [System.Web.Script.Services.ScriptService]


    public class GoAberChallengesWS : System.Web.Services.WebService
    {
        private CommunitiesService io_communitiesService;
        private ChallengeService io_challengeService;
        private IEnumerable<Community> io_communities;
        private string[] io_comChallenges;

        public GoAberChallengesWS()
        {

            io_communitiesService = new CommunitiesService();
            io_challengeService = new ChallengeService();
            //io_communities = io_communitiesService.getAllCommunities();
            //IEnumerable<SelectListItem> lo_comchallenges = io_communities.Select(c => new SelectListItem
            //{
            //    Value = c.Id.ToString(),
            //    Text = c.name
            //});

            //io_comChallenges = new string[lo_comchallenges.Count()];
            //int i = 0;
            //foreach (SelectListItem item in lo_comchallenges)
            //{
            //    io_comChallenges[i] = item.Value;
            //    i++;
            //}
        }

        [WebMethod]
        [XmlInclude(typeof(ChallengeData))]
        public bool RecieveChallenge(ChallengeData challenge, int userGroup)
        {
            try {
                Challenge lo_chalmod = new Challenge();
                lo_chalmod.categoryUnitId = challenge.categoryUnitId;
                lo_chalmod.endTime = challenge.endTime;
                lo_chalmod.name = challenge.name;
                lo_chalmod.startTime = challenge.startTime;
                io_challengeService.createChallenge(lo_chalmod);
                io_challengeService.addChallengeToCommunities(lo_chalmod, new string[] { challenge.communityId.ToString() }, userGroup, false);
                return true;
            } catch (Exception ex)
            {
                return false;
            }
        }
    }
}
