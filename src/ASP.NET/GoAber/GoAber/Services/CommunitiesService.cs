using GoAber.Models;
using GoAber.WebService.ChallengesWS;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GoAber.Services
{
    public class CommunitiesService
    {
        private ApplicationDbContext db = new ApplicationDbContext();

        public IEnumerable<Community> getAllCommunities()
        {
            return db.Communities;
        }

        public Community RequestContract(Community ao_community)
        {
            try {
                if (ao_community.home) return ao_community;

                GoAberChallengeWSConsumer lo_challengeconsumer = GoAberChallengeWSConsumer.GetInstance();
                return lo_challengeconsumer.RequestContract(ao_community);
            } catch (Exception e)
            {
                return null;
            }
        }
    }
}