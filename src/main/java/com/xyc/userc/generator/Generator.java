package com.xyc.userc.generator;

import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 2020/3/18.
 * 代码生成器启动类
 */
public class Generator
{
    public static void main(String[] args) throws Exception
    {
        //存放generator执行过程中的警告信息
        List<String> warnings = new ArrayList<String>();
        //当生成代码重复时，覆盖原代码
        boolean overwrite = true;
        InputStream inputStream = Generator.class.getResourceAsStream("/generator/generatorConfig.xml");
        ConfigurationParser configurationParser = new ConfigurationParser(warnings);
        Configuration configuration = configurationParser.parseConfiguration(inputStream);
        inputStream.close();

        DefaultShellCallback callback = new DefaultShellCallback(overwrite);
        //创建generator
        MyBatisGenerator myBatisGenerator = new MyBatisGenerator(configuration,callback,warnings);
        //执行生成代码
        myBatisGenerator.generate(null);
        //输出警告信息
        for(String warning : warnings)
        {
            System.out.println(warning);
        }
    }

}
