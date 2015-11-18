/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduling.Jobs;

import DTO.IJobDetail;
import Scheduling.Interfaces.IJob;
import java.util.Date;

/**
 *
 * @author Dan
 */
public abstract class AbstractJob implements IJob, Runnable{
    private final IJobDetail io_jobdetails;
    public AbstractJob(IJobDetail ao_jobdetails) {
        this.io_jobdetails = ao_jobdetails;
    }
    
    public IJobDetail getJobDetails() {
        return this.io_jobdetails;
    }
    
    public long getDateInSeconds() {
        return (this.io_jobdetails.getShcedtimemins()*60);
    }
    
    @Override
    public Integer getJobType() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
