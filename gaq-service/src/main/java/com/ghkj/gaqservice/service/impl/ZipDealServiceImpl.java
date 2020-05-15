package com.ghkj.gaqservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ghkj.gaqservice.service.ZipDealService;
import org.dom4j.io.SAXReader;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sun.misc.BASE64Encoder;


/**
 * @version 1.0
 * @ClassName : ZipDealServiceImpl
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/12/6 12:41
 */
@Service
public class ZipDealServiceImpl implements ZipDealService {
    private static final Logger logger = LoggerFactory.getLogger(ZipDealServiceImpl.class);

    @Value("${matchAllNum}")
    int matchAllNum;

    @Value("${elasticUserPassword}")
    private String elasticUserPassword;

    @Value("${elasticIp}")
    private String elasticIp;

    @Override
    public JSONObject importEsByXml(List<MultipartFile> files)throws Exception {
        JSONObject jsonObject = new JSONObject();
        List<Map> successMap = new ArrayList<>();
        List<Map> errorMap = new ArrayList<>();
        //获取文件输入流
        FileInputStream input = (FileInputStream) files.get(0).getInputStream();
        //FileInputStream input = new FileInputStream("F:\\aa.zip");
        //获取ZIP输入流(一定要指定字符集Charset.forName("GBK")否则会报java.lang.IllegalArgumentException: MALFORMED)
        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(input), Charset.forName("GBK"));
        //定义ZipEntry置为null,避免由于重复调用zipInputStream.getNextEntry造成的不必要的问题
        ZipEntry ze = null;
        //循环遍历
        while ((ze = zipInputStream.getNextEntry()) != null) {
            if(ze.isDirectory()==false) {
                logger.info("文件名：" + ze.getName() + " 文件大小：" + ze.getSize() + " bytes");
                StringBuffer buffer = new StringBuffer();
                //读取
                BufferedReader br = new BufferedReader(new InputStreamReader(zipInputStream, Charset.forName("UTF-8")));
                String str = null;
                //内容不为空，输出
                while ((str = br.readLine()) != null) {
                    buffer.append(str);
                }
                try {
                    InputStream stream = new ByteArrayInputStream(buffer.toString().getBytes());
                    //返回documentBuilderFactor对象
                    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                    //返回db对象
                    DocumentBuilder db = dbf.newDocumentBuilder();
                    Document document = db.parse(stream);
                    Element element = document.getDocumentElement();
                    NodeList childNodes = element.getChildNodes();//第一级节点
                    logger.info("childNodes====" + childNodes.getLength());
                    for (int i = 0; i < childNodes.getLength(); i++) {  //循环根节点
                        //获取其中一个根节点
                        Node node = childNodes.item(i);//获取其中一个节点
                        logger.info("==="+node.getNodeName());
                        logger.info("==="+node.getTextContent());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        //一定记得关闭流
        zipInputStream.closeEntry();
        input.close();
        return jsonObject;
    }
}
