/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: WarehousingServiceImpl
 * Author: Emiya
 * Date: 2020/10/13 9:57
 * Description: 入库接口实现类
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yanhua.rtb.common.EngineException;
import com.yanhua.rtb.entity.ContentSpxl;
import com.yanhua.rtb.entity.ContentSpxlDetail;
import com.yanhua.rtb.service.IContentSpxlService;
import com.yanhua.rtb.service.WarehousingService;
import com.yanhua.rtb.util.FileUtil;
import com.yanhua.rtb.util.HttpTools;
import com.yanhua.rtb.util.Md5;
import com.yanhua.rtb.util.URLStreamTool;
import com.yanhua.rtb.vo.ContentSpxlVo;
import com.yanhua.rtb.vo.CtccContentSpxlVo;
import io.swagger.models.auth.In;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 〈功能简述〉<br>
 * 〈入库接口实现类〉
 *  <p>
 * @author Emiya
 * @create 2020/10/13 9:57
 * @version 1.0.0
 */
@Slf4j
@Service
public class WarehousingServiceImpl implements WarehousingService {

    @Value("${warehousing.cnUnicom.url}")
    private String cnUnicomUrl;

    @Value("${warehousing.cnUnicom.code}")
    private String cnUnicomCode;

    @Value("${warehousing.cnUnicom.md5}")
    private String cnUnicomMd5;

    @Value("${warehousing.cnUnicom.fileUrl}")
    private String cnUnicomFileUrl;

    @Value("${warehousing.cnUnicom.user}")
    private String cnUnicomUser;

    @Value("${warehousing.chinaMobile.url}")
    private String chinaMobileUrl;

    @Value("${warehousing.chinaMobile.tokenUrl}")
    private String chinaMobileTokenUrl;

    @Value("${warehousing.chinaMobile.channelCode}")
    private String channelCode;

    @Autowired
    private Environment environment;

    @Autowired
    private IContentSpxlService iContentSpxlService;

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

    @Transactional(rollbackFor=Exception.class)
    @Override
    public List<String> getCnUnicom(List<String> copyrightIds) {
        List<String> rets = new ArrayList<>();
        if (copyrightIds!=null&&copyrightIds.size()>0){
            //循环访问获取数据
            try {
                rets = copyrightIds.stream().filter(StringUtils::isNotBlank).map(s -> {
                    String ret = buildingCuccUrl(s);
//                    System.out.println(ret);
                    if (StringUtils.isNotEmpty(ret)){
                        JSONObject jsonObject = JSON.parseObject(ret);
                        if (jsonObject.containsKey("code")&& "000".equals(jsonObject.get("code"))){
                            JSONArray jsonArray = jsonObject.getJSONArray("contentSpxlData");
                            List<ContentSpxl> contentSpxls = JSONArray.parseArray(jsonArray.toString(),ContentSpxl.class);
                            StringBuilder stringBuffer = new StringBuilder();
                            String finalS = s;
                            contentSpxls.stream().filter(Objects::nonNull).forEach(contentSpxl -> {
                                String cpId = null;
                                cpId = contentSpxl.getCpId()==null?StringUtils.substring(finalS,0,8): String.valueOf(contentSpxl.getCpId());
                                if ("80721000".equals(cpId)){
                                    //属 粒子文化
                                    contentSpxl.setAttribution(2);
                                }else if ("79858000".equals(cpId)){
                                    //属 岩华
                                    contentSpxl.setAttribution(1);
                                }else {
                                    //属 其他
                                    contentSpxl.setAttribution(9);
                                }
                                contentSpxl.setOperator(2);
                                operate(stringBuffer,contentSpxl);
                            });
                            s = stringBuffer.toString();
                            log.info("联通入库记录================>{}",s);
                        }
                    }
                    return s;
                }).collect(Collectors.toList());
            }catch (Exception e){
                e.printStackTrace();
                log.error("联通接口批量入库异常!");
                throw new EngineException("联通接口批量入库异常!"+e.getMessage());
            }
        }
        return rets;
    }

