/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Jobs;

import Scheduling.Jobs.AbstractJob;
import DTO.IJobDetail;

/**
 *
 * @author Dan
 */
public class JobFactory {
    
    /***
     * Creates a specified type of job.
     * @param ao_jobdetail
     * @return 
     */
    public static AbstractJob CreateJob(IJobDetail ao_jobdetail) {
        switch (ao_jobdetail.getTasktype()) {
            case "Jawbone":
                return new JawBoneJob(ao_jobdetail);
            case "Fitbit":
                return new FitBitJob(ao_jobdetail);    
            case "Challenge":
                return new ChallengeJob(ao_jobdetail);
            default:
                break;
        }
        return null;
    }
}
