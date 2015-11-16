using GoAber.Scheduling.Interfaces;
using Hangfire;
using Owin;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Linq.Expressions;
using System.Web;

namespace GoAber.Scheduling.Hangfire
{
    public class HangfireScheduler : IScheduler
    {
        public string CreateOnceJob(Expression<Action> am_methodcall, int ai_minutes)
        {
            TimeSpan time = new TimeSpan(0,0,ai_minutes,0,0);
            return BackgroundJob.Schedule(am_methodcall, time);
        }

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

        public void CreateHangfireDB(HangfireContext context)
        {
            Dummy lo_dummy = new Dummy { DummyId = 0 };

            context.Dummy.Add(lo_dummy);
            context.SaveChanges();
        }

        public void Init(IAppBuilder ao_app, string as_constring)
        {
            using (HangfireContext context = new HangfireContext())
            {
                if (context.Dummy.Count() <= 0)
                {
                    CreateHangfireDB(context);
                }
            }
            GlobalConfiguration.Configuration
            .UseSqlServerStorage("Hangfire");

            ao_app.UseHangfireDashboard();
            ao_app.UseHangfireServer();
        }

        public void RemoveRecurringJob(string as_id)
        {
            RecurringJob.RemoveIfExists(as_id);
        }

        public void RemoveOnceJob(string as_id)
        {
            throw new NotImplementedException();
        }

        public void EditOnceJob(string as_id, Expression<Action> am_methodcall, Func<string> ao_cronexp)
        {
            throw new NotImplementedException();
        }
    }
}