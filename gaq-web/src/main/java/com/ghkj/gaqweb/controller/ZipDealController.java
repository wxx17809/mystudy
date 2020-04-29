package com.ghkj.gaqweb.controller;


import com.ghkj.gaqcommons.untils.TokenUtils;
import com.ghkj.gaqcommons.untils.ZIPUtil;
import com.ghkj.gaqservice.service.ZipDealService;
import com.ghkj.gaqweb.untils.ExcelReader;
import com.ghkj.gaqweb.untils.FileConfig;
import com.ghkj.gaqweb.untils.FileUtil;
import com.sun.rowset.internal.Row;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.security.Key;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @version 1.0
 * @ClassName : ZipDealController
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/12/6 11:58
 */
@RestController
@RequestMapping("/hello")
public class ZipDealController {
    private static final Logger logger = LoggerFactory.getLogger(ZipDealController.class);

    @Autowired
    private ZipDealService zipDealService;

    /**
     * 上传target文件压缩包
     * 吴璇璇
     * 2019.10.28
     * @return
     * @throws Exception
     */
    @PostMapping("/importEsByXml")
    public com.alibaba.fastjson.JSONObject importEsByXml(HttpServletRequest request) throws Exception {
        String token=request.getParameter("token");
        int result = TokenUtils.ValidToken(token);
        logger.info("=========" + result);
        com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
        if (result == 0) {
            logger.info("进入导入数据的方法====");
            try {
                MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
                List<MultipartFile> files = params.getFiles("file");
                jsonObject = zipDealService.importEsByXml(files);
                logger.info("进入图片处理=====");
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("导入失败==" + e.getLocalizedMessage());
                logger.info("可能原因:服务器未启动，数据库没连接，用户名密码错误，数据库里没hjj索引");
            }
            jsonObject.put("state", 200);
        }
        return jsonObject;
    }


    /**
     * 这个deomo入参的类型是MultipartFile，很多网上的例子是File类型
     * @return
     * @throws Exception
     */
    @PostMapping("/addPersonsFileOfZip")
    public JSONObject addPersonsFileOfZip(HttpServletRequest request) throws Exception {
        JSONObject jsonObject=new JSONObject();
        try {
            MultipartHttpServletRequest params = ((MultipartHttpServletRequest) request);
            List<MultipartFile> files = params.getFiles("file");
            MultipartFile file=files.get(0);
            String name=file.getOriginalFilename();
            //把接收的压缩包存到D://test 文件夹下
            FileInputStream input=(FileInputStream) file.getInputStream();
            OutputStream outputStream=new FileOutputStream("D://test//"+name);
            byte[] but=new byte[8 * 1024];
            int s;
            while ((s=input.read(but,0,but.length))!=-1){
                outputStream.write(but);
                outputStream.flush();
            }
            //解压存放的压缩包
            ZIPUtil.decompressZip("D://test//"+name,"D://test//");
            //List<List<String>> aa=ExcelReader.readXlsx("D://test//aa//aa.xlsx");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("导入失败==" + e.getLocalizedMessage());
            logger.info("可能原因:服务器未启动，数据库没连接，用户名密码错误，数据库里没hjj索引");
        }

        return jsonObject;


    }

    private void readExcel(String s)throws Exception {
       /* String filePath=s+"aa.xlsx";

        //创建输入流
        File file=new File(filePath);
        InputStream in=new FileInputStream(file);

        //获得excel文件对应对象
        Workbook workbook;

        // 根据后缀，得到不同的Workbook子类，即HSSFWorkbook或XSSFWorkbook
        if(filePath.endsWith("XLSX")) {
            workbook = new XSSFWorkbook(in);
        }else {
            workbook = new HSSFWorkbook(in);
        }

        //获得sheet对应对象
        Sheet sheet=workbook.getSheetAt(0);

        //创建人员对象容器
        //List<Employee> employeeList =new ArrayList<>();
       // Employee employee;

        //创建表头容器
        List<String>  headers=new ArrayList<>();

        //解析sheet，将获得多行数据，并放入迭代器中
        Iterator<Row> ito=sheet.iterator();
        int count=0;

        Row row;
        while(ito.hasNext()) {
            row = ito.next();

            //由于第一行是标题，因此这里单独处理
            if (count == 0) {
                for(int i=0;i<10;i++){
                    headers.add(row.getCell(i).toString());
                }
            } else {
                //其它行都在这里处理

//                employee =new Employee();
//                employee.setRank(count);
//                employee.setNumber(row.getCell(1).toString());
//                employee.setName(row.getCell(2).toString());
//                employee.setDepartment(row.getCell(3).toString());
//                employee.setPosition(row.getCell(4).toString());
//                employee.setEmail(row.getCell(5).toString());
//                employee.setTelNum(row.getCell(6).toString());
//                employee.setUniversity(row.getCell(7).toString());
//                employee.setMajor(row.getCell(8).toString());
//                employee.setTeacher(row.getCell(9).toString());

                //employeeList.add(employee);
            }

            ++count;
        }

        //打印表头
        *//*for(String str:headers){
            System.out.println(str);
        }*//*


       // employeeList.get(4).info();
        workbook.close();
*/
    }


}
