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


   public int getTasktype();
   public int getSchedtype();


   public int getShcedtimemins();


   public Boolean getStartnow();

}
