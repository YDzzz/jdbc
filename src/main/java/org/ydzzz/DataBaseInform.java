package org.ydzzz;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseInform {
    private enum __DATABASE {Mysql,H2}
    private Connection connection = null;
    private String Password = null;
    private String UserName = null;
    private String Url = null;
    private String Catalog = null;
    private __DATABASE DatabaseType = null;
    private boolean State = false;

    /**
     * 检查是否已经建立正确的Connection连接
     * @return Connection对象
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public boolean check() throws SQLException, ClassNotFoundException {
        switch (DatabaseType){
            case H2 -> Class.forName("org.h2.Driver");
            case Mysql -> Class.forName("com.mysql.cj.jdbc.Driver");
        }
        connection = DriverManager.getConnection(Url, UserName, Password);
        if (connection.isValid(0)) {
            State = true;
            System.out.println("jdbc connect succeed! :)");
        } else {
            State = false;
            System.out.println("jdbc connect error! :(");
        }
        connection.close();
        return State;
    }

    /**
     * 使用参数构造对象
     * @param url
     * @param userName
     * @param password
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public DataBaseInform(String url, String userName, String password) throws ClassNotFoundException, SQLException {
        Password = password;
        UserName = userName;
        Url = url;
        update();
    }

    /**
     * 使用配置文件构造对象
     */
    public DataBaseInform() {
        InputStream inputStream = DataBaseInform.class.getClassLoader().getResourceAsStream("jdbc.properties");
        if (inputStream == null){
            System.out.println("there is no properties file!");
        } else{
            Properties properties = new Properties();
            try {
                properties.load(inputStream);
                Url = properties.getProperty("jdbc.url",null);
                Password = properties.getProperty("jdbc.password",null);
                UserName = properties.getProperty("jdbc.username",null);
            } catch (IOException e) {
                System.out.println("properties file error! :(");
            }
            try {
                update();
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     *返回数据库的Connection对象
     * @return Connection对象
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     *
     * @return 连接状态
     */
    public boolean isState() {
        return State;
    }

    /**
     *
     * @return 数据库的密码
     */
    public String getPassword() {
        return Password;
    }

    /**
     *
     * @return 数据库连接名
     */
    public String getUserName() {
        return UserName;
    }

    /**
     *
     * @return 数据库连接链接
     */
    public String getUrl() {
        return Url;
    }

    /**
     * 用来设置数据库密码
     * @param password
     */
    public void setPassword(String password){
        Password = password;
    }

    /**
     * 用来设置数据库用户名
     * @param userName
     */
    public void setUserName(String userName) {
        UserName = userName;
    }

    /**
     * 用来设置数据库的url
     * @param url
     */
    public void setUrl(String url) {
        Url = url;
    }

    /**
     * 用来获得数据库的名字
     * @return String
     */
    public String getCatalog() {
        return Catalog;
    }

    /**
     * 用来更新Connection链接，修改数据库密码、用户名、url后使用该函数使配置生效
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    protected void update() throws SQLException, ClassNotFoundException {
        // 通过url检测数据库类型
        if (Url.contains("mysql"))   DatabaseType = __DATABASE.Mysql;
        else if (Url.contains("h2")) DatabaseType = __DATABASE.H2;

        // 检查数据库连接信息是否正确
        if (!check())  {Catalog = null; return;}

        // 获取数据库名
        connection = DriverManager.getConnection(Url, UserName, Password);
        switch (DatabaseType){
            case H2 -> Catalog = connection.getSchema();
            case Mysql -> Catalog = connection.getCatalog();
        }
    }
}
