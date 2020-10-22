///**
// * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
// * FileName: ScheduleConfiguration
// * Author: Emiya
// * Date: 2020/10/21 10:45
// * Description: 调度器配置类
// * History:
// * <author> <time> <version> <desc>
// * 作者姓名 修改时间 版本号 描述
// */
//package com.yanhua.rtb.config;
//
//import com.yanhua.rtb.job.CnMobileFullJob;
//import com.yanhua.rtb.job.CnMobileIncJob;
//import org.quartz.*;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//
///**
// * 〈功能简述〉<br>
// * 〈调度器配置类〉
// *  <p>
// * @author Emiya
// * @create 2020/10/21 10:45
// * @version 1.0.0
// */
//@Configuration
////@EnableScheduling
//public class ScheduleConfiguration {
//
//    public static class CnMobileFullJobConfiguration {
//
//        @Bean
//        public JobDetail fullJob() {
//            return JobBuilder.newJob(CnMobileFullJob.class)
//                    .withIdentity("CnMobileFullJob","CnMobileJob") // 名字为 demoJob01
//                    .storeDurably() // 没有 Trigger 关联的时候任务是否被保留。因为创建 JobDetail 时，还没 Trigger 指向它，所以需要设置为 true ，表示保留。
//                    .build();
//        }
//
//        @Bean
//        public Trigger fullJobTrigger() {
//            // 简单的调度计划的构造器
//            SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withMisfireHandlingInstructionIgnoreMisfires()
//                    .withIntervalInSeconds(5) // 频率。
//                    .repeatForever(); // 次数。
//            // Trigger 构造器
//            return TriggerBuilder.newTrigger()
//                    .forJob(fullJob()) // 对应 Job 为 demoJob01
//                    .withIdentity("CnMobileFullJobTrigger","CnMobileTrigger") // 名字为 demoJob01Trigger
//                    .withSchedule(scheduleBuilder) // 对应 Schedule 为 scheduleBuilder
//                    .build();
//        }
//
//    }
//
//    public static class CnMobileIncJobConfiguration {
//
//        @Bean
//        public JobDetail incJob() {
//            return JobBuilder.newJob(CnMobileIncJob.class)
//                    .withIdentity("CnMobileIncJob","CnMobileJob") // 名字为 demoJob02
//                    .storeDurably() // 没有 Trigger 关联的时候任务是否被保留。因为创建 JobDetail 时，还没 Trigger 指向它，所以需要设置为 true ，表示保留。
//                    .build();
//        }
//
//        @Bean
//        public Trigger incJobTrigger() {
//            // 简单的调度计划的构造器
//            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ? *").withMisfireHandlingInstructionIgnoreMisfires();
////            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0 0 2 * * ? *");
//            // Trigger 构造器
//            return TriggerBuilder.newTrigger()
//                    .forJob(incJob()) // 对应 Job 为 demoJob02
//                    .withIdentity("CnMobileIncJobTrigger","CnMobileTrigger") // 名字为 demoJob02Trigger
//                    .withSchedule(scheduleBuilder) // 对应 Schedule 为 scheduleBuilder
//                    .build();
//        }
//
//    }
//}