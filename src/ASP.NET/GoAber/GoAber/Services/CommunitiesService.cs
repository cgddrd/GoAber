using GoAber.Models;
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
    }
}