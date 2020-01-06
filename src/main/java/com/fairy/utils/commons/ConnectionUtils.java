package com.fairy.utils.commons;

import java.sql.Connection;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ConnectionUtils
{
    private static DataSource dataSource;
    public static String getTableSchema() {
        try(Connection conn = dataSource.getConnection())
        {
            String url = conn.getMetaData().getURL();
            return url.substring(url.lastIndexOf("/")+1, url.indexOf("?"));
        }
        catch (Exception e)
        {
            log.error("",e);
        }
        return null;
    }
    
    @Autowired
    public void setDataSource(DataSource dataSource) {
        ConnectionUtils.dataSource = dataSource;
    }
}
