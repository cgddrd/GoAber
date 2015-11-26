using GoAber.Models;
using GoAber.RemoteChallengeWS;
using System;
using System.Collections.Generic;
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

        public bool AddChallenge(Challenge ao_challenge, int ai_userGroup)
        {
            try {
                List<RemoteChallengeWS.ChallengeData> lo_chaldatalist = new List<RemoteChallengeWS.ChallengeData>();
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
                    lo_chaldata.communityId = lo_comchal.communityId;
                    lo_chaldatalist.Add(lo_chaldata);
                }

                List<KeyValuePair<string, bool>> lo_results = new List<KeyValuePair<string, bool>>();


                for (int i = 0; i < lo_chaldatalist.Count; i++)
                {
                    int li_id = lo_chaldatalist[i].communityId;
                    IQueryable<Community> query = from coms in db.Communities
                                                  where coms.Id == li_id
                                                  select coms;

                    Community lo_com = query.First();
                    Uri lo_host = new Uri(lo_com.endpointUrl);
                    Uri lo_endpoint = new Uri(lo_host, @"WebService/ChallengesWS/GoAberChallengesWS.asmx");
                    GoAberChallengesWSSoapClient lo_service = new GoAberChallengesWSSoapClient("GoAberChallengesWSSoap", lo_endpoint.AbsoluteUri);

                    bool lb_res = lo_service.RecieveChallenge(lo_chaldatalist[i], ai_userGroup);
                    lo_results.Add(new KeyValuePair<string, bool>(lo_chaldatalist[i].name, lb_res));
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
    }

    //public void AddData(string ls_authtoken)
    //{
    //    ConsumerEvent lo_event = new ConsumerEvent();
    //    try
    //    {
    //        GoAberWS.AuthHeader lo_authentication = new GoAberWS.AuthHeader();
    //        lo_authentication.authtoken = ls_authtoken;
    //        GoAberWS.ActivityData[] lo_data = new GoAberWS.ActivityData[1];
    //        lo_data[0] = new GoAberWS.ActivityData();
    //        lo_data[0].value = 4;
    //        lo_data[0].date = DateTime.Now;
    //        lo_data[0].categoryUnitId = 1;

    //        GoAberWS.GoAberWSSoapClient lo_service = new GoAberWS.GoAberWSSoapClient();
    //        bool lb_res = lo_service.AddActivityData(lo_authentication, lo_data);
    //        lo_event.result = lb_res;
    //    }
    //    catch (Exception e)
    //    {
    //        lo_event.result = false;
    //    }
    //    ConsumerUpdate(this, lo_event);
    //}
}