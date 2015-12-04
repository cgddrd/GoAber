using GoAber.Models;
using GoAber.Services;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GoAber.WebService.ChallengesWS
{
    public class HandleResult
    {
        private static ApplicationDbContext db = new ApplicationDbContext();

        public static ResultData RecieveResult(ResultData result)
        {
            try
            {
                int li_catunitid = result.categoryUnitId;
                IQueryable<CategoryUnit> lo_catunitquery = from c in db.CategoryUnits
                                                           where c.Id == li_catunitid
                                                           select c;
                CategoryUnit lo_catunit = lo_catunitquery.First();

                string ls_challengeId = result.challengeId;
                IQueryable<Challenge> lo_chalquery = from c in db.Challenges
                                                     where c.Id == ls_challengeId
                                                     select c;
                Challenge lo_challenge = lo_chalquery.First();

                string ls_authtoken = result.authtoken;
                IQueryable<Community> lo_comquery = from c in db.Communities
                                                    where c.authtoken == ls_authtoken
                                                    select c;
                Community lo_community = lo_comquery.First();


                //Record recieved result.
                Result lo_resmodel = new Result();
                lo_resmodel.categoryUnitId = result.categoryUnitId;
                lo_resmodel.categoryUnit = lo_catunit;
                lo_resmodel.challengeId = result.challengeId;
                lo_resmodel.challenge = lo_challenge;
                lo_resmodel.communityId = lo_community.Id;
                lo_resmodel.community = lo_community;
                lo_resmodel.value = result.value;

                db.Results.Add(lo_resmodel);

                //Generate response result.
                Result lo_respres = ResultService.CreateResult(result.challengeId, db);

                IQueryable<Community> lo_homecomquery = from c in db.Communities
                                                        where c.home == true
                                                        select c;

                ResultData lo_respresdata = new ResultData();
                lo_respresdata.categoryUnitId = lo_respres.categoryUnitId;
                lo_respresdata.challengeId = lo_respres.challengeId;

                lo_respresdata.authtoken = result.authtoken;

                lo_respresdata.value = lo_respres.value.Value;

                db.Results.Add(lo_respres);

                //Save result data.
                db.SaveChanges();

                return lo_respresdata;
            }
            catch (Exception ex)
            {
                return null;
            }
        }
    }
}