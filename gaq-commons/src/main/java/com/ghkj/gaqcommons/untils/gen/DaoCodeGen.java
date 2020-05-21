package com.ghkj.gaqcommons.untils.gen;

import org.beetl.core.Template;
import org.beetl.sql.core.db.TableDesc;
import org.beetl.sql.ext.gen.CodeGen;
import org.beetl.sql.ext.gen.GenConfig;
import org.beetl.sql.ext.gen.SourceGen;

import java.io.IOException;

public class DaoCodeGen implements CodeGen {
    String outPath = "";
    String pkg = null;
    String entityPkg = null;
    String mapperTemplate = "";

    public DaoCodeGen() {
    }

    public DaoCodeGen(String pkg, String entityPkg, String templatePath,String outPath) {
        this.pkg = pkg;
        this.mapperTemplate = templatePath;
        this.entityPkg = entityPkg;
        this.outPath = outPath;
    }
    @Override
    public void genCode(String entityPkg, String entityClass, TableDesc tableDesc, GenConfig config, boolean isDisplay) {
        if (pkg == null) {
            pkg = entityPkg;
        }
        Template template = SourceGen.getGt().getTemplate(mapperTemplate);
        String mapperClass = entityClass + "Mapper";
        template.binding("className", mapperClass);
        template.binding("entityClass", entityClass);
        template.binding("package", pkg);
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
                throw new RuntimeException("dao代码生成失败", e);
            }
        }
    }
}
