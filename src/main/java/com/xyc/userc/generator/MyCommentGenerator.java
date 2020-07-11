package com.xyc.userc.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.DefaultCommentGenerator;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.Properties;

/**
 * Created by 1 on 2020/3/18.
 * 自定义注释生成器类，用于自定义generator生成的实体类中的字段添加注释
 */
public class MyCommentGenerator extends DefaultCommentGenerator
{
    private boolean suppressAllComments;

    private boolean addRemarkComments;

    //获取用户设置的commentGenerator标签中的property参数
    @Override
    public void addConfigurationProperties(Properties properties)
    {
        super.addConfigurationProperties(properties);
        suppressAllComments = "true".equals(properties.getProperty(
                PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS
        )) ? true : false;

        addRemarkComments = "true".equals(properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_ADD_REMARK_COMMENTS)) ? true : false;
    }

    //重写生成的实体类中字段的注释
    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn)
    {
        //若配置了阻止生成所有注释，则直接返回
        if(suppressAllComments)
        {
            return;
        }
        //文档注释开始
        field.addJavaDocLine("/**");
        //获取数据库表字段备注信息
        String remarks = introspectedColumn.getRemarks();
        //若addRemarkComments为true且remarks不为空则添加备注信息
        if(addRemarkComments && StringUtility.stringHasValue(remarks))
        {
            String[] remarkLines = remarks.split(System.getProperty("line.separator"));
            for(String remarkLine : remarkLines)
            {
                field.addJavaDocLine(" * " + remarkLine);
            }
        }
        //注释中保留数据库字段名
        field.addJavaDocLine(" * " + introspectedColumn.getActualColumnName());
        field.addJavaDocLine(" */");
    }

    //重写生成的mapper接口方法注释
    @Override
    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable)
    {
    }

    //重写生成的实体类字段getter方法注释
    @Override
    public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn)
    {
    }

    //重写生成的实体类字段setter方法注释
    @Override
    public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn)
    {
    }

    //重写生成的xml映射文件节点的注释
    @Override
    public void addComment(XmlElement xmlElement) {
    }
}
