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

        /// <summary>
        /// Recieves results from foreign community.
        /// Returns home community results for challenge.
        /// </summary>
        /// <param name="result"></param>
        /// <returns></returns>
        [WebMethod]
        [XmlInclude(typeof(ResultData))]
        public ResultData RecieveResult(ResultData result)
        {
            try
            {
                return HandleResult.RecieveResult(result);
            }
            catch (Exception ex)
            {
                return null;
            }
        }
    }
}
