/**
 * @filename:ContentSpxlDetailDao 2020年10月13日
 * @project rtb  V1.0
 * Copyright(c) 2020 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import com.yanhua.rtb.entity.ContentSpxlDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**   
 * @description 联通入库文件表数据访问层
 *
 * @version V1.0
 * @author Emiya
 * 
 */
@Mapper
public interface ContentSpxlDetailMapper extends BaseMapper<ContentSpxlDetail> {

    List<ContentSpxlDetail> getContentSpxlDetailResult(@Param("content_id6") Integer content_id6);
}
