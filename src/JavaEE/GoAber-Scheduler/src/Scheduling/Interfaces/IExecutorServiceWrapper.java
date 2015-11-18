/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduling.Interfaces;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Dan
 */
public interface IExecutorServiceWrapper {
    public ScheduledFuture scheduleAtFixedRate(Runnable command, long initialDelay, long delay, TimeUnit unit);
    public ScheduledFuture scheduleOnce(Runnable command, long delay, TimeUnit unit);
}
