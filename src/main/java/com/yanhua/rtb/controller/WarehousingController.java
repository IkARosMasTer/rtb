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

//import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yanhua.rtb.common.EngineException;
import com.yanhua.rtb.common.GlobalConstant;
import com.yanhua.rtb.common.ResponseResult;
import com.yanhua.rtb.entity.Area;
import com.yanhua.rtb.service.IAreaService;
import com.yanhua.rtb.service.WarehousingService;
import com.yanhua.rtb.service.impl.AreaServiceImpl;
import com.yanhua.rtb.util.ExcelUtils;
import com.yanhua.rtb.util.HttpTools;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.WebAsyncTask;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.lang.Thread.sleep;


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
    @Autowired
    private IAreaService iAreaService;

    @Resource
    @Qualifier("redisTemplateCustomize")
    private RedisTemplate<Object,Object> template;

    @Value("${warehousing.chinaMobile.tokenUrl}")
    private String chinaMobileTokenUrl;

    @ApiOperation(value = "联通excel上传接口",httpMethod = "POST",notes = "excel字段名为copyrightId")
    @RequestMapping(value="/upload/cucc",headers="content-type=multipart/form-data",method = RequestMethod.POST)
    @ResponseBody
    public WebAsyncTask<List<String>> cuccUpload(
            @ApiParam(value = "上传的文件", required = true)
            @RequestParam("excelFile") MultipartFile excelFile) {
        log.info("<---------联通入库线程" + Thread.currentThread().getName() + "进入控制器方法");
        List<String> copyrightIds;
        checkExcel(excelFile);
        try{
            copyrightIds = ExcelUtils.getCuccCopyrightIds(excelFile.getInputStream());
        }catch (Exception e){
            e.printStackTrace();
            log.error("联通excel上传接口=============>IO流操作异常");
            throw new EngineException("IO流操作异常");
        }
        //webAsyncTask构造方法里可以直接传超时期限，否则就看统一的异步请求配置里的超时时间
        WebAsyncTask<List<String>> webAsyncTask = new WebAsyncTask<>( 300L,() -> warehousingService.getCnUnicom(copyrightIds));
        webAsyncTask.onCompletion(() -> log.info("<---------联通入库线程" + Thread.currentThread().getName() + " 执行完毕"));
        webAsyncTask.onTimeout(() -> {
            log.info("<---------联通入库线程" + Thread.currentThread().getName() + " onTimeout");
            // 超时的时候，直接抛异常，让外层统一处理超时异常
            throw new TimeoutException("后台处理中,请稍后查看");
        });
        webAsyncTask.onError(() -> {
            log.error("<---------联通入库线程" + Thread.currentThread().getName() + "异步处理异常");
            throw new EngineException("异步处理异常");
        });
        log.info("<---------联通入库线程" + Thread.currentThread().getName() + " 从控制器方法返回");
        return webAsyncTask;
    }


    @ApiOperation(value = "移动excel上传接口",httpMethod = "POST",notes = "excel格式为移动平台拉取的原始模板")
    @RequestMapping(value="/upload/cmcc",headers="content-type=multipart/form-data",method = RequestMethod.POST)
    @ResponseBody
    public WebAsyncTask<List<String>> cmccUpload(
            @ApiParam(value = "上传的文件", required = true)
            @RequestParam("excelFile") MultipartFile excelFile) {
        checkExcel(excelFile);
        List<Map<String,String>> copyrightIds;
        try{
            copyrightIds = ExcelUtils.getCmccCopyrightIds(excelFile.getInputStream());
        }catch (Exception e){
            e.printStackTrace();
            log.error("移动excel上传接口=============>IO流操作异常");
            throw new EngineException("IO流操作异常");
        }
        //事先redis获取token
//        String token = (String) template.opsForValue().get(GlobalConstant.CTCC_TOKEN_KEY);
        String token = null;
//        if (!StringUtils.isNotEmpty(token)) {
//            String body = "{\n" +
//                    "    \"msisdn\": 15906632054\n" +
//                    "}";
//            //
//            String ret = HttpTools.doPost(chinaMobileTokenUrl, body);
//                String ret = "{\n" +
//                        "    \"code\": 200,\n" +UserMapper
//                        "    \"data\": {\n" +
//                        "        \"resCode\": \"000000\",\n" +
//                        "        \"msisdn\": \"182*****769\",\n" +
//                        "        \"resMsg\": \"【OPEN】操作成功\",\n" +
//                        "        \"token\": \"fdfefe038ee74473a7ef4008a308fd33\"\n" +
//                        "    },\n" +
//                        "    \"mes\": \"ok\"\n" +
//                        "}";
//            if (StringUtils.isNotEmpty(ret)) {
//                JSONObject jsonObject = JSON.parseObject(ret);
//                if (jsonObject.containsKey("code") && ((Integer) jsonObject.get("code") == 200)) {
//                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
//                    if (jsonObject1 != null && jsonObject1.containsKey("token")) {
//                        token = (String) jsonObject1.get("token");
//                        //放入redis缓存,有效期30分钟
//                        template.opsForValue().set(GlobalConstant.CTCC_TOKEN_KEY, token, 3600, TimeUnit.SECONDS);
//                    }
//                }
//            }
//        }
        log.info("移动excel上传接口=================>token记录:"+token);
//        if (!StringUtils.isNotEmpty(token)) {
//            log.error("移动入库出错=============>获取token失败");
//            throw new EngineException("移动入库获取token失败");
//        }
        String finalToken = token;
        WebAsyncTask<List<String>> webAsyncTask = new WebAsyncTask<>(300L, () -> {
            log.info("<---------移动入库线程" + Thread.currentThread().getName()+"开始处理");
            List<String> strings = warehousingService.getChinaMobile(copyrightIds, finalToken);
            log.info("<---------移动入库线程"+ Thread.currentThread().getName()+"处理成功");
            return strings;
        });
        webAsyncTask.onCompletion(() -> log.info("<---------移动入库线程"+Thread.currentThread().getName() + " 执行完毕"));
        webAsyncTask.onTimeout(() -> {
            log.info("<---------移动入库线程"+Thread.currentThread().getName() + " onTimeout");
            throw new TimeoutException("后台处理中,请稍后查看");
        });
        webAsyncTask.onError(() -> {
            log.error("<---------移动入库线程"+Thread.currentThread().getName() + "异步处理异常");
            throw new EngineException("异步处理异常");
        });
        log.info("<---------移动入库线程"+Thread.currentThread().getName() + "从控制器方法返回");
        return webAsyncTask;
    }

    @ApiOperation(value = "电信excel上传接口",httpMethod = "POST",notes = "excel格式为电信平台拉取的原始模板")
    @RequestMapping(value="/upload/ctcc" ,headers="content-type=multipart/form-data",method = RequestMethod.POST)
    @ResponseBody
    public WebAsyncTask<List<String>>  ctccUpload(@ApiParam(value = "上传的文件", required = true)
                                                @RequestParam("excelFile") MultipartFile excelFile) {
        checkExcel(excelFile);
        List<Map<String,String>> copyrightIds;
        try{
            copyrightIds = ExcelUtils.getCtccCopyrightIds(excelFile.getInputStream());
        }catch (Exception e){
            e.printStackTrace();
            log.error("电信excel上传接口=============>IO流操作异常");
            throw new EngineException("IO流操作异常");
        }
        WebAsyncTask<List<String>> webAsyncTask = new WebAsyncTask<>(300L,() -> {
            log.info("<---------电信入库线程" + Thread.currentThread().getName()+"开始处理");
            List<String> strings = warehousingService.getChinaTelecom(copyrightIds);
            log.info("<---------电信入库线程"+ Thread.currentThread().getName()+"处理成功");
            return strings;
        });
        webAsyncTask.onCompletion(() -> log.info("<---------电信入库线程"+Thread.currentThread().getName() + " 执行完毕"));
        webAsyncTask.onTimeout(() -> {
            log.info("<---------电信入库线程"+Thread.currentThread().getName() + " onTimeout");
            throw new TimeoutException("后台处理中,请稍后查看");
        });
        webAsyncTask.onError(() -> {
            log.error("<---------电信入库线程"+Thread.currentThread().getName() + "异步处理异常");
            throw new EngineException("异步处理异常");
        });
        log.info("<---------电信入库线程"+Thread.currentThread().getName() + "从控制器方法返回");
        return webAsyncTask;
    }


    private void checkExcel(MultipartFile excelFile){
        String name = excelFile.getOriginalFilename();
        if (StringUtils.isNotEmpty(name)){
            String suffix = name.substring(name.lastIndexOf(".")).toLowerCase();
            if (!".xls".equals(suffix)&&!".xlsx".equals(suffix)) {
                throw new EngineException(1003, "excel文件格式错误");
            }
        }else {
            throw new EngineException(1004,"excel文件名缺失");
        }
    }

    private void checkImg(MultipartFile imgFile){
        log.info("imgFile="+imgFile);
        String name = imgFile.getOriginalFilename();
        if (StringUtils.isNotEmpty(name)){
            String suffix = name.substring(name.lastIndexOf(".")).toLowerCase();
            System.out.println("后缀为"+suffix);
        }
    }
}