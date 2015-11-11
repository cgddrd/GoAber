using GoAber.Scheduling.Interfaces;
using Hangfire;
using Owin;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Web;

namespace GoAber.Scheduling.Hangfire
{
    public class HangfireScheduler : IScheduler
    {


        public void CreateRecurringJob(string as_id, Expression<Action> am_methodcall, Func<string> ao_cronexp)
        {
            RecurringJob.AddOrUpdate(as_id, am_methodcall, ao_cronexp);
        }

        public void CreateRecurringJob(string as_id, Expression<Action> am_methodcall, string as_cronexp)
        {
            RecurringJob.AddOrUpdate(as_id, am_methodcall, as_cronexp);
        }

        public void EditRecurringJob(string as_id, Expression<Action> am_methodcall, Func<string> ao_cronexp)
        {
            CreateRecurringJob(as_id, am_methodcall, ao_cronexp);
        }

        public void EditRecurringJob(string as_id, Expression<Action> am_methodcall, string as_cronexp)
        {
            CreateRecurringJob(as_id, am_methodcall, as_cronexp);
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

        public void RemoveJob(string as_id)
        {
            RecurringJob.RemoveIfExists(as_id);
        }
    }
}