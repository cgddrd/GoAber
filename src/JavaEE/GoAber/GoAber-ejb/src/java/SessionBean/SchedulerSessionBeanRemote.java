/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package SessionBean;

import DTO.IJobDetail;
import javax.ejb.Remote;

/**
 *
 * @author Dan
 */
@Remote
public interface SchedulerSessionBeanRemote {

    boolean AddJob(DTO.IJobDetail ao_job);

    boolean RemoveJob(IJobDetail ao_job);

    Boolean EditJob(DTO.IJobDetail ao_job);

    String TestRemoteMethod();
    
}
