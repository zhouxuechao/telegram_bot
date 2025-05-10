package com.example.demo.config;


import java.sql.*;

public class MySqlConnection {

    // 数据库连接参数
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3307/ry2";
    private static final String USERNAME = "ry2";
    private static final String PASSWORD = "123456";

    private MySqlConnection() {}

    // 获取数据库连接实例
    public static Connection getInstance() throws ClassNotFoundException, SQLException {
        Class.forName(DRIVER);
        DriverManager.setLoginTimeout(10);
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static String[] chaxunkaijianghaoma(){
        Connection connection = null;
        String[] number = new String[2];
        try {
            // 获取数据库连接实例
            connection = MySqlConnection.getInstance();
            // 执行SQL语句
            String sql = "select lottery_number,lssue_number from kaijiang where status = 1 ORDER BY id desc limit 1 ";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                // 处理查询结果
                number[0] =  rs.getString("lottery_number");
                number[1] =  rs.getString("lssue_number");
            }
            pstmt.close();
            return number;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭数据库连接
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String chaxunjihuahaoma(){
        Connection connection = null;
        String number = null;
        try {
            // 获取数据库连接实例
            connection = MySqlConnection.getInstance();
            // 执行SQL语句
            String sql = "select CONCAT(stage,\" 号码：\",number,\" | \",(case status when '0' then '等开' when '1' then '中' when '2' then '错' end) ) number from kaijiangjihua ORDER BY id desc limit 1 ";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                // 处理查询结果
                number =  rs.getString("number");
            }
            // 关闭Statement对象
            pstmt.close();
            return  number;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭数据库连接
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String chaxundangqianqihao(){
        Connection connection = null;
        String number = null;
        try {
            // 获取数据库连接实例
            connection = MySqlConnection.getInstance();
            // 执行SQL语句
            String sql = "select lssue_number from kaijiang where status = 0 ORDER BY id asc limit 1 ";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                // 处理查询结果
                number =  rs.getString("lssue_number");
            }
            pstmt.close();
            return number;
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭数据库连接
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(chaxunjihuahaoma());
        System.out.println(chaxundangqianqihao());
        System.out.println(chaxunkaijianghaoma()[0]);
        System.out.println(chaxunkaijianghaoma()[1]);
    }
}
