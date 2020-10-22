///**
// * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
// * FileName: CnMobileFullJob
// * Author: Emiya
// * Date: 2020/10/21 11:01
// * Description: 中国移动全量彩铃文件定时获取任务
// * History:
// * <author> <time> <version> <desc>
// * 作者姓名 修改时间 版本号 描述
// */
//package com.yanhua.rtb.job;
//
//import com.yanhua.rtb.service.ICnMobileService;
//import lombok.extern.slf4j.Slf4j;
//import org.quartz.DisallowConcurrentExecution;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.quartz.PersistJobDataAfterExecution;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.quartz.QuartzJobBean;
//import org.springframework.stereotype.Component;
//
//import java.util.concurrent.atomic.AtomicInteger;
//
///**
// * 〈功能简述〉<br>
// * 〈中国移动全量彩铃文件定时获取任务〉
// *  <p>
// * @author Emiya
// * @create 2020/10/21 11:01
// * @version 1.0.0
// */
//@Slf4j
//@Component
//@DisallowConcurrentExecution
//@PersistJobDataAfterExecution
//public class CnMobileFullJob extends QuartzJobBean {
//
//
//    private final AtomicInteger counts = new AtomicInteger();
//
//    @Autowired
//    private ICnMobileService iCnMobileService;
//
//    @Override
//    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        log.info("[executeInternal][定时第 ({}) 次执行, iCnMobileService 为 ({})]", counts.incrementAndGet(),
//                iCnMobileService);
//    }
//
//
//}