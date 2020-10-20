/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: WarehousingService
 * Author: Emiya
 * Date: 2020/10/13 9:44
 * Description: 入库接口
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.service;

import java.util.List;

/**
 * 〈功能简述〉<br>
 * 〈入库接口〉
 *  <p>
 * @author Emiya
 * @create 2020/10/13 9:44
 * @version 1.0.0
 */
public interface WarehousingService {

    /**
     *
     * @description: 中国联通彩铃信息入库
     *      <p/>
     * @param copyrightIds:
     */
    List<String> getCnUnicom(List<String> copyrightIds);

}