package com.ghkj.gaqcommons.untils;

import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.*;
import jxl.write.biff.RowsExceededException;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        List<Map<String, Object>> arrcell=new ArrayList<>();
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
                    Map<String, Object> map=new HashMap<>();
                    Row row=sheet.getRow(rIndex);
                    if(row!=null){
                        int firstCellIndex=row.getFirstCellNum();
                        int lastCellIndex=row.getLastCellNum();
                        for (int cIndex=firstCellIndex;cIndex<lastCellIndex;cIndex++){
                            Cell cell=row.getCell(cIndex);
                            map.put(String.valueOf(cIndex),cell.toString());
                        }
                    }
                    arrcell.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            dataMap.put("success", false);
            dataMap.put("msg", "excel解析失败!请按照模版格式上传");
        }
        dataMap.put("arrcell", arrcell);
        return dataMap;
    }


    /***************************************************************************
     * @param fileName EXCEL文件名称
     * @param listContent EXCEL文件正文数据集合
     * @param response EXCEL文件第一行列标题集合
     * 导出excel
     * @return
     */
    public final static void exportExcel(String fileName, String[] Title, List<Map<Integer, Object>> listContent, HttpServletResponse response) {
        String result = "系统提示：Excel文件导出成功！";
        // 以下开始输出到EXCEL
        try {
            //定义输出流，以便打开保存对话框______________________begin
            OutputStream os = response.getOutputStream();// 取得输出流
            response.reset();// 清空输出流
            // 设定输出文件头
            response.setContentType("application/x-download");//下面三行是关键代码，处理乱码问题
            response.setCharacterEncoding("utf-8");
            //设置浏览器响应头对应的Content-disposition
            response.setHeader("Content-disposition", "attachment;filename="+new String(fileName.getBytes("gbk"), "iso8859-1"));
            //定义输出流，以便打开保存对话框_______________________end

            /** **********创建工作簿************ */
            WritableWorkbook workbook = jxl.Workbook.createWorkbook(os);

            /** **********创建工作表************ */

            WritableSheet sheet = workbook.createSheet("Sheet1", 0);

            /** **********设置纵横打印（默认为纵打）、打印纸***************** */
            jxl.SheetSettings sheetset = sheet.getSettings();
            sheetset.setProtected(false);


            /** ************设置单元格字体************** */
            WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
            WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

            /** ************以下设置三种单元格样式，灵活备用************ */
            // 用于标题居中
            WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
            wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
            wcf_center.setVerticalAlignment(jxl.format.VerticalAlignment.CENTRE); // 文字垂直对齐
            wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
            wcf_center.setWrap(false); // 文字是否换行

            // 用于正文居左
            WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
            wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
            wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
            wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐
            wcf_left.setWrap(true); // 文字是否换行


            /** ***************以下是EXCEL开头大标题，暂时省略********************* */
            //sheet.mergeCells(0, 0, colWidth, 0);
            //sheet.addCell(new Label(0, 0, "XX报表", wcf_center));
            /** ***************以下是EXCEL第一行列标题********************* */
            for (int i = 0; i < Title.length; i++) {
                sheet.addCell(new Label(i, 0, Title[i], wcf_center));
            }
            /** ***************以下是EXCEL正文数据********************* */
            int i = 1;
            for (Map<Integer, Object> map : listContent) {
                int j = 0;
                for (Integer s : map.keySet()) {
                    if (map.get(s) != null) {
                        System.out.println("====" + map.get(s).toString());
                        sheet.addCell(new Label(j, i, map.get(s).toString(), wcf_left));
                    } else {
                        sheet.addCell(new Label(j, i, "", wcf_left));
                    }
                    j++;
                }
                i++;
            }
            /** **********将以上缓存中的内容写到EXCEL文件中******** */
            workbook.write();
            /** *********关闭文件************* */
            workbook.close();
        } catch (IOException e) {
            result = "系统提示：Excel文件导出失败，原因：" + e.toString();
            System.out.println(result);
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }
}