/**
 * Copyright (C), 2020-2020, 浙江岩华文化科技有限公司
 * FileName: ExcelUtils
 * Author: Emiya
 * Date: 2020/10/13 16:00
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
package com.yanhua.rtb.util;

import com.yanhua.rtb.common.EngineException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.*;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 〈功能简述〉<br>
 * 〈〉
 *  <p>
 * @author Emiya
 * @create 2020/10/13 16:00
 * @version 1.0.0
 */
@Slf4j
public class ExcelUtils {

    public static List<String> getCopyrightIds(InputStream inputStream) {
        List<String> list = new ArrayList<>();
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
            inputStream.close();
            //工作表对象
            Sheet sheet = workbook.getSheetAt(0);
            //总行数
            int rowLength = sheet.getLastRowNum() + 1;
            if (rowLength <= 1) {
                log.error("文件内容有误");
                throw new EngineException("文件内容有误");
            }
            //工作表的列
            Row row = sheet.getRow(0);
            Cell cell = row.getCell(0);
            for (int i = 1; i < rowLength; i++) {
                String contentId = (String) getCellInfo(sheet.getRow(i), 0,null);
                if (StringUtils.isNotEmpty(contentId)){
                    list.add(contentId);
                }
            }
        } catch (Exception e) {
            log.error("parse excel file error :", e);
            throw new EngineException("读取excel失败");
        }
        return list;
    }


    public static Object getCellInfo(Row row, Integer num, String format) {
        Cell cell = row.getCell(num);
        if (cell != null) {
            int cellType = cell.getCellType();
            if (cellType == Cell.CELL_TYPE_NUMERIC) {
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    return DateFormatUtils.format(date, format);
                } else {
                    return (int) cell.getNumericCellValue();
                }
            } else if (cellType == Cell.CELL_TYPE_STRING) {
                return cell.getStringCellValue().trim();
            } else if (cellType == Cell.CELL_TYPE_BLANK) {
                return "";
            }
        }
        return "";
    }

}