    @Override
    public List<String> getChinaMobile(List<Map<String,String>> copyrightIds) {
        List<String> retList = new ArrayList<>();
        if (copyrightIds!=null&&copyrightIds.size()>0){
            try {
                retList = copyrightIds.stream().filter(Objects::nonNull).map(s -> {
                    StringBuilder stringBuffer = new StringBuilder(s.get("copyrightId"));
                    ContentSpxlVo contentSpxlVo = buildingCtccUrl(s);
                    if (contentSpxlVo!=null){
                        ContentSpxl contentSpxl = new ContentSpxl();
                        BeanUtils.copyProperties(contentSpxlVo,contentSpxl);
                        contentSpxl.setOperator(1);
                        contentSpxl.setCopyrightId(s.get("copyrightId"));
                        List<ContentSpxlDetail> contentSpxlDetails = contentSpxlVo.getFileList().stream().filter(Objects::nonNull).map(contentSpxlDetailVo -> {
                            ContentSpxlDetail contentSpxlDetail = new ContentSpxlDetail();
                            BeanUtils.copyProperties(contentSpxlDetailVo,contentSpxlDetail);
                            return contentSpxlDetail;
                        }).collect(Collectors.toList());
                        contentSpxl.setFileList(contentSpxlDetails);
                        //数据库操作
                        operate(stringBuffer,contentSpxl);
                    }
                    log.info("移动入库记录================>{}",stringBuffer.toString());
                    return stringBuffer.toString();
                }).collect(Collectors.toList());
            }catch (Exception e ){
                e.printStackTrace();
                log.error("移动接口批量入库异常!");
                throw new EngineException("移动接口批量入库异常!"+e.getMessage());
            }
        }
        retList.forEach(System.out::println);
        return retList;
    }

    private void operate (StringBuilder stringBuilder, ContentSpxl contentSpxl){
        try{
            //将图片、文件保存本地服务器
//                                    pullPoster(contentSpxl);
//                                    pullVod(contentSpxlDetails,contentSpxl.getContentId());
            ContentSpxl contentSpxl1 = iContentSpxlService.selectByWrapper(contentSpxl.getContentId(),contentSpxl.getCopyrightId());
            if (contentSpxl1!=null){
                //说明已存在，则更新
                contentSpxl.setId(contentSpxl1.getId());
                stringBuilder.append(iContentSpxlService.update(contentSpxl, contentSpxl.getFileList()));
            }else {
                //新增
                stringBuilder.append(iContentSpxlService.save(contentSpxl,contentSpxl.getFileList()));
            }
        }catch (Exception e){
            //操作数据库出错，跳过该条
            stringBuilder.append("copyrightId:").append(contentSpxl.getCopyrightId()).append(",").append(e.getMessage());
            log.error("视频彩铃入库出错=============>copyrightId:{},{}",contentSpxl.getCopyrightId(),e.getMessage());
        }
    }


