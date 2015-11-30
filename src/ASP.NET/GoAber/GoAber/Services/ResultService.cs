using GoAber.Models;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Web;

namespace GoAber.Services
{
    public class ResultService
    {

        public static Result CreateResult(string as_challengeid, ApplicationDbContext db, Challenge ao_challenge = null)
        {


            if (ao_challenge == null)
            {
                IQueryable<Challenge> lo_query = from c in db.Challenges
                                                 where c.Id == as_challengeid
                                                 select c;
                ao_challenge = lo_query.First();
            }

            ao_challenge.complete = true;
            db.Entry(ao_challenge).State = EntityState.Modified;
            db.SaveChanges();

            IQueryable<int?> lo_query_result = from a in db.ActivityDatas
                                               where
                                               (a.date > ao_challenge.startTime && a.date < ao_challenge.endTime)
                                               && (a.categoryUnitId == ao_challenge.categoryUnitId)
                                               select a.value;

            int? li_result = lo_query_result.ToArray().Sum();


            int li_catunitid = ao_challenge.categoryUnitId;
            IQueryable<CategoryUnit> lo_catunitquery = from c in db.CategoryUnits
                                                       where c.Id == li_catunitid
                                                       select c;
            CategoryUnit lo_catunit = lo_catunitquery.First();


            IQueryable<Community> lo_comquery = from c in db.Communities
                                                where c.home == true
                                                select c;
            Community lo_community = lo_comquery.First();


            Result lo_result = new Result();
            lo_result.challenge = ao_challenge;
            lo_result.challengeId = ao_challenge.Id;
            lo_result.community = lo_community;
            lo_result.communityId = lo_community.Id;
            lo_result.categoryUnit = lo_catunit;
            lo_result.categoryUnitId = lo_catunit.Id;
            lo_result.value = li_result;
            return lo_result;
        }
    }
}