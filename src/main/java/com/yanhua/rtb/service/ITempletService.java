/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: ITempletService
 * Author: Emiya
 * Date: 2020/10/15 10:36
 * Description: 模板（二级栏目）服务接口
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.service;

import com.sun.org.apache.xpath.internal.objects.XString;
import com.yanhua.rtb.entity.Element;
import com.yanhua.rtb.entity.Template;
import com.yanhua.rtb.vo.TemplateVo;
import com.yanhua.rtb.vo.TempletVo;

import java.util.List;
import java.util.Map;

/**
 * 〈功能简述〉<br>
 * 〈模板（二级栏目）服务接口〉
 *  <p>
 * @author Emiya
 * @create 2020/10/15 10:36
 * @version 1.0.0
 */
public interface ITempletService {

    /**
     *
     * @description: 新增模板, 生成模板id
     *      <p/>
     * @param templetVo:
     * @return com.yanhua.rtb.vo.TempletVo
     */
    TempletVo saveTemplet(TempletVo templetVo);

    /**
     *
     * @description: 删除模板以及其包含的推荐位
     *      <p/>
     * @param templetId:
     * @return java.lang.String
     */
    String deleteTemplet(Integer templetId);

    /**
     *
     * @description: 检查模板id
     *      <p/>
     * @param templetId:
     * @return void
     */
    TempletVo checkTemplet(Integer templetId);

    /**
     *
     * @description: 更新模板
     *      <p/>
     * @param templetVo:
     * @return java.lang.String
     */
    String updateTemplet(TempletVo templetVo);

    /**
     *
     * @description: 批量新增/更新模板
     *      <p/>
     * @param templetVos:
     */
    Map<String,Object> saveOrUpdateTemplets(List<TempletVo> templetVos, Integer columnId);

    List<Element> getElementCopy(Integer templetId);
}