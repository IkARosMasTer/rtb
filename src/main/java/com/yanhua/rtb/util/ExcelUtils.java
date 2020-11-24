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
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
    public static List<Map<String,String>> getCmccCopyrightIds(InputStream inputStream) {
        List<Map<String,String>> maps= new ArrayList<>();
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
            inputStream.close();
            //工作表对象
            Sheet sheet = workbook.getSheetAt(0);
            //总行数
            int rowLength = sheet.getLastRowNum() + 1;
            if (rowLength <=1) {
                log.error("文件内容有误");
                throw new EngineException("excel文件内容有误");
            }
            //工作表的列
            Row row = sheet.getRow(0);
            int cellLength = row.getPhysicalNumberOfCells();
            Integer copyrightIdNo = null;
            Integer labelNo = null;
            Integer contentSortNo = null;
            Integer cpNo = null;
            for (int j = 0; j< cellLength; j++) {
                Cell cell = row.getCell(j);
                if (cell!=null){
                    int cellType = cell.getCellType();
                    if (cellType == Cell.CELL_TYPE_STRING&&"版权编号".equals(cell.getStringCellValue().trim())){
                        copyrightIdNo = j;
                    }
                    if (cellType == Cell.CELL_TYPE_STRING&&"CPID".equals(cell.getStringCellValue().trim())){
                        cpNo = j;
                    }
                    if (cellType == Cell.CELL_TYPE_STRING&&"内容标识".equals(cell.getStringCellValue().trim())){
                        labelNo = j;
                    }
                    if (cellType ==Cell.CELL_TYPE_STRING&&"内容库分类".equals(cell.getStringCellValue().trim())){
                        contentSortNo = j;
                    }
                }
            }
            if (copyrightIdNo==null||contentSortNo==null||labelNo==null){
                log.error("excel文件内容无相应字段");
                throw new EngineException("excel文件内容无相应字段");
            }
            for (int i = 1; i< rowLength;i++){
                Map<String,String> map = new HashMap<>();
                String contentSort = (String) getCellInfo(sheet.getRow(i),contentSortNo,null);
                if (!"咪咕音乐订阅库".equals(contentSort)){
                    continue;
                }
                String contentId = (String) getCellInfo(sheet.getRow(i),copyrightIdNo,null);
                if (StringUtils.isNotEmpty(contentId)) {
                    map.put("copyrightId",contentId);
                }else {
                    continue;
                }
                String label = (String) getCellInfo(sheet.getRow(i),labelNo,null);
                map.put("label",label);
                maps.add(map);
            }
        }catch (IOException | InvalidFormatException e){
            log.error("parse excel file error :", e);
            throw new EngineException("读取excel失败");
        }
        return maps;
    }

    public static List<String> getCuccCopyrightIds(InputStream inputStream) {
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
            int cellLength = row.getPhysicalNumberOfCells();
            Integer copyrightIdNo = null;
            for (int j = 0; j< cellLength; j++) {
                Cell cell2 = row.getCell(j);
                if (cell2 != null) {
                    int cellType = cell2.getCellType();
                    if (cellType == Cell.CELL_TYPE_STRING && ("版权ID".equals(cell2.getStringCellValue().trim())||
                            "copyrightid".equals(cell2.getStringCellValue().trim()))) {
                        copyrightIdNo = j;
                        break;
                    }
                }
            }
            for (int i = 1; i < rowLength; i++) {
                String contentId = (String) getCellInfo(sheet.getRow(i), copyrightIdNo,null);
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


    public static List<Map<String,String>> getCtccCopyrightIds(InputStream inputStream) {
        List<Map<String,String>> maps= new ArrayList<>();
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(inputStream);
            inputStream.close();
            //工作表对象
            Sheet sheet = workbook.getSheetAt(0);
            //总行数
            int rowLength = sheet.getLastRowNum() + 1;
            if (rowLength <=1) {
                log.error("文件内容有误");
                throw new EngineException("excel文件内容有误");
            }
            //工作表的列
            Row row = sheet.getRow(0);
            int cellLength = row.getPhysicalNumberOfCells();
            Integer copyrightIdNo = null;
            Integer labelNo = null;
            Integer sortlNo = null;
            Integer contentSortName = null;
            for (int j = 0; j< cellLength; j++) {
                Cell cell = row.getCell(j);
                if (cell!=null){
                    int cellType = cell.getCellType();
                    if (cellType == Cell.CELL_TYPE_STRING&&"资源编码".equals(cell.getStringCellValue().trim())){
                        copyrightIdNo = j;
                    }
                    if (cellType == Cell.CELL_TYPE_STRING&&"视频标签".equals(cell.getStringCellValue().trim())){
                        labelNo = j;
                    }
                    if (cellType == Cell.CELL_TYPE_STRING&&"视频类型".equals(cell.getStringCellValue().trim())){
                        sortlNo = j;
                    }
                    if (cellType ==Cell.CELL_TYPE_STRING&&"上源授权方".equals(cell.getStringCellValue().trim())){
                        contentSortName = j;
                    }
                }
            }
            if (copyrightIdNo==null||contentSortName==null||labelNo==null||sortlNo==null){
                log.error("excel文件内容无相应字段");
                throw new EngineException("excel文件内容缺少相应字段");
            }
            for (int i = 1; i< rowLength;i++){
                Map<String,String> map = new HashMap<>();
                String contentSort = (String) getCellInfo(sheet.getRow(i),contentSortName,null);
                if (!"浙江岩华文化科技有限公司".equals(contentSort)){
                    continue;
                }
                String contentId = getCellInfo(sheet.getRow(i),copyrightIdNo,null).toString();
                if (StringUtils.isNotEmpty(contentId)) {
                    map.put("copyrightId",contentId);
                }else {
                    continue;
                }
                String label = (String) getCellInfo(sheet.getRow(i),labelNo,null);
                String sort = (String) getCellInfo(sheet.getRow(i),sortlNo,null);
                map.put("label",sort+"#"+label);
                maps.add(map);
            }
        }catch (IOException | InvalidFormatException e){
            log.error("parse excel file error :", e);
            throw new EngineException("读取excel失败");
        }
        return maps;
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