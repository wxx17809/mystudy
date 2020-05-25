package com.ghkj.gaqcommons.untils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;

public class ExcelUtil {
    /**
     * 方法描述：解析excel数据
     * 创建人：吴璇璇
     * 创建时间：2018年5月14日 下午5:42:44
     * @param file
     * @return
     */
    public static Map<String, Object> readExcel(MultipartFile file) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String legalSuffix = ".xls,.xlsx";
        String orignalFileName = file.getOriginalFilename();    //真实的文件名
        String suffix = orignalFileName.substring(orignalFileName.lastIndexOf('.'));
        //判断文件的合法性
        if (!legalSuffix.contains(suffix)) {
            dataMap.put("success", false);
            dataMap.put("msg", "非法的文件，请上传扩展名为\"" + legalSuffix + "\"的文件！");
            return dataMap;
        }
        Workbook wb;
        int index = file.getOriginalFilename().lastIndexOf(".");
        String fileType = file.getOriginalFilename().substring(index + 1);
        try {
            if ("xls".equals(fileType)) {//xls为97--2003格式
                wb = new HSSFWorkbook(file.getInputStream());
            } else {//xlsx为2007后的格式
                wb = new XSSFWorkbook(file.getInputStream());
            }
            //获取excel里sheet的总数
            int maxSheet = wb.getNumberOfSheets();
            for (int sheeti = 0; sheeti < maxSheet; sheeti++) {
                //开始解析
                Sheet sheet = wb.getSheetAt(sheeti);//读取每个sheet
                int firstRowIndex=sheet.getFirstRowNum()+1;//第一行是列名，所以不读
                int lastRowIndex=sheet.getLastRowNum();//最后一行
                for (int rIndex=firstRowIndex;rIndex<=lastRowIndex;rIndex++){
                    Row row=sheet.getRow(rIndex);
                    if(row!=null){
                        int firstCellIndex=row.getFirstCellNum();
                        int lastCellIndex=row.getLastCellNum();
                        for (int cIndex=firstCellIndex;cIndex<lastCellIndex;cIndex++){
                            Cell cell=row.getCell(cIndex);
                            System.out.println("====="+cell.toString());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("success", false);
            dataMap.put("msg", "excel解析失败!请按照模版格式上传");
        }
        return dataMap;
    }


}