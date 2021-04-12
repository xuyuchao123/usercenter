package com.xyc.userc.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * Created by 1 on 2021/4/12.
 */
public class MybatisConfig
{
    @Configuration
    @MapperScan(
            basePackages = "com.xyc.userc.dao",
            sqlSessionTemplateRef = "sqlSessionTemplate")
    public static class Db1
    {

        @Bean
        @Primary
        public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception
        {
            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(dataSource);
            sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/mapper/*.xml"));
            return sqlSessionFactoryBean.getObject();
        }

        @Bean
        @Primary
        public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception
        {
            return new SqlSessionTemplate(sqlSessionFactory);
        }

        @Bean
        @Primary
        public DataSourceTransactionManager dataSourceTransactionManager(@Qualifier("dataSource") DataSource dataSource)
        {
            return new DataSourceTransactionManager(dataSource);
        }
    }

    @Configuration
    @MapperScan(
            basePackages = "com.xyc.userc.dao2",
            sqlSessionTemplateRef = "sqlSessionTemplate2")
    public static class Db2
    {
        @Bean
        public SqlSessionFactory sqlSessionFactory2(@Qualifier("dataSource2") DataSource dataSource2) throws Exception
        {
            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(dataSource2);
            sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/mapper2/*.xml"));
            return sqlSessionFactoryBean.getObject();
        }

        @Bean
        public SqlSessionTemplate sqlSessionTemplate2(@Qualifier("sqlSessionFactory2") SqlSessionFactory sqlSessionFactory) throws Exception
        {
            return new SqlSessionTemplate(sqlSessionFactory);
        }

        @Bean
        public DataSourceTransactionManager dataSourceTransactionManager2(@Qualifier("dataSource2") DataSource dataSource2)
        {
            return new DataSourceTransactionManager(dataSource2);
        }
    }

}