    /**
     *
     * @description: 获取联通平台彩铃数据
     *      <p/>
     * @param copyrightId:
     * @return java.lang.String
     */
    private String buildingCuccUrl(String copyrightId){
        Date date = new Date();
        try {
            String timestamp = simpleDateFormat.format(date);
            String digest = "appkey"+cnUnicomCode+"timestamp"+timestamp+cnUnicomMd5;
            digest = Md5.crypt(digest);
            String url = cnUnicomUrl+"?appkey="+cnUnicomCode+"&timestamp="+timestamp+"&digest="+digest;
            String body = "{\"batch\":\"\",\"channelId\":\""+cnUnicomCode+"\",\"copyrightIds\":\""+copyrightId+"\",\"dataIds\":\"\",\"endTime\":\"\",\"pageEnd\":100,\"pageStart\":1,\"startTime\":\"\",\"types\":\"contentSpxl\"}";
            System.out.println(url);
            System.out.println(body);
            return HttpTools.doPost(url,body);
        }catch (Exception e){
            log.error("联通入库出错=============>获取平台彩铃异常,copyrightId{}跳过,{}",copyrightId,e.getMessage());
        }
        return null;
    }
    /**
     *
     * @description: 获取移动平台彩铃数据
     *      <p/>
     * @param map:
     * @return java.lang.String
     */
    private ContentSpxlVo buildingCtccUrl(Map<String,String> map){
        String copyrightId = map.get("copyrightId");
        //分类标签
        String label = map.get("label");
        try {


            String body = "{\n" +
                    "    \"msisdn\": 15906632054\n" +
                    "}";
//            String ret = HttpTools.doPost(chinaMobileTokenUrl,body);
            String ret = "{\n" +
                    "    \"code\": 200,\n" +
                    "    \"data\": {\n" +
                    "        \"resCode\": \"000000\",\n" +
                    "        \"msisdn\": \"182*****769\",\n" +
                    "        \"resMsg\": \"【OPEN】操作成功\",\n" +
                    "        \"token\": \"19f9d0aa65454c48aba9a3d3c070fb3e\"\n" +
                    "    },\n" +
                    "    \"mes\": \"ok\"\n" +
                    "}";
            String token = "";
            if (StringUtils.isNotEmpty(ret)) {
                JSONObject jsonObject = JSON.parseObject(ret);
                if (jsonObject.containsKey("code")&&((Integer) jsonObject.get("code")==200)){
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    if (jsonObject1!=null&&jsonObject1.containsKey("token")){
                        token = (String) jsonObject1.get("token");
                    }
                }
            }
            if (StringUtils.isNotBlank(token)) {
                String data = "{\"youCallbackName\":\"infoDataCallBack\",\"channelCode\":" + channelCode + ",\"token\":" + token + ",\"vrbtId\":" + copyrightId + "}";
//                String ff = "https://m.12530.com/order/rest/vrbt/product/query.do?data=";
//                String gg = "{\"youCallbackName\":\"infoDataCallBack\",\"channelCode\":\"00210OX\",\"token\":\"8bf9adf62276420d9fa4c229d8f3adbd\",\"vrbtId\":\"699052T2491M\"}";
                data = URLEncoder.encode(data,"utf-8");
//                ff = ff+gg;
                String ret1 = HttpTools.doHttpGet(chinaMobileUrl + data);
                System.out.println("ff="+chinaMobileUrl + data);
//                String ret1 = HttpTools.doHttpGet(ff);
//                System.out.println("ret="+ret1);
                if (StringUtils.isNotBlank(ret1)){
                    JSONObject jsonObject = JSON.parseObject(ret1);
                    if (jsonObject.containsKey("resCode")&&"000000".equals(jsonObject.get("resCode"))){
                        CtccContentSpxlVo ctccContentSpxlVo = JSONObject.parseObject(String.valueOf(jsonObject.getJSONObject("vrbtProduct")),CtccContentSpxlVo.class);
                        if (ctccContentSpxlVo!=null){
                            return new ContentSpxlVo(ctccContentSpxlVo,label,copyrightId);
                        }
                    }
                }
            }else {
                log.error("移动入库出错=============>获取token失败,copyrightId:{}跳过",copyrightId);
            }
        }catch (ParseException e){
            log.error("移动入库出错=============>copyrightId:{}跳过,日期转换异常{}",copyrightId,e.getMessage());
        }catch (Exception e) {
            log.error("移动入库出错=============>copyrightId:{}跳过,未知异常{}",copyrightId,e.getMessage());
        }
        return null;
    }

    /**
     *
     * @description: 拉取联通三种图片
     *      <p/>   
     * @param contentSpxl: 
     * @return void
     */
    private void pullPoster(ContentSpxl contentSpxl) throws EngineException {
        if (StringUtils.isNotEmpty(contentSpxl.getPoster())){
            String posterUrl = contentSpxl.getPoster();
            contentSpxl.setPoster(pull(posterUrl));
        }
        if (StringUtils.isNotEmpty(contentSpxl.getVerPoster())){
            String posterUrl = contentSpxl.getVerPoster();
            contentSpxl.setVerPoster(pull(posterUrl));
        }
        if (StringUtils.isNotEmpty(contentSpxl.getScreenshot())){
            String posterUrl = contentSpxl.getScreenshot();
            contentSpxl.setScreenshot(pull(posterUrl));
        }
    }

    /**
     *
     * @description: 遍历彩铃视频文件数组,远程拉取
     *      <p/>   
     * @param contentSpxlDetails: 
     * @param contentId: 
     * @return void
     */
    private void pullVod(List<ContentSpxlDetail> contentSpxlDetails,String contentId) throws EngineException {
        if (contentSpxlDetails!=null&&contentSpxlDetails.size()>0){
            contentSpxlDetails.stream().filter(contentSpxlDetail ->
                    contentSpxlDetail!=null&&StringUtils.isNotEmpty(contentSpxlDetail.getFilePath())).forEach(contentSpxlDetail -> {
                        String vodUrl = contentSpxlDetail.getFilePath();
                try {
                    contentSpxlDetail.setFilePath(pullVod(vodUrl,contentId));
                } catch (Exception e) {
                    throw new EngineException(e.getMessage());
                }
            });
        }
    }


