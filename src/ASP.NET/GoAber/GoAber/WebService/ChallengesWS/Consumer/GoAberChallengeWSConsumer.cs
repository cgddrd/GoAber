using GoAber.Models;
using GoAber.RemoteChallengeWS;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Web;

namespace GoAber.WebService.ChallengesWS
{
    public class GoAberChallengeWSConsumer
    {

        private ApplicationDbContext db = new ApplicationDbContext();
        private static GoAberChallengeWSConsumer io_instance;
        protected GoAberChallengeWSConsumer() {}

        public static GoAberChallengeWSConsumer GetInstance()
        {
            if (io_instance == null)
            {
                io_instance = new GoAberChallengeWSConsumer();
            }
            return io_instance;
        }


        public GoAberChallengesWSSoapClient GetSOAPClient(Community ao_com)
        {
            Uri lo_host = new Uri(ao_com.domain);
            Uri lo_endpoint = new Uri(lo_host, ao_com.challengesEndpoint);
            GoAberChallengesWSSoapClient lo_service = new GoAberChallengesWSSoapClient("GoAberChallengesWSSoap", lo_endpoint.AbsoluteUri);
            return lo_service;
        }

        public bool AddChallenge(Challenge ao_challenge, int ai_userGroup)
        {
            try {
                IQueryable<Community> lo_homecomquery = from coms in db.Communities
                                              where coms.home == true
                                              select coms;

                Community lo_homecom = lo_homecomquery.First();

                List<KeyValuePair<string, bool>> lo_results = new List<KeyValuePair<string, bool>>();
                foreach (CommunityChallenge lo_comchal in ao_challenge.communityChallenges)
                {
                    RemoteChallengeWS.ChallengeData lo_chaldata = new RemoteChallengeWS.ChallengeData();
                    lo_chaldata.categoryUnitId = ao_challenge.categoryUnitId;
                    if (ao_challenge.endTime != null)
                    {
                        lo_chaldata.endTime = (DateTime)ao_challenge.endTime;
                    }
                    lo_chaldata.startTime = ao_challenge.startTime;
                    lo_chaldata.name = ao_challenge.name;
                    lo_chaldata.communityId = lo_homecom.Id;
                    lo_chaldata.id = ao_challenge.Id;

                    int li_id = lo_comchal.communityId;
                    IQueryable<Community> query = from coms in db.Communities
                                                  where coms.Id == li_id
                                                  select coms;

                    Community lo_com = query.First();
                    GoAberChallengesWSSoapClient lo_service = GetSOAPClient(lo_com);

                    bool lb_res = lo_service.RecieveChallenge(lo_chaldata, ai_userGroup);
                    lo_results.Add(new KeyValuePair<string, bool>(lo_chaldata.name, lb_res));
                }


                for (int i = 0; i < lo_results.Count; i++)
                {
                    if (!lo_results[i].Value) return false;
                }
                return true;
            }
            catch (Exception ex)
            {
                return false;
            }
       }

        public Result SendResult(Result ao_res, Community ao_sendto)
        {
            try {
                RemoteChallengeWS.ResultData lo_resdata = new RemoteChallengeWS.ResultData();
                lo_resdata.categoryUnitId = ao_res.categoryUnitId;
                lo_resdata.communityId = ao_res.community.Id;
                lo_resdata.value = ao_res.value.Value;
                lo_resdata.challengeId = ao_res.challengeId;


                GoAberChallengesWSSoapClient lo_service = GetSOAPClient(ao_sendto);

                RemoteChallengeWS.ResultData lo_responsedata = lo_service.RecieveResult(lo_resdata);

                Result lo_response = new Result();
                lo_response.categoryUnitId = lo_responsedata.categoryUnitId;
                lo_response.challengeId = lo_responsedata.challengeId;
                lo_response.communityId = lo_responsedata.communityId;
                lo_response.value = lo_responsedata.value;

                return lo_response;
            } catch (Exception e)
            {
                Debug.Write(e.StackTrace);
                return null;
            }
        }
    }

}