package org.ydzzz;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataBase {
    public static DataBaseInform dataBaseInform = null;

    /**
     * 按配置文件初始化
     */
    public DataBase(){
        dataBaseInform = new DataBaseInform();
    }

    /**
     * 输入url，username，password初始化数据库信息
     * @param url
     * @param username
     * @param password
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public DataBase(String url, String username, String password) throws SQLException, ClassNotFoundException {
        dataBaseInform = new DataBaseInform(url, username, password);
    }


    public <E> boolean search(E e){
        try {
            Class<?> a = e.getClass();
            Class<?> b = a.getSuperclass();
            b.getDeclaredField("tableName");
        } catch (NoSuchFieldException ex) {
            System.out.println("sorry,the object you send in must extends by DataBaseObject :(");
        }
        String s = e.getClass().getName().toString();
        ArrayList<String> arrayName = new ArrayList<>();
        Field[] fields = e.getClass().getDeclaredFields();
        for (Field field : fields) {
                arrayName.add(field.getName());
        }
        arrayName.forEach((a)->{
            System.out.println(a);
        });
        return false;
    }
}
