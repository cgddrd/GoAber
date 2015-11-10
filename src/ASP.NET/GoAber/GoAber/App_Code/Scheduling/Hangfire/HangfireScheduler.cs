using Hangfire;
using Owin;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Web;

namespace GoAber.Scheduling.Hangfire
{
    public class HangfireScheduler : IScheduling
    {
        private static HangfireScheduler io_scheduler;

        protected HangfireScheduler()
        {
        }

        public static HangfireScheduler Instance()
        {
            if (io_scheduler == null)
            {
                io_scheduler = new HangfireScheduler();
            }
            return io_scheduler;
        }

        public void CreateRecurringJob(string as_id, Expression<Action> am_methodcall, Func<string> ao_cronexp)
        {
            RecurringJob.AddOrUpdate(as_id, am_methodcall, ao_cronexp);
        }

        public void CreateRecurringJob(string as_id, Expression<Action> am_methodcall, string as_cronexp)
        {
            RecurringJob.AddOrUpdate(as_id, am_methodcall, as_cronexp);
        }

        public void Init(IAppBuilder ao_app)
        {
            throw new NotImplementedException();
        }

        public void Init(IAppBuilder ao_app, string as_constring)
        {
            GlobalConfiguration.Configuration
            .UseSqlServerStorage("Hangfire");

            ao_app.UseHangfireDashboard();
            ao_app.UseHangfireServer();
        }
    }
}