///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package Startups;
//
//import Scheduling.SchedulerFactory;
//import SessionBean.JobDetailFacade;
//import javax.annotation.PostConstruct;
//import javax.ejb.Asynchronous;
//import javax.ejb.EJB;
//import javax.ejb.Singleton;
//import javax.ejb.Startup;
//import javax.ejb.Stateless;
//
///**
// *
// * @author Dan
// */
//@Singleton
//@Startup
//public class SchedulerMain {
//    
//    @Stateless
//    public static class MainClass {     
//        @Asynchronous
//        public void Main(Object[] args) {
//            //Calls scheduler init method.
//            SchedulerFactory.Instance(args);
//        }  
//    }
//    
//    @EJB
//    private MainClass io_mainclass;
//    
//    @EJB
//    private JobDetailFacade io_jobdetail;
//    
//    @PostConstruct
//    private void initiateMain() {
//        io_mainclass.Main(new Object[] {io_jobdetail.findAll()});
//        
//    }
//}
