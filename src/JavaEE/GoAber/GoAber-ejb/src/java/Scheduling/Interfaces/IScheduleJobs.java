/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduling.Interfaces;

import GoAberDatabase.Jobdetail;


/**
 *
 * @author Dan
 */
public interface IScheduleJobs {
    public boolean AddJob(Jobdetail ao_job);
    public boolean RemoveJob(String as_id);
    public boolean EditJob(Jobdetail ao_job);
}
