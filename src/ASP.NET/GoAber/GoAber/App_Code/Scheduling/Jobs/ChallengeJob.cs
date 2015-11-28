using GoAber.Models;
using GoAber.Scheduling.Interfaces;
using GoAber.Services;
using GoAber.WebService.ChallengesWS;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Diagnostics;
using System.Linq;
using System.Web;

namespace GoAber.App_Code.Scheduling.Jobs
{
    public class ChallengeJob : IJob
    {
        ApplicationDbContext db = new ApplicationDbContext();
        public string GetID()
        {
            return "challengejob";
        }



        public void Run(string[] args)
        {
            try {
                Debug.WriteLine("Challenge complete");
                string ls_challengeid = args[0];

                IQueryable<Challenge> lo_query = from c in db.Challenges
                                                 where c.Id == ls_challengeid
                                                 select c;
                Challenge lo_challenge = lo_query.First();



                IQueryable<Community> lo_homecomquery = from c in db.Communities
                                                        where c.home == true
                                                        select c;

                Result lo_result = ResultService.CreateResult(ls_challengeid, db, lo_challenge);
                db.Results.Add(lo_result);


                Community lo_homecom = lo_homecomquery.First();
                lo_result.community = lo_homecom;

                GoAberChallengeWSConsumer lo_consumer = null;
                foreach (CommunityChallenge lo_com in lo_challenge.communityChallenges)
                {
                    if (lo_com.community.Id == lo_homecom.Id) continue;
                    if (lo_consumer == null)
                    {
                        lo_consumer = GoAberChallengeWSConsumer.GetInstance();
                    }
                    try
                    {
                        Result lo_foreignresult = lo_consumer.SendResult(lo_result, lo_com.community);
                        db.Results.Add(lo_foreignresult);
                    }
                    catch (Exception ex)
                    {
                        Debug.Write(ex.StackTrace);
                    }
                }

                db.SaveChanges();
            } catch (Exception ex)
            {
                Debug.Write(ex.StackTrace);
            }                         
        }
    }
}