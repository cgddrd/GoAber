/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

/**
 *
 * @author Dan
 */
public interface IJobDetail {
    
   public String getJobid();
   public String getTasktype();
   public String getSchedtype();
   public int getShcedtimemins();
   public Boolean getStartnow();
   public String[] scheduler_args();

}
