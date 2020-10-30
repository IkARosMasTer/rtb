/**
 * @filename:ContentSpxlServiceImpl 2020年10月13日
 * @project rtb  V1.0
 * Copyright(c) 2018 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yanhua.rtb.common.EngineException;
import com.yanhua.rtb.entity.ContentSpxl;
import com.yanhua.rtb.entity.ContentSpxlDetail;
import com.yanhua.rtb.mapper.ContentSpxlDetailMapper;
import com.yanhua.rtb.mapper.ContentSpxlMapper;
import com.yanhua.rtb.service.IContentSpxlDetailService;
import com.yanhua.rtb.service.IContentSpxlService;
import com.yanhua.rtb.vo.ContentSpxlVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**   
 * @description 联通入库表服务实现
 *
 * @version V1.0
 * @author Emiya
 * 
 */
@Slf4j
@Service
public class ContentSpxlServiceImpl extends ServiceImpl<ContentSpxlMapper, ContentSpxl> implements IContentSpxlService  {


    @Autowired
    private ContentSpxlMapper contentSpxlMapper;
    @Autowired
    private ContentSpxlDetailMapper contentSpxlDetailMapper;
    @Autowired
    private IContentSpxlDetailService iContentSpxlDetailService;
    @Override
    public ContentSpxl selectByWrapper(String contentId,String copyrightId) {

        if (StringUtils.isEmpty(contentId)||copyrightId==null){
            throw new EngineException(1002,"内容id或者版权id为空");
        }
        List<ContentSpxl> contentSpxls = contentSpxlMapper.selectList(new QueryWrapper<ContentSpxl>().lambda().eq(ContentSpxl::getContentId,contentId).eq(ContentSpxl::getCopyrightId,copyrightId));
        if (contentSpxls!=null&&contentSpxls.size()>0){
            return contentSpxls.get(0);
        }
        return null;
    }

    @Transactional(rollbackFor = {Exception.class,EngineException.class})
    @Override
    public String update(ContentSpxl contentSpxl, List<ContentSpxlDetail> contentSpxlDetails) {
        if (contentSpxl==null){
            throw new EngineException(1002,"彩铃主体为空");
        }
//        int upNum = contentSpxlMapper.update(contentSpxl,new UpdateWrapper<ContentSpxl>().lambda().eq(ContentSpxl::getContentId,contentSpxl.getContentId()).eq(ContentSpxl::getCopyrightId,contentSpxl.getCopyrightId()));
        int upNum = contentSpxlMapper.updateById(contentSpxl);
        contentSpxlDetails.stream().filter(Objects::nonNull).forEach(contentSpxlDetail -> contentSpxlDetail.setContentId(contentSpxl.getContentId()));
        int delNum = contentSpxlDetailMapper.delete(new QueryWrapper<ContentSpxlDetail>().lambda().eq(ContentSpxlDetail::getContentId,contentSpxl.getContentId()));
        boolean res = iContentSpxlDetailService.saveBatch(contentSpxlDetails);
        String ret = "copyrightId:"+contentSpxl.getCopyrightId()+",contentId:"+contentSpxl.getContentId()+",更新主体"+upNum+"条"+"删除详细"+delNum+"条并新增"+res;
        log.info("彩铃入库更新===========>{}",ret);
        return ret;
    }

    @Transactional(rollbackFor = {Exception.class,EngineException.class})
    @Override
    public String save(ContentSpxl contentSpxl, List<ContentSpxlDetail> contentSpxlDetails) {
        if (contentSpxl==null){
            throw new EngineException(1002,"彩铃主体为空");
        }
        int insNum = contentSpxlMapper.insert(contentSpxl);
        contentSpxlDetails.stream().filter(Objects::nonNull).forEach(contentSpxlDetail -> contentSpxlDetail.setContentId(contentSpxl.getContentId()));
        boolean res = iContentSpxlDetailService.saveBatch(contentSpxlDetails);
        String ret = "copyrightId:"+contentSpxl.getCopyrightId()+",contentId:"+contentSpxl.getContentId()+",新增主体"+insNum+"条"+"新增详细"+res;
        log.info("彩铃入库新增==========={}",ret);
        return ret;
    }


    @Override
    public IPage<ContentSpxlVo> pageVo(IPage<ContentSpxlVo> page, Wrapper<ContentSpxl> queryWrapper) {
        return page.setRecords(contentSpxlMapper.pageVo(page,queryWrapper));
    }
}