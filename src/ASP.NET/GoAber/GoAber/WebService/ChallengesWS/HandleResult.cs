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
                //Record recieved result.
                Result lo_resmodel = new Result();
                lo_resmodel.categoryUnitId = result.categoryUnitId;
                lo_resmodel.challengeId = result.challengeId;
                lo_resmodel.communityId = result.communityId;
                lo_resmodel.value = result.value;

                db.Results.Add(lo_resmodel);

                //Generate response result.
                Result lo_respres = ResultService.CreateResult(result.challengeId, db);

                ResultData lo_respresdata = new ResultData();
                lo_respresdata.categoryUnitId = lo_respres.categoryUnitId;
                lo_respresdata.challengeId = lo_respresdata.challengeId;

                IQueryable<Community> lo_homecomquery = from c in db.Communities
                                                        where c.home == true
                                                        select c;

                lo_respresdata.communityId = lo_homecomquery.First().Id;

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