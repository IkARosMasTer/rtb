package com.yanhua.rtb;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yanhua.rtb.entity.ContentSpxl;
import com.yanhua.rtb.service.IContentSpxlService;
import com.yanhua.rtb.service.IElementService;
import com.yanhua.rtb.util.HttpTools;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RtbApplicationTests {

    @Autowired
    private IContentSpxlService iElementService;
    @Test
    void contextLoads() {
        ContentSpxl contentSpxl = iElementService.getOne(new QueryWrapper<ContentSpxl>().lambda().eq(ContentSpxl::getContentId,"79858000202008215771430"));
        System.out.println(contentSpxl);
    }

}
