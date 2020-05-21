package com.ghkj.gaqcommons.untils.gen;


import org.apache.commons.lang3.StringUtils;
import org.beetl.core.Template;
import org.beetl.sql.core.db.TableDesc;
import org.beetl.sql.core.kit.GenKit;
import org.beetl.sql.ext.gen.CodeGen;
import org.beetl.sql.ext.gen.GenConfig;
import org.beetl.sql.ext.gen.SourceGen;

import java.io.IOException;

public class ServiceCodeGen implements CodeGen {
    String outPath = "";
    String pkg = null;
    String mapperTemplate = "";

    public ServiceCodeGen() {
    }

    public ServiceCodeGen(String pkg, String templatePath, String outPath) {
        this.pkg = pkg;
        this.mapperTemplate = templatePath;
        this.outPath = outPath;
    }

    @Override
    public void genCode(String entityPkg, String entityClass, TableDesc tableDesc, GenConfig config, boolean isDisplay) {
        if (pkg == null) {
            pkg = entityPkg;
        }
        Template template = SourceGen.getGt().getTemplate(mapperTemplate);
        String mapperClass = entityClass + "Service";
        template.binding("className", mapperClass);
        template.binding("package", pkg);
        template.binding("entityClass", entityClass);
        template.binding("modelClassVar", StringUtils.uncapitalize(entityClass));

        String CR = System.getProperty("line.separator");
        String mapperHead = "import " + entityPkg + ".*;" + CR;
        template.binding("imports", mapperHead);
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
                throw new RuntimeException("service代码生成失败", e);
            }
        }

    }

}
