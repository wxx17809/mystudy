package com.ghkj.gaqweb.untils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;


public class ExcelReader {

    public  static void readExcel(){
        //excel文件路径
        //String excelPath = "C:\\Users\\Administrator\\Desktop\\aa.xlsx";
        String excelPath = "C:\\Users\\Administrator\\Desktop\\bb.xls";
        try{
            //String encoding = "GBK";
            File excel = new File(excelPath);
            if (excel.isFile() && excel.exists()) {   //判断文件是否存在
                Workbook wb=null;
                wb = readExcel(excelPath);
                //获取excel下的sheet页总个数
                int maxSheet = wb.getNumberOfSheets();
                //获取各个excel，可以控制需要导入那个页
                for(int sheeti = 0; sheeti < maxSheet; sheeti++){
                    //开始解析
                    Sheet sheet = wb.getSheetAt(sheeti);     //读取每个sheet
                    int firstRowIndex = sheet.getFirstRowNum() + 1;   //第一行是列名，所以不读
                    int lastRowIndex = sheet.getLastRowNum();
                    for (int rIndex = firstRowIndex; rIndex <= lastRowIndex; rIndex++) {   //遍历行
                        Row row = sheet.getRow(rIndex);
                        if (row != null) {
                            int firstCellIndex = row.getFirstCellNum();
                            int lastCellIndex = row.getLastCellNum();
                            for (int cIndex = firstCellIndex; cIndex < lastCellIndex; cIndex++) {   //遍历列
                                Cell cell = row.getCell(cIndex);
                                if (cell != null) {
                                    System.out.println(cell.toString());
                                }
                            }
                        }
                    }
                }
            } else {
                System.out.println("找不到指定的文件");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    //读取excel
    public static Workbook readExcel(String filePath){
        Workbook wb = null;
        if(filePath==null){
            return null;
        }
        String extString = filePath.substring(filePath.lastIndexOf("."));
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
            if(".xls".equals(extString)){
                return wb = new HSSFWorkbook(is);
            }else if(".xlsx".equals(extString)){
                return wb = new XSSFWorkbook(is);
            }else{
                return wb = null;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wb;
    }

    public static void main(String[] args) {
       readExcel();
    }


}

