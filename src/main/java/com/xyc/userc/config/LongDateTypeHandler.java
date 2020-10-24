package com.xyc.userc.config;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

/**
 * Created by 1 on 2020/10/24.
 */
@MappedTypes(java.lang.Long.class)
@MappedJdbcTypes(JdbcType.DATE)
public class LongDateTypeHandler implements TypeHandler<Long>
{
    @Override
    public void setParameter(PreparedStatement ps, int i, Long parameter, JdbcType jdbcType) throws SQLException
    {
        Date date = new Date(parameter);
        ps.setDate(i, date);
    }

    @Override
    public Long getResult(ResultSet rs, String columnName) throws SQLException
    {
        return rs.getDate(columnName).getTime();
    }

    @Override
    public Long getResult(ResultSet rs, int columnIndex) throws SQLException
    {
        return rs.getDate(columnIndex).getTime();
    }

    @Override
    public Long getResult(CallableStatement cs, int columnIndex) throws SQLException
    {
        return null;
    }

}
