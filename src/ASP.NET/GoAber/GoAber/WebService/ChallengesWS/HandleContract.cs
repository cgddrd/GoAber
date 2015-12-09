using GoAber.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GoAber.WebService.ChallengesWS
{
    public class HandleContract
    {
        private static ApplicationDbContext db = new ApplicationDbContext();
        public static CommunityData RecieveContract(CommunityData ao_comdata)
        {
            string ls_authtoken = Guid.NewGuid().ToString();
            Community ao_com = new Community();
            ao_com.name = ao_comdata.name;
            ao_com.home = false;
            ao_com.domain = ao_comdata.domain;
            ao_com.challengesEndpoint = ao_comdata.challengesEndpoint;
            ao_com.authtoken = ls_authtoken;

            db.Communities.Add(ao_com);
            db.SaveChanges();

            //Find home community to send back information.
            IQueryable<Community> lo_homecomquery = from coms in db.Communities
                                                    where coms.home == true
                                                    select coms;

            Community lo_homecom = lo_homecomquery.First();

            CommunityData ao_responsedata = new CommunityData();
            ao_responsedata.authtoken = ls_authtoken;
            ao_responsedata.domain = lo_homecom.domain;
            ao_responsedata.challengesEndpoint = lo_homecom.challengesEndpoint;
            ao_responsedata.name = lo_homecom.name;

            return ao_responsedata;
        }
    }
}