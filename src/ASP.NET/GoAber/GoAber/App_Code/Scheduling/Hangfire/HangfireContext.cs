using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Data.Entity;

namespace GoAber.Scheduling.Hangfire
{
    public class HangfireContext : DbContext
    {
        public DbSet<Dummy> Dummy { get; set; }
        public HangfireContext() :base("Hangfire")
        {
            Database.SetInitializer(new CreateDatabaseIfNotExists<HangfireContext>());
        }
    }

    public class Dummy
    {
        public int DummyId { get; set; }
    }
}