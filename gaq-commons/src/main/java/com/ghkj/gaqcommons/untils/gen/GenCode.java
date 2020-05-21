package com.ghkj.gaqcommons.untils.gen;

import org.apache.commons.lang3.SystemUtils;
import org.beetl.sql.core.SQLManager;
import org.beetl.sql.core.UnderlinedNameConversion;
import org.beetl.sql.core.db.TableDesc;
import org.beetl.sql.core.kit.StringKit;
import org.beetl.sql.ext.gen.GenConfig;
import org.beetl.sql.ext.gen.GenFilter;
import org.beetl.sql.ext.gen.MDCodeGen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.Properties;

public class GenCode {
    SQLManager sqlManager;
    GenConfig config;

    private GenCode() throws SQLClientInfoException {
        init();
    }

    public static void main(String[] args) throws Exception {
        GenCode gen = new GenCode();
         gen.genTable("jbbs_zhuti");


        //gen.genAll();
    }

    public Connection getConnection(String username, String password, String host, String port, String database) {
        Connection con = null;
        Properties props = new Properties();
        try {
            //Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useUnicode=true&characterEncoding=UTF8";
            props.setProperty("user", username);
            props.setProperty("password", password);
            props.setProperty("remarks", "true"); //设置可以获取remarks信息
            props.setProperty("useInformationSchema", "true");//设置可以获取tables remarks信息
            con = DriverManager.getConnection(url, props);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return con;
    }

    private void init() throws SQLClientInfoException {
        String url = "jdbc:mysql://localhost/jee?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC";
        sqlManager = SQLManager.newBuilder("com.mysql.cj.jdbc.Driver", url, "root", "root").build();
        sqlManager.setNc(new UnderlinedNameConversion());
        String userDir = SystemUtils.USER_DIR + "/data-entity";
        System.setProperty("user.dir", userDir);
        GenConfig genConfig = new GenConfig();
        String daoTemplate = genConfig.getTemplate("/mapper.btl");//模板名称在resource下
        //mapper文件所在的包的位置
        DaoCodeGen daoMapper = new DaoCodeGen("com.ghkj.gaqdao.mapper","com.ghkj.gaqentity",daoTemplate,
                "G:/mystudy/gaq-dao/src/main/java/");
        String serviceTemplate = genConfig.getTemplate("/service.btl");
        //service文件所在包的位置
        ServiceCodeGen serviceMapper = new ServiceCodeGen("com.ghkj.gaqservice.service", serviceTemplate,
                "G:/mystudy/gaq-service/src/main/java/");
        String serviceImplTemplate = genConfig.getTemplate("/serviceImpl.btl");
        //serviceImpl文件所在包的位置
        ServiceImplCodeGen serviceImplMapper = new ServiceImplCodeGen("com.ghkj.gaqservice.service.impl",
                "com.ghkj.gaqdao.mapper","com.ghkj.gaqservice.service", serviceImplTemplate,
                "G:/mystudy/gaq-service/src/main/java/");
        String controllerTemplate = genConfig.getTemplate("/controller.btl");
        //controller所在的包位置
        ControllerCodeGen controllerMapper = new ControllerCodeGen("com.ghkj.gaqweb.controller",
                "com.ghkj.gaqservice.service", controllerTemplate, "G:/mystudy/gaq-web/src/main/java/");

        config = new GenConfig();
        //不需要生成的注释掉即可
        config.codeGens.add(daoMapper);
        config.codeGens.add(serviceMapper);
        config.codeGens.add(serviceImplMapper);
        config.codeGens.add(controllerMapper);

        config.setTemplate(config.getTemplate("/entity.btl"));
        config.preferBigDecimal(true);
    }

    private void genAll() throws Exception {
        String pkg = "com.ghkj.gaqentity";
        GenFilter filter = new GenFilter() {
            @Override
            public boolean accept(String tableName) {
                System.out.println(tableName);
                return true;
            }
        };
        sqlManager.genALL(pkg, config, filter);
    }

    private void genTable(String table) throws Exception {
        String pkg = "com.ghkj.gaqentity";
        //生成dao、service、impl、controller、entity的代码
        sqlManager.genPojoCode(table, pkg,"G:/mystudy/gaq-entity/src/main/java/", config);
//        sqlManager.genSQLFile(table,null,config);
        //生成sql下XXX.md
        genMapperXml(sqlManager, config, table);
    }
    /**
     * 生成xml文件
     */
    public static void genMapperXml(SQLManager sqlManager, GenConfig config, String table) throws IOException {
        String fileName = StringKit.toLowerCaseFirstOne(sqlManager.getNc().getClassName(table));
        if (config.getIgnorePrefix() != null && !config.getIgnorePrefix().trim().equals("")) {
            fileName = fileName.replaceFirst(StringKit.toLowerCaseFirstOne(config.getIgnorePrefix()), "");
            fileName = StringKit.toLowerCaseFirstOne(fileName);
        }
        String target ="G:/mystudy/gaq-dao/src/main/resources/xml/" + fileName+"Mapper" + ".xml";
        TableDesc desc = sqlManager.getMetaDataManager().getTable(table);
        FileWriter writer = new FileWriter(new File(target));
        MDCodeGen mdCodeGen = new MDCodeGen();
        mdCodeGen.setMapperTemplate(config.getTemplate("/mapperXml.btl"));
        mdCodeGen.genCode(sqlManager.getBeetl(), desc, sqlManager.getNc(), null, writer);
        writer.close();
    }

}



