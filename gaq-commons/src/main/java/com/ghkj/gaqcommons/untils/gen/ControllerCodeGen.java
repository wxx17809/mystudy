package com.ghkj.gaqcommons.untils.gen;


import org.apache.commons.lang3.StringUtils;
import org.beetl.core.Template;
import org.beetl.sql.core.db.TableDesc;
import org.beetl.sql.ext.gen.CodeGen;
import org.beetl.sql.ext.gen.GenConfig;
import org.beetl.sql.ext.gen.SourceGen;

import java.io.IOException;

public class ControllerCodeGen implements CodeGen {
    String outPath = "";
    String pkg = null;
    String servicePkg = null;
    String mapperTemplate = "";

    public ControllerCodeGen() {
    }

    public ControllerCodeGen(String pkg, String servicePkg, String templatePath, String outPath) {
        this.pkg = pkg;
        this.mapperTemplate = templatePath;
        this.servicePkg = servicePkg;
        this.outPath = outPath;
    }

    @Override
    public void genCode(String entityPkg, String entityClass, TableDesc tableDesc, GenConfig config, boolean isDisplay) {
        if (pkg == null) {
            pkg = entityPkg;
        }
        Template template = SourceGen.getGt().getTemplate(mapperTemplate);
        String mapperClass = entityClass + "Controller";
        template.binding("tableRemark", tableDesc.getRemark());
        template.binding("className", mapperClass);
        template.binding("servicePackage", servicePkg);
        template.binding("package", pkg);
        template.binding("entityClass", entityClass);
        template.binding("serviceClassVar", StringUtils.uncapitalize(entityClass) + "Service");
        template.binding("modelClassVar", StringUtils.uncapitalize(entityClass));
        template.binding("basePath", StringUtils.uncapitalize(entityClass));
        String CR = System.getProperty("line.separator");
        String mapperHead = "import " + entityPkg + ".*;" + CR;
        template.binding("imports", mapperHead);
        String mapperCode = template.render();
        if (isDisplay) {
            System.out.println();
            System.out.println(mapperCode);
        } else {
            try {
                SourceGen.saveSourceFile(outPath, pkg, mapperClass, mapperCode);
                System.out.println(mapperClass+"生成成功");
            } catch (IOException e) {
                throw new RuntimeException("controller代码生成失败", e);
            }
        }

    }

}
