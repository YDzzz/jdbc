package org.ydzzz;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataBase {
    private DataBaseInform inform = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet rs = null;
    private String TableName = null;
    private ArrayList<String> columnNameList = null;
    private String selectSql = null;
    private String[] selectColumnNameSql = null;
    private String selectGroupSql = null;
    private String selectWhereSql = null;
    private String selectOrderBySql = null;
    private String insertSql = null;
    private String insertColumnSql = null;
    private String insertValuesSql = null;
    private String deleteSql = null;
    private String deleteWhereSql = null;
    private String updateSql = null;
    private String updateSetSql = null;
    private String updateWhereSql = null;


    public DataBase(String url, String userName, String password, String TableName)
            throws SQLException, ClassNotFoundException {
        inform = new DataBaseInform(url, userName, password);
        this.TableName = TableName;
        String sqlString = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '"
                + inform.getCatalog() + "' AND TABLE_NAME = '" + this.TableName + "';";
        preparedStatement = inform.getConnection().prepareStatement(sqlString);
        rs = preparedStatement.executeQuery();
        columnNameList = new ArrayList<String>();
        while (rs.next()) {
            columnNameList.add(rs.getString("COLUMN_NAME"));
        }
        preparedStatement.close();
        rs.close();
    }

    public DataBase(String TableName) {
        inform = new DataBaseInform();
        this.TableName = TableName;
    }

    public void updateDatabaseInformation() throws ClassNotFoundException, SQLException {
        inform.update();
    }

    public String getTableName() {
        return this.TableName;
    }

    public ArrayList<String> getColumnNameList() {
        return this.columnNameList;
    }

    public String getSelectSql() {
        return this.selectSql;
    }

    public void setSelectSql(String selectSql) {
        this.selectSql = selectSql;
    }

    public String getDeleteSql() {
        return this.deleteSql;
    }

    public void setDeleteSql(String deleteSql) {
        this.deleteSql = deleteSql;
    }

    public String getUpdateSql() {
        return this.updateSql;
    }

    public void setUpdateSql(String updateSql) {
        this.updateSql = updateSql;
    }

    public void change() throws SQLException {
        this.updateSql = new String("UPDATE " + inform.getCatalog() + "." + this.TableName + " SET " + this.updateSetSql);
        if (this.updateWhereSql != null) {
            this.updateSql += " WHERE " + this.updateWhereSql;
        }
        this.updateSql += ";";
        this.update(updateSql);
    }

    public void setupdateSetSql(String updateSetSql) {
        this.updateSetSql = updateSetSql;
    }

    public void setUpdateWhereSql(String updateWhereSql) {
        this.updateWhereSql = updateWhereSql;
    }

    public void insert() throws SQLException {
        this.insertSql = new String("INSERT INTO " + inform.getCatalog() + "." + this.TableName);
        if (this.insertColumnSql != null) {
            this.insertSql += "(" + this.insertColumnSql + ")";
        }
        this.insertSql += " VALUES(" + this.insertValuesSql + ")";
        this.insertSql += ";";
        this.update(this.insertSql);
    }

    public String getInsertSql() {
        return this.insertSql;
    }

    public void setInsertSql(String insertSql) {
        this.insertSql = insertSql;
    }

    public void setInsertColumnSql(String insertColumnSql) {
        this.insertColumnSql = insertColumnSql;
    }

    public void setInsertValuesSql(String insertValuesSql) {
        this.insertValuesSql = insertValuesSql;
    }

    public void delete() throws SQLException {
        this.deleteSql = new String("DELETE FROM " + inform.getCatalog() + "." + this.TableName);
        if (deleteWhereSql != null) {
            this.deleteSql += " WHERE " + this.deleteWhereSql + ";";
        }
        this.update(this.deleteSql);
    }

    public void update(String sql) throws SQLException {
        if (preparedStatement != null) {
            preparedStatement.close();
        }
        preparedStatement = inform.getConnection().prepareStatement(sql);
        preparedStatement.executeUpdate();
        preparedStatement.close();
    }

    public void setDeleteWhereSql(String deleteWhereSql) {
        this.deleteWhereSql = deleteWhereSql;
    }

    public void setSelectGroupSql(String selectGroupSql) {
        this.selectGroupSql = selectGroupSql;
    }

    public ArrayList<String[]> select(String selectSql) throws SQLException {
        this.selectSql = selectSql;
        return selectUpdate();
    }

    public ArrayList<String[]> select() throws SQLException {
        this.selectSql = new String(
                "SELECT * FROM " + inform.getCatalog() + "." + this.TableName);
        if (this.selectGroupSql == null) {
            if (selectWhereSql != null) {
                this.selectSql += " WHERE " + this.selectWhereSql;
            }
        } else {
            this.selectSql += " GROUP BY " + this.selectGroupSql;
            if (selectWhereSql != null) {
                this.selectSql += " HAVING " + this.selectWhereSql;
            }
        }
        if (selectOrderBySql != null) {
            this.selectSql += " ORDER BY " + this.selectOrderBySql;
        }
        this.selectSql += ";";
        return selectUpdate();
    }

    private ArrayList<String[]> selectUpdate() throws SQLException {
        if (preparedStatement != null) {
            preparedStatement.close();
        }
        preparedStatement = inform.getConnection().prepareStatement(this.selectSql);
        if (rs != null) {
            rs.close();
        }
        rs = preparedStatement.executeQuery();
        ArrayList<String[]> a = new ArrayList<String[]>();
        if (selectColumnNameSql != null) {
            while (rs.next()) {
                String[] result = new String[selectColumnNameSql.length];
                for (int i = 0; i < selectColumnNameSql.length; i++) {
                    result[i] = rs.getString(selectColumnNameSql[i]);
                }
                a.add(result);
            }
        } else {
            while (rs.next()) {
                String[] result = new String[columnNameList.size()];
                for (int i = 0; i < columnNameList.size(); i++) {
                    result[i] = rs.getString(columnNameList.get(i));
                }
                a.add(result);
            }
        }

        return a;
    }

    public void setSelectWhereSql(String selectWhereSql) {
        this.selectWhereSql = selectWhereSql;
    }

    public void setSelectOrderBySql(String selectOrderBySql) {
        this.selectOrderBySql = selectOrderBySql;
    }

    public void setSelectColumnNameSql(String... selectColumnNameSql) {
        this.selectColumnNameSql = selectColumnNameSql;
    }
}
