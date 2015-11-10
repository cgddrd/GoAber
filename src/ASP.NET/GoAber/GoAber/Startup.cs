using System.Diagnostics;
using Hangfire;
using Microsoft.Owin;
using Owin;
using GoAber.Scheduling;
using GoAber.Scheduling.Hangfire;
//using GoAber.Scheduling;

[assembly: OwinStartupAttribute(typeof(GoAber.Startup))]

namespace GoAber
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            IScheduling lo_scheduler = HangfireScheduler.Instance();
            lo_scheduler.Init(app, "Hangfire");

            ScheduleTasks.Execute(lo_scheduler);
            ConfigureAuth(app);
        }
    }
} 
