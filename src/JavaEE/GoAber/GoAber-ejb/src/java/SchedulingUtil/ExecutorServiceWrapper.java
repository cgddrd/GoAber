package SchedulingUtil;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//From GoAber-Scheduler project.
import Scheduling.Interfaces.IExecutorServiceWrapper;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javax.enterprise.concurrent.ManagedScheduledExecutorService;

/**
 *
 * @author Dan
 */
public class ExecutorServiceWrapper implements IExecutorServiceWrapper {
    
    private final ManagedScheduledExecutorService io_executor;
    
    public ExecutorServiceWrapper(ManagedScheduledExecutorService ao_executor) {
        this.io_executor = ao_executor;
    }

    /***
     * Schedules a recurring job.
     * @param command
     * @param initialDelay
     * @param delay
     * @param unit
     * @return 
     */
    @Override
    public ScheduledFuture scheduleAtFixedRate(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return this.io_executor.scheduleAtFixedRate(command, initialDelay, delay, unit);
    }
    /***
     * Schedules a job to run once.
     * @param command
     * @param delay
     * @param unit
     * @return 
     */
    @Override
    public ScheduledFuture scheduleOnce(Runnable command, long delay, TimeUnit unit) {
        return this.io_executor.schedule(command, delay, unit);
    }

    
}
