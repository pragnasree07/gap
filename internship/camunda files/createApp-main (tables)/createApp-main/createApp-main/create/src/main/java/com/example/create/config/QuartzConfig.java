//package com.example.create.config;
//import com.example.create.scheduler.OrderRetryJob;
//import org.quartz.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class QuartzConfig {
//    @Bean
//    public JobDetail orderRetryJobDetail() {
//        return JobBuilder.newJob(OrderRetryJob.class)
//                .withIdentity("orderRetryJob")
//                .storeDurably()
//                .build();
//    }
//
//    @Bean
//    public Trigger orderRetryTrigger() {
//        return TriggerBuilder.newTrigger()
//                .forJob(orderRetryJobDetail())
//                .withIdentity("orderRetryTrigger")
//                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
//                        .withIntervalInSeconds(30)
//                        .repeatForever())
//                .build();
//    }
//}
//
