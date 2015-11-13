/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduling;

import Scheduling.Interfaces.IScheduler;

/**
 *
 * @author Dan
 */
public class SchedulerFactory {
        private static SchedulerFactory io_schedfactory;
        private IScheduler io_scheduler;

        protected SchedulerFactory()
        {
            if (io_scheduler == null)
            {
                //io_scheduler = new HangfireScheduler();
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
