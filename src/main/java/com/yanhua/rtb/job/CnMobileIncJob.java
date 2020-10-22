///**
// * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
// * FileName: CnMobileIncJob
// * Author: Emiya
// * Date: 2020/10/21 11:14
// * Description: 中国移动增量
// * History:
// * <author> <time> <version> <desc>
// * 作者姓名 修改时间 版本号 描述
// */
//package com.yanhua.rtb.job;
//
//import lombok.extern.slf4j.Slf4j;
//import org.quartz.DisallowConcurrentExecution;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.quartz.PersistJobDataAfterExecution;
//import org.springframework.scheduling.quartz.QuartzJobBean;
//import org.springframework.stereotype.Component;
//
///**
// * 〈功能简述〉<br>
// * 〈中国移动增量彩铃文件定时获取任务〉
// *  <p>
// * @author Emiya
// * @create 2020/10/21 11:14
// * @version 1.0.0
// */
//@Slf4j
//@Component
//@DisallowConcurrentExecution
//@PersistJobDataAfterExecution //无状态的Job可以并发执行(各自拥有jobDataMap)，而有状态的StatefulJob不能并发执行(共享jobDataMap)
//public class CnMobileIncJob extends QuartzJobBean {
//
//    @Override
//    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
//        log.info("[executeInternal][我开始的执行了]");
//    }
//
//}