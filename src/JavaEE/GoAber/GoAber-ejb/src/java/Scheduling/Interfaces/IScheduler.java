/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduling.Interfaces;

import Scheduling.Jobs.AbstractJob;
import java.util.Date;

/**
 *
 * @author Dan
 */
public interface IScheduler {
       void Init();

        void CreateRecurringJob(AbstractJob ao_job);

        void RemoveJob(String as_id);

        void EditRecurringJob(AbstractJob ao_job);

}
