/**
 * @filename:IElementService 2020年10月01日
 * @project rtb  V1.0
 * Copyright(c) 2020 Emiya Co. Ltd. 
 * All right reserved. 
 */
package com.yanhua.rtb.service;

import com.yanhua.rtb.entity.Element;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yanhua.rtb.vo.ElementVo;
import com.yanhua.rtb.vo.TempletVo;

import java.util.List;
import java.util.Map;

/**
 * @description 元素表服务层
 * @version V1.0
 * @author Emiya
 * 
 */
public interface IElementService extends IService<Element> {


    /**
     *
     * @description: 根据模板id统计推荐位数量
     *      <p/>
     * @param templetId:
     * @return int
     */
    int countElementNumByTempletId(Integer templetId);

    /**
     *
     * @description: 新增推荐位, 生成推荐位id
     *      <p/>
     * @param elementVo:
     * @return com.yanhua.rtb.vo.ElementVo
     */
    ElementVo saveElement(ElementVo elementVo);

    /**
     *
     * @description: 删除推荐位id
     *      <p/>
     * @param elementId:
     * @return java.lang.String
     */
    String deleteElement(Integer elementId);

    /**
     *
     * @description: 检查推荐位id
     *      <p/>
     * @param elementId:
     * @return void
     */
    void checkElement(Integer elementId);

    /**
     *
     * @description: 根据模板id删除所有推荐位
     *      <p/>
     * @param templetId:
     * @return int
     */
    String deleteElementAll(Integer templetId);

    /**
     *
     * @description: 更新单个推荐位
     *      <p/>
     * @param elementVo:
     * @return java.lang.String
     */
    String updateElement(ElementVo elementVo);

    List<Integer> updateElementAll(List<ElementVo> elementVos,Integer templetId);

    Map<String,Object> saveOrUpdateElements(List<Element> elements);

    ElementVo getElementAll(Integer elementId);

    List<ElementVo> getElementAlls (Integer templetId);
	
}