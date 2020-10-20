/**
 * @filename:IContentSpxlService 2020年10月13日
 * @project rtb  V1.0
 * Copyright(c) 2020 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.yanhua.rtb.entity.ContentSpxl;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhua.rtb.entity.ContentSpxlDetail;

import java.util.List;

/**
 * @description 联通入库表服务层
 * @version V1.0
 * @author Emiya
 * 
 */
public interface IContentSpxlService extends IService<ContentSpxl> {

    ContentSpxl selectByWrapper(String contentId,Long copyrightId);

    String update(ContentSpxl contentSpxl, List<ContentSpxlDetail> contentSpxlDetails);

    String save(ContentSpxl contentSpxl,List<ContentSpxlDetail> contentSpxlDetails);
	
}