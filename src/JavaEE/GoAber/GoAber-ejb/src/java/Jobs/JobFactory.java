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
    
    public static AbstractJob CreateJob(IJobDetail ao_jobdetail) {
        switch (ao_jobdetail.getTasktype()) {
            case 0:
                return new FitBitJob(ao_jobdetail);
        //
            case 1:
                break;
        //
            default:
                break;
        }
        return null;
    }
}