    /**
     *
     * @description: 拉取联通远程视频文件
     *      <p/>   
     * @param vodUrl: 
     * @param contentId: 
     * @return java.lang.String
     */
    private String pullVod(String vodUrl,String contentId) throws Exception {
        String url = FileUtil.createVodPath(vodUrl);
        //文件名处理
        String localTempVodFile = FileUtil.resetPath( environment.getProperty("mams.path") + url);
        // 远程文件路径处理
        Date date = new Date();
        String timestamp = simpleDateFormat.format(date);
        String tokenId = cnUnicomCode+"&SHITING&"+timestamp+"&"+cnUnicomMd5+"&"+contentId;
        tokenId = Md5.crypt(tokenId);
        String remoteFileName =  cnUnicomFileUrl+vodUrl+"?user="+cnUnicomUser+"&channelid="+cnUnicomCode+"&contentid="+contentId+"&tokenid="+tokenId+"&timestamp="+timestamp;
        // ************** 文件获取 ********************
//        try {
//            URLStreamTool.downloadFile(remoteFileName, localTempVodFile);
//            return url;
//        } catch (Exception e) {
//            log.error("远程文件拉取失败:" + url,e);
//            throw new Exception("远程文件拉取失败:"+url+e.getMessage());
//        }
        return remoteFileName;
    }

    /**
     *
     * @description: 拉取联通图片文件
     *      <p/>   
     * @param posterUrl: 
     * @return java.lang.String
     */
    private String pull(String posterUrl)  {
        String imgUrl = FileUtil.createImgPath(posterUrl);
        // 文件名处理
        String localTempImgFile = FileUtil.resetPath( environment.getProperty("mams.path") + imgUrl);
        // 远程文件路径处理
        String remoteFileName =  cnUnicomFileUrl+posterUrl;
        // ************** 文件获取 ********************
        try {
            URLStreamTool.downloadFile(remoteFileName, localTempImgFile);
            return imgUrl;
        } catch (Exception e) {
            log.error("远程文件拉取失败:" + posterUrl,e);
            throw new EngineException("远程文件拉取失败:"+posterUrl+e.getMessage());
        }
    }

    public static void main(String[] args) {
//        WarehousingServiceImpl warehousingService = new WarehousingServiceImpl();
//        System.out.println(warehousingService.buildingUrl("44"));
        String jj = "{\"contentSpxlData\":[\n" +
                "\t\t{\n" +
                "\t\t\t\"serialNo\":\"0000000000300000749703000000013847379648\",\n" +
                "\t\t\t\"operation\":\"3\",\n" +
                "\t\t\t\"contentId\":\"79858000202008215771430\",\n" +
                "\t\t\t\"copyrightId\":\"79858000001502\",\n" +
                "\t\t\t\"songId\":\"3132127\",\n" +
                "\t\t\t\"contentName\":\"圣诞铃儿响叮当\",\n" +
                "\t\t\t\"contentEnName\":\"\",\n" +
                "\t\t\t\"spid\":\"90115000\",\n" +
                "\t\t\t\"contType\":\"0\",\n" +
                "\t\t\t\"para1\":\"\",\n" +
                "\t\t\t\"publishTime\":\"20200821154708\",\n" +
                "\t\t\t\"validDate\":\"20220822\",\n" +
                "\t\t\t\"grantType\":\"\",\n" +
                "\t\t\t\"useArea\":\"00086000000\",\n" +
                "\t\t\t\"spreadChannel\":\"\",\n" +
                "\t\t\t\"grantTimes\":\"\",\n" +
                "\t\t\t\"createTime\":\"\",\n" +
                "\t\t\t\"onlineDate\":\"\",\n" +
                "\t\t\t\"endDate\":\"20220822\",\n" +
                "\t\t\t\"format\":\"\",\n" +
                "\t\t\t\"feeType\":\"2\",\n" +
                "\t\t\t\"hightestPrice\":\"\",\n" +
                "\t\t\t\"lowestPrice\":\"\",\n" +
                "\t\t\t\"suggestPrice\":\"0000000000\",\n" +
                "\t\t\t\"discount\":\"\",\n" +
                "\t\t\t\"discountDesc\":\"\",\n" +
                "\t\t\t\"settlementParam\":\"\",\n" +
                "\t\t\t\"discountSettlementParam\":\"\",\n" +
                "\t\t\t\"range\":\"00086000000\",\n" +
                "\t\t\t\"terminalType\":\"\",\n" +
                "\t\t\t\"channelAbility\":\"70\",\n" +
                "\t\t\t\"password\":\"\",\n" +
                "\t\t\t\"contLevel\":\"01\",\n" +
                "\t\t\t\"isShow\":\"1\",\n" +
                "\t\t\t\"listenFilePath\":\"/entry?C=cms001&ContentID=79858000202008215771430&F=11820162\",\n" +
                "\t\t\t\"sendFilePath1\":\"/entry?C=cms001&ContentID=79858000202008215771430&F=11820162\",\n" +
                "\t\t\t\"label\":\"节日#圣诞节#动画#国外节日\",\n" +
                "\t\t\t\"price\":\"10\",\n" +
                "\t\t\t\"spproductid\":\"10\",\n" +
                "\t\t\t\"single\":\"1\",\n" +
                "\t\t\t\"ability\":\"0\",\n" +
                "\t\t\t\"sendfilepath2\":\"/entry?C=cms001&ContentID=79858000202008215771430&F=11820149\",\n" +
                "\t\t\t\"foreShow\":\"\",\n" +
                "\t\t\t\"wapicon\":\"\",\n" +
                "\t\t\t\"wwwicon\":\"\",\n" +
                "\t\t\t\"poster\":\"\",\n" +
                "\t\t\t\"verPoster\":\"/90115000/mv_vod/volteposter/verticalpath/20200821/1296715298867224578.jpg\",\n" +
                "\t\t\t\"screenshot\":\"/90115000/mv_vod/volteposter/screenshot/20200821/1296714874609483777.jpeg\",\n" +
                "\t\t\t\"fileList\":[\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"filePath\":\"/90115000/mv_vod/volte_mp4/20200821/1296715298208718850.mp4\",\n" +
                "\t\t\t\t\t\"fileCode\":\"MPEG-4\",\n" +
                "\t\t\t\t\t\"mime\":\"video/mp4\",\n" +
                "\t\t\t\t\t\"drmtype\":\"0\",\n" +
                "\t\t\t\t\t\"fileSize\":\"4833854\",\n" +
                "\t\t\t\t\t\"filePlayPath\":\"/entry?C=cms001&ContentID=79858000202008215771430&F=11820149\",\n" +
                "\t\t\t\t\t\"filePlayTime\":\"14\",\n" +
                "\t\t\t\t\t\"lyricPath\":\"\",\n" +
                "\t\t\t\t\t\"fileMl\":\"317\",\n" +
                "\t\t\t\t\t\"fileCyl\":\"\",\n" +
                "\t\t\t\t\t\"samplebitrate\":\"48000\",\n" +
                "\t\t\t\t\t\"samplingrate\":\"\",\n" +
                "\t\t\t\t\t\"resolution\":\"\",\n" +
                "\t\t\t\t\t\"aspectratio\":\"\",\n" +
                "\t\t\t\t\t\"filelhws\":\"16\",\n" +
                "\t\t\t\t\t\"fileSd\":\"2\"\n" +
                "\t\t\t\t},\n" +
                "\t\t\t\t{\n" +
                "\t\t\t\t\t\"filePath\":\"/90115000/mv_vod/voltemv/20200821/1296715297747345409.3gp\",\n" +
                "\t\t\t\t\t\"fileCode\":\"H.263\",\n" +
                "\t\t\t\t\t\"mime\":\"video/3gpp\",\n" +
                "\t\t\t\t\t\"drmtype\":\"0\",\n" +
                "\t\t\t\t\t\"fileSize\":\"740716\",\n" +
                "\t\t\t\t\t\"filePlayPath\":\"/entry?C=cms001&ContentID=79858000202008215771430&F=11820162\",\n" +
                "\t\t\t\t\t\"filePlayTime\":\"14\",\n" +
                "\t\t\t\t\t\"lyricPath\":\"\",\n" +
                "\t\t\t\t\t\"fileMl\":\"120\",\n" +
                "\t\t\t\t\t\"fileCyl\":\"\",\n" +
                "\t\t\t\t\t\"samplebitrate\":\"8000\",\n" +
                "\t\t\t\t\t\"samplingrate\":\"\",\n" +
                "\t\t\t\t\t\"resolution\":\"\",\n" +
                "\t\t\t\t\t\"aspectratio\":\"\",\n" +
                "\t\t\t\t\t\"filelhws\":\"8\",\n" +
                "\t\t\t\t\t\"fileSd\":\"1\"\n" +
                "\t\t\t\t}\n" +
                "\t\t\t],\n" +
                "\t\t\t\"descripiton\":\"圣诞铃儿响叮当\",\n" +
                "\t\t\t\"sourceCompany\":\"\",\n" +
                "\t\t\t\"sourceCpId\":\"\",\n" +
                "\t\t\t\"videoLevel\":\"\",\n" +
                "\t\t\t\"cpId\":\"79858000\"\n" +
                "\t\t}\n" +
                "\t]}";

        JSONObject jsonObject = JSON.parseObject(jj);
        JSONArray jsonArray = jsonObject.getJSONArray("contentSpxlData");
        List<ContentSpxl> contentSpxls = JSONArray.parseArray(jsonArray.toString(),ContentSpxl.class);

        System.out.println("dd"+contentSpxls.size());
    }
}