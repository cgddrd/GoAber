/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduling.Jobs;

import DTO.IJobDetail;


/**
 *
 * @author Dan
 */
public class FitBitJob extends AbstractJob {

    public FitBitJob(IJobDetail ao_jobdetails) {
        super(ao_jobdetails);
    }

    @Override
    public String getID() {
       return getJobDetails().getJobid();
    }

    @Override
    public void run() {
        System.out.println("I HAVE BEEN CALLED! " + getJobDetails().getJobid());
    }



    
}
