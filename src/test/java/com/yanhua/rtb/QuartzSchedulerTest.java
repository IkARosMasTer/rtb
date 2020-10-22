///**
// * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
// * FileName: QuartzSchedulerTest
// * Author: Emiya
// * Date: 2020/10/21 15:49
// * Description:
// * History:
// * <author> <time> <version> <desc>
// * 作者姓名 修改时间 版本号 描述
// */
//package com.yanhua.rtb;
//
//import com.yanhua.rtb.job.CnMobileFullJob;
//import com.yanhua.rtb.job.CnMobileIncJob;
//import org.junit.jupiter.api.Test;
//import org.quartz.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
///**
// * 〈功能简述〉<br>
// * 〈〉
// *  <p>
// * @author Emiya
// * @create 2020/10/21 15:49
// * @version 1.0.0
// */
////@RunWith(SpringRunner.class)
//@SpringBootTest(classes = RtbApplication.class)
//public class QuartzSchedulerTest {
//
//    @Autowired
//    private Scheduler scheduler;
//
//    @Test
//    public void addDemoJob01Config() throws SchedulerException {
//        // 创建 JobDetail
//        JobDetail jobDetail = JobBuilder.newJob(CnMobileFullJob.class)
//                .withIdentity("demoJob01") // 名字为 demoJob01
//                .storeDurably() // 没有 Trigger 关联的时候任务是否被保留。因为创建 JobDetail 时，还没 Trigger 指向它，所以需要设置为 true ，表示保留。
//                .build();
//        // 创建 Trigger
//        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
//                .withIntervalInSeconds(5) // 频率。
//                .repeatForever(); // 次数。
//        Trigger trigger = TriggerBuilder.newTrigger()
//                .forJob(jobDetail) // 对应 Job 为 demoJob01
//                .withIdentity("demoJob01Trigger") // 名字为 demoJob01Trigger
//                .withSchedule(scheduleBuilder) // 对应 Schedule 为 scheduleBuilder
//                .build();
//        // 添加调度任务
//        scheduler.scheduleJob(jobDetail, trigger);
//    }
//
//    @Test
//    public void addDemoJob02Config() throws SchedulerException {
//        // 创建 JobDetail
//        JobDetail jobDetail = JobBuilder.newJob(CnMobileIncJob.class)
//                .withIdentity("demoJob02") // 名字为 demoJob02
//                .storeDurably() // 没有 Trigger 关联的时候任务是否被保留。因为创建 JobDetail 时，还没 Trigger 指向它，所以需要设置为 true ，表示保留。
//                .build();
//        // 创建 Trigger
//        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule("0/10 * * * * ? *");
//        Trigger trigger = TriggerBuilder.newTrigger()
//                .forJob(jobDetail) // 对应 Job 为 demoJob01
//                .withIdentity("demoJob02Trigger") // 名字为 demoJob01Trigger
//                .withSchedule(scheduleBuilder) // 对应 Schedule 为 scheduleBuilder
//                .build();
//        // 添加调度任务
//        scheduler.scheduleJob(jobDetail, trigger);
////        scheduler.scheduleJob(jobDetail, Sets.newSet(trigger), true);
//    }
//}