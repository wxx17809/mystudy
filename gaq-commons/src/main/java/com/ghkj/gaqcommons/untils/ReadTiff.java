package com.ghkj.gaqcommons.untils;

import com.drew.imaging.tiff.TiffMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;
import org.gdal.gdalconst.gdalconstConstants;

import java.io.File;
import java.util.Iterator;

public class ReadTiff {

    public  static void  readFlt() {
        gdal.AllRegister(); //GDAL所有操作都需要先注册格式
        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8","YES");
        String rasterFilePath ="E:\\soft\\tobai\\006_K50E024006_DOM_20191223\\K50E024006_DOM-20191223181231-1.tif";//测试用文件路径
        Dataset dataset = gdal.Open(rasterFilePath,
                gdalconstConstants.GA_ReadOnly);
//        if (dataset == null) {
//            System.out.println("GDAL read error: " + gdal.GetLastErrorMsg());
//        }
        Driver driver = dataset.GetDriver();
        System.out.println("driver short name: " + driver.getShortName());
        System.out.println("driver long name: " + driver.getLongName());
        System.out.println("metadata list: " + driver.GetMetadata_List());

        int xsize = dataset.getRasterXSize();
        System.out.println("xsize==="+xsize);
        int ysize = dataset.getRasterYSize();
        System.out.println("ysize==="+ysize);
        int count = dataset.getRasterCount();
        System.out.println("count==="+count);
        String proj = dataset.GetProjection();
        Band band = dataset.GetRasterBand(1);
        // 左上角点坐标 lon lat: transform[0]、transform[3]
        // 像素分辨率 x、y方向 : transform[1]、transform[5] 表示图像横向和纵向的分辨率（一般这两者的值相等，符号相反，横向分辨率为正数，纵向分辨率为负数）
        // 旋转角度: transform[2]、transform[4]) 表示图像旋转系数，对于一般图像来说，这两个值都为0。
        double[] transform = dataset.GetGeoTransform();
        System.out.println("======="+transform.length);
        System.out.println("11经度: " + transform[0]);
        System.out.println("11纬度: " + transform[3]);
    }


    public static void readTifUtil(){
        gdal.AllRegister(); //GDAL所有操作都需要先注册格式
        gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8","YES");
        String rasterFilePath ="E:\\soft\\tobai\\006_K50E024006_DOM_20191223\\K50E024006_DOM-20191223181231-1.tif";//测试用文件路径
        //读取影像
        Dataset rds = gdal.Open(rasterFilePath, gdalconst.GA_ReadOnly);
        //宽、高、波段数
        int x = rds.getRasterXSize();
        System.out.println("宽==="+x);
        int y = rds.getRasterYSize();
        System.out.println("高==="+y);
        int b = rds.getRasterCount();
        System.out.println("波段数==="+b);

        //六参数信息
        double[] geoTransform = rds.GetGeoTransform();
        //影像左上角投影坐标
        double[] ulCoord = new double[2];
        ulCoord[0] = geoTransform[0];
        System.out.println("左上角的经度==="+ulCoord[0]);
        ulCoord[1] = geoTransform[3];
        System.out.println("左上角的纬度==="+ulCoord[1]);
        //影响右上角投影坐标
        //upRighg(x)=GT(0)+srcWidth*GT(1)+0*GT(2)
        //upRight(y)=GT(3)+srcWidth*GT(4)+0*GT(5)
        double[]upRight=new double[2];
        upRight[0]=geoTransform[0]+x*geoTransform[1]+0*geoTransform[2];
        System.out.println("右上角的经度==="+upRight[0]);
        upRight[1]=geoTransform[3]+x*geoTransform[4]+0*geoTransform[5];
        System.out.println("右上角的纬度==="+upRight[1]);
        //downLeft(x)=GT(0)+0*GT(1)+srcHeight*GT(2)
        //downLeft(y)=GT(3)+0*GT(4)+srcHeight*GT(5)
        //图像左下角坐标
        double[] downLeft=new double[2];
        downLeft[0]=geoTransform[0]+0*geoTransform[1]+y*geoTransform[2];
        System.out.println("左下角经度==="+downLeft[0]);
        downLeft[1]=geoTransform[3]+0*geoTransform[4]+y*geoTransform[5];
        System.out.println("左下角纬度==="+downLeft[1]);
        //影像右下角投影坐标
        double[] brCoord = new double[2];
        brCoord[0] = geoTransform[0] + x * geoTransform[1] + y * geoTransform[2];
        System.out.println("右下角的经度==="+brCoord[0]);
        brCoord[1] = geoTransform[3] + x * geoTransform[4] + y * geoTransform[5];
        System.out.println("右下角的纬度==="+brCoord[1]);
    }

    public static void main(String[] args) throws Exception{
        readTifUtil();

    }

}



