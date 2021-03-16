package com.xyc.userc.mybatis.typehandler;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;
import org.apache.tomcat.jni.Local;

import java.security.*;
import java.sql.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

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
        Timestamp timestamp = rs.getTimestamp(columnName);
        if(timestamp != null)
        {
            return timestamp.getTime();
        }
        return null;
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
