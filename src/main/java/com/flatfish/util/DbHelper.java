package com.flatfish.util;

import java.sql.Connection;
import java.sql.DriverManager;

public final class DbHelper {


    public static Connection getConnection(String url, String useName, String password) {
        Connection con = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = (Connection) DriverManager.getConnection(url, useName, password);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return con;
    }
}
