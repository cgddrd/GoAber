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
            return BackgroundJob.Schedule(am_methodcall, TimeSpan.FromMinutes(ai_minutes));
        }

        public string CreateOnceJob(Expression<Action> am_methodcall, DateTimeOffset adao_date)
        {
            return BackgroundJob.Schedule(am_methodcall, adao_date);
        }


        public void CreateRecurringJob(string as_id, Expression<Action> am_methodcall, int ai_minutes)
        {
            TimeSpan time = new TimeSpan(0, 0, ai_minutes, 0, 0);
            int days = time.Days % 12;
            int month = time.Days / 12;
            string ls_cronexp = String.Format(
                "*/{0} */{1} */{2} */{3} */{4}",
                time.Minutes.ToString(),
                time.Hours.ToString(),
                days.ToString(),
                month.ToString(),
                "1");
            RecurringJob.AddOrUpdate(as_id, am_methodcall, Cron.Minutely);
        }


        public void EditRecurringJob(string as_id, Expression<Action> am_methodcall, int ai_minutes)
        {
            CreateRecurringJob(as_id, am_methodcall, ai_minutes);
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