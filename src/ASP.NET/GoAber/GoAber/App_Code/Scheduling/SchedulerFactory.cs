using GoAber.Scheduling.Hangfire;
using GoAber.Scheduling.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace GoAber.Scheduling
{
    public class SchedulerFactory
    {

        private static SchedulerFactory io_schedfactory;
        private IScheduler io_scheduler;

        protected SchedulerFactory()
        {
            if (io_scheduler == null)
            {
                io_scheduler = new HangfireScheduler();
            }
        }

        public static SchedulerFactory Instance()
        {
            if (io_schedfactory == null)
            {
                io_schedfactory = new SchedulerFactory();
            }
            return io_schedfactory;
        }

        public IScheduler GetScheduler()
        {
            return io_scheduler;
        }
    }
}