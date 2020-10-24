package com.xyc.userc.mybatis.typehandler;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;

/**
 * Created by 1 on 2020/10/24.
 */
@MappedTypes(java.lang.Long.class)
@MappedJdbcTypes(JdbcType.TIMESTAMP)
public class LongDateTypeHandler implements TypeHandler<Long>
{

    public LongDateTypeHandler() {
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, Long parameter, JdbcType jdbcType) throws SQLException
    {
        Timestamp timestamp = new Timestamp(parameter);
        ps.setTimestamp(i, timestamp);
    }

    @Override
    public Long getResult(ResultSet rs, String columnName) throws SQLException
    {
        return rs.getTimestamp(columnName).getTime();
    }

    @Override
    public Long getResult(ResultSet rs, int columnIndex) throws SQLException
    {
        return rs.getTimestamp(columnIndex).getTime();
    }

    @Override
    public Long getResult(CallableStatement cs, int columnIndex) throws SQLException
    {
        return null;
    }

}
