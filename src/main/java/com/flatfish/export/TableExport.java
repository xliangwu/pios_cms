package com.flatfish.export;

import com.flatfish.util.DbHelper;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TableExport {

    public static void main(String[] args) {
        TableExport export = new TableExport();
        export.exportPassAtToday("02", 10, "jdbc:sqlserver://localhost:1433;DatabaseName=pios", "user_test", "cave321");
    }

    public List<String> exportPassAtToday(String machineCode, int startHour, String url, String userName, String password) {
        LocalDate today = LocalDate.now();
        LocalDateTime todayTime = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), startHour, 0, 0);
        LocalDateTime yesterdayTime = todayTime.minusDays(1);
        Timestamp startDate = Timestamp.valueOf(yesterdayTime);
        Timestamp endDate = Timestamp.valueOf(todayTime);
        return exportPass(machineCode, startDate, endDate, url, userName, password);
    }

    public List<String> exportPass(String machineCode, Timestamp startDate, Timestamp endDate, String url, String userName, String password) {
        Connection con = DbHelper.getConnection(url, userName, password);

        if (null == con) {
            System.out.println("连接数据库失败，请检查用户名和密码");
        }

        List<String> output = new ArrayList<String>();
        String query = "select cardNumber,inTime,outTime from PassRecord where reserve4!='临时车牌' and (intime between  ? and ? or outTime between  ? and ?)";
        try {
            System.out.println("Start date :" + startDate);
            System.out.println("End date :" + endDate);

            PreparedStatement stmt = con.prepareStatement(query);
            stmt.setTimestamp(1, startDate);
            stmt.setTimestamp(2, endDate);
            stmt.setTimestamp(3, startDate);
            stmt.setTimestamp(4, endDate);

            ResultSet rs = stmt.executeQuery();

            SimpleDateFormat fmt = new SimpleDateFormat("YYYYMMddHHmmss");
            String line = null;
            while (rs.next()) {
                String cardNumber = rs.getString("cardNumber");
                Timestamp inTime = rs.getTimestamp("inTime");
                Timestamp outTime = rs.getTimestamp("outTime");

                System.out.println(cardNumber + "," + inTime + "," + outTime);

                if (null != inTime) {
                    line = machineCode + fmt.format(inTime) + cardNumber;
                    output.add(line);
                }

                if (null != outTime) {
                    line = machineCode + fmt.format(outTime) + cardNumber;
                    output.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
            }
        }
        System.out.println("记录数据 :" + output.size());
        return output;
    }
}
