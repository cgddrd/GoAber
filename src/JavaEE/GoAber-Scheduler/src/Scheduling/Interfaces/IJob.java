/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Scheduling.Interfaces;

import java.util.Date;

/**
 *
 * @author Dan
 */
public interface IJob {
    Integer getJobType();
    void run();
}
