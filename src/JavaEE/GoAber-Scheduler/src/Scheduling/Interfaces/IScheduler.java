/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduling.Interfaces;

import DTO.IJobDetail;
import Scheduling.Jobs.AbstractJob;

/**
 *
 * @author Dan
 */
public interface IScheduler {
       void Init(Object[] args);

        void CreateRecurringJob(AbstractJob ao_job);

        void RemoveJob(IJobDetail ao_jobdetail);

        void EditRecurringJob(AbstractJob ao_job);

}
