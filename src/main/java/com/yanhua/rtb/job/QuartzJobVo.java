/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: QuartzJobVo
 * Author: Emiya
 * Date: 2020/10/21 18:32
 * Description: 定时任务对象视图类
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.job;

import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.util.ClassUtils;

import java.util.Date;

/**
 * 〈功能简述〉<br>
 * 〈定时任务对象视图类〉
 *  <p>
 * @author Emiya
 * @create 2020/10/21 18:32
 * @version 1.0.0
 */
@Data
public class QuartzJobVo {

    /**
     * 触发器开始时间
     */
    private Date startTime;
    /**
     * 触发器结束时间
     */
    private Date endTime;
    /**
     * job名称
     */
    private String jobName;
    /**
     * job组名
     */
    private String jobGroupName;
    /**
     * 定时器名称
     */
    private String triggerName;
    /**
     * 定时器组名
     */
    private String triggerGroupName;
    /**
     * 执行定时任务的具体操作
     */
    private Class<?> jobClass;
    /**
     * cron表达式
     */
    private String cron;
    /**
     * job的附加信息
     */
    private JobDataMap jobDataMap = new JobDataMap();

    /**
     * 校验
     * @return
     */
    public boolean verify(){
        return !(StringUtils.isEmpty(jobName)
                || StringUtils.isEmpty(jobGroupName)
                || StringUtils.isEmpty(triggerName)
                || StringUtils.isEmpty(triggerGroupName)
                || StringUtils.isEmpty(cron)
//        || CollectionUtils.isEmpty(jobDataMap)
                || ObjectUtils.isEmpty(startTime)
                || ObjectUtils.isEmpty(endTime)
                || !ClassUtils.hasMethod(Job.class, "execute", JobExecutionContext.class)
        );
    }
}