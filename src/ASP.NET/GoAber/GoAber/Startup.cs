using System.Diagnostics;
using Hangfire;
using Microsoft.Owin;
using Owin;
using GoAber.Scheduling;
using GoAber.Scheduling.Hangfire;
//using GoAber.Scheduling;

[assembly: OwinStartupAttribute(typeof(GoAber.Startup))]
[assembly: log4net.Config.XmlConfigurator(ConfigFile = "Web.Config", Watch = true)]

namespace GoAber
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            SchedulerFactory.Instance().GetScheduler().Init(app, "Hangfire");

            ConfigureAuth(app);
        }
    }
} 
