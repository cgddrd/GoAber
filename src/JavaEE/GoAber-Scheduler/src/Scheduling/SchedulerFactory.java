/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduling;

import Scheduling.Interfaces.IExecutorServiceWrapper;
import Scheduling.Interfaces.IScheduler;

/**
 *
 * @author Dan
 */
public class SchedulerFactory {
        private static SchedulerFactory io_schedfactory;
        private IScheduler io_scheduler;

        protected SchedulerFactory(Object[] args)
        {
            if (io_scheduler == null)
            {
                io_scheduler = new ManagedScheduler((IExecutorServiceWrapper)args[0]);
                io_scheduler.Init(args);
            }
        }

        public static SchedulerFactory Instance(Object[] args)
        {
            if (io_schedfactory == null)
            {
                    io_schedfactory = new SchedulerFactory(args);
            }
            return io_schedfactory;
        }

        public IScheduler GetScheduler()
        {
            return io_scheduler;
        }
}
