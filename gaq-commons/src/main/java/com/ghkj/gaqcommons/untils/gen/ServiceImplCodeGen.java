package com.ghkj.gaqcommons.untils.gen;


import org.apache.commons.lang3.StringUtils;
import org.beetl.core.Template;
import org.beetl.sql.core.db.TableDesc;
import org.beetl.sql.core.kit.GenKit;
import org.beetl.sql.ext.gen.CodeGen;
import org.beetl.sql.ext.gen.GenConfig;
import org.beetl.sql.ext.gen.SourceGen;

import java.io.IOException;

public class ServiceImplCodeGen implements CodeGen {
    String outPath = "";
    String pkg = null;
    String daoPkg = null;
    String servicePkg = null;
    String mapperTemplate = "";

    public ServiceImplCodeGen() {
    }

    public ServiceImplCodeGen(String pkg, String daoPkg, String servicePkg,String templatePath, String outPath) {
        this.pkg = pkg;
        this.mapperTemplate = templatePath;
        this.daoPkg = daoPkg;
        this.outPath = outPath;
        this.servicePkg = servicePkg;
    }

    @Override
    public void genCode(String entityPkg, String entityClass, TableDesc tableDesc, GenConfig config, boolean isDisplay) {
        if (pkg == null) {
            pkg = entityPkg;
        }
        Template template = SourceGen.getGt().getTemplate(mapperTemplate);
        String mapperClass = entityClass + "ServiceImpl";
        template.binding("className", mapperClass);
        template.binding("implementsName", entityClass+"Service");
        template.binding("daoPackage", daoPkg);
        template.binding("package", pkg);
        template.binding("entityClass", entityClass);
        template.binding("daoClassVar", StringUtils.uncapitalize(entityClass) + "Dao");
        template.binding("modelClassVar", StringUtils.uncapitalize(entityClass));

        String CR = System.getProperty("line.separator");
        String mapperHead = "import " + entityPkg + "."+entityClass+";" + CR;
        template.binding("imports", mapperHead);

        String mapperHeadService = "import " + servicePkg + "."+entityClass+"Service;" + CR;
        template.binding("importsService", mapperHeadService);
        String mapperCode = template.render();
        if (isDisplay) {
            System.out.println();
            System.out.println(mapperCode);
        } else {
            try {
//                SourceGen.saveSourceFile(GenKit.getJavaSRCPath(), pkg, mapperClass, mapperCode);
                SourceGen.saveSourceFile(outPath, pkg, mapperClass, mapperCode);
                System.out.println(mapperClass+"生成成功");
            } catch (IOException e) {
                throw new RuntimeException("serviceImpl代码生成失败", e);
            }
        }

    }

}
