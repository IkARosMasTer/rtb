/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: WarehouingController
 * Author: Emiya
 * Date: 2020/10/13 15:23
 * Description: 入库控制器
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yanhua.rtb.common.EngineException;
import com.yanhua.rtb.common.ResponseResult;
import com.yanhua.rtb.service.WarehousingService;
import com.yanhua.rtb.util.ExcelUtils;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;


/**
 * 〈功能简述〉<br>
 * 〈入库控制器〉
 *  <p>
 * @author Emiya
 * @create 2020/10/13 15:23
 * @version 1.0.0
 */
@RestController
@RequestMapping("/Warehousing")
@Api(tags = "入库控制器")
@ResponseResult
@Slf4j
public class WarehousingController {

    @Autowired
    private WarehousingService warehousingService;

    @ApiOperation(value = "联通excel上传接口",httpMethod = "POST",notes = "excel字段名为copyrightId")
    @PostMapping(value="/upload/unicom",headers="content-type=multipart/form-data")
    @ResponseBody
    public WebAsyncTask<List<String>> upload(
            @ApiParam(value = "上传的文件", required = true)
            @RequestParam("excelFile") MultipartFile excelFile) {
        log.info(Thread.currentThread().getName() + " 进入控制器方法了");
        String name = excelFile.getOriginalFilename();
        List<String> copyrightIds;
        if (StringUtils.isNotEmpty(name)){
            String suffix = name.substring(name.lastIndexOf(".")).toLowerCase();
            if (".xls".equals(suffix)||".xlsx".equals(suffix)){
                try{
                    copyrightIds = ExcelUtils.getCopyrightIds(excelFile.getInputStream());
                }catch (Exception e){
                    e.printStackTrace();
                    log.error("联通excel上传接口=============>IO流操作异常");
                    throw new EngineException("IO流操作异常");
                }
            }else {
                throw new EngineException(1003,"excel文件格式错误");
            }
        }else {
            throw new EngineException(1004,"excel文件名缺失");
        }
        //webAsyncTask构造方法里可以直接传超时期限，否则就看统一的异步请求配置里的超时时间
        WebAsyncTask<List<String>> webAsyncTask = new WebAsyncTask<>( () -> {
            log.info(Thread.currentThread().getName() + " 进入call方法");
            List<String> strings = warehousingService.getCnUnicom(copyrightIds);
            log.info(Thread.currentThread().getName() + " 从warehousingService方法返回");
            return strings;
        });
        webAsyncTask.onCompletion(() -> log.info(Thread.currentThread().getName() + " 执行完毕"));
        webAsyncTask.onTimeout(() -> {
//            log.info(Thread.currentThread().getName() + " onTimeout");
            // 超时的时候，直接抛异常，让外层统一处理超时异常
            throw new TimeoutException("后台处理中,请稍后查看");
        });
        webAsyncTask.onError(() -> {
//            log.error(Thread.currentThread().getName() + "异步处理异常");
            throw new EngineException("异步处理异常");
        });
//        log.info(Thread.currentThread().getName() + " 从控制器方法返回");
        return webAsyncTask;
    }

}