/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import datasource.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *  Thuc hien lay du lieu tu database
 * 
 */
public class DataModel {

    private Connection con;
    private DataSource dataSource;
    private String preferenceTable;
    private String userIDColumn;
    private String itemIDColumn;
    private String preferenceColumn;
    private String userTable;
    private String contentTable;
    private List<String> contentColumns;
    private String keyUserTable;
    private String keyItemTable;
    float[][] data;
  
    int itemNum;
    int userNum;
 


     public DataModel(DataSource dataSource, String preferenceTable, String userTable, String itemTable, String userIDColumn, String itemIDColumn, String preferenceColumn) throws SQLException, Exception {
        con = dataSource.getConnection();
        this.dataSource = dataSource;
        this.userTable = userTable;
        this.contentTable = itemTable;
        this.preferenceTable = preferenceTable;
        this.userIDColumn = userIDColumn;
        this.itemIDColumn = itemIDColumn;
        this.preferenceColumn = preferenceColumn;
        System.out.println("Loading data....");
        init();
        System.out.println("Done !....");
    }

    public DataModel(Connection con, String preferenceTable, String userTable, String itemTable, String userIDColumn, String itemIDColumn, String preferenceColumn) throws SQLException, Exception {
        this.con = con;
        this.userTable = userTable;
        this.contentTable = itemTable;
        this.preferenceTable = preferenceTable;
        this.userIDColumn = userIDColumn;
        this.itemIDColumn = itemIDColumn;
        this.preferenceColumn = preferenceColumn;
        System.out.println("Loading data....");
        init();
        System.out.println("Done !....");
    }

    int getUserCount() throws SQLException {
        // SELECT max(userID) FROM users
        String sql = "SELECT max(" + keyUserTable + ") FROM " + userTable;
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(sql);
        int result = 0;
        while (rs.next()) {
            result = rs.getInt(1);
        }
        stm.close();
        rs.close();
        return result;
    }

    int getItemCount() throws SQLException {
        // SELECT max(movieId) FROM movies
        String sql = "SELECT max(" + keyItemTable + ") FROM " + contentTable;
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(sql);
        int result = 0;
        while (rs.next()) {
            result = rs.getInt(1);
        }
        stm.close();
        rs.close();
        return result;
    }

    private void init() throws SQLException, Exception {
;
        userNum= 943;
      //  System.out.println("So luong ban ghi trong bang users:" + userNum);

        itemNum = 1682;
    //    System.out.println("So luong ban ghi trong bang movies:" + itemNum);

        data = new float[userNum+1][itemNum+1];
        for (int i = 0; i <= userNum; i++) {
            for (int j = 0; j <= itemNum; j++) {
                data[i][j] = -1;
            }
        }
        // Select userId, movieId, rating  from ratings
        String sql = "select " + userIDColumn + ", " + itemIDColumn + ", " + preferenceColumn + " from " + preferenceTable + " ";
        Statement stm = con.createStatement();
        ResultSet rs = stm.executeQuery(sql);
        int userID, itemID;
        float pref;

        // Xay dung gia tri cho ma tran
        while (rs.next()) {
            userID = rs.getInt(1);
            itemID = rs.getInt(2);
            pref = rs.getFloat(3);
            try {
                data[userID][itemID] = pref;
            } catch (Exception e) {
//                e.printStackTrace();
//                System.out.println(userID + " " + itemID);
            }
        }
        stm.close();
        rs.close();
//        rs1.close();
//        rs2.close();
    }

    public float[][] getData() {
        return data;
    }

    public void setData(float[][] data) {
        this.data = data;
    }

    public int getItemNum() {
        return itemNum;
    }

    public int getUserNum() {
        return userNum;
    }

    public Connection getCon() {
        return con;
    }

    public List<String> getContentColumns() {
        return contentColumns;
    }

    //Lay ve danh sach cac Item chung nhau
    public List getConjItems(int userIDA, int userIDB) {


        List items = new ArrayList();

        try {
            for (int i = 1; i <= itemNum; i++) {
                if (data[userIDA][i] != -1 && data[userIDB][i] != -1) {
                    items.add(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;

    }
// tim tap Pi giao Pj

    public List getConjUsers(int itemIDA, int itemIDB) {

        List users = new ArrayList();

        try {
            for (int i = 1; i <= userNum; i++) {
                if (data[i][itemIDA] != -1 && data[i][itemIDB] != -1) {
                    users.add(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    public List getUserForItem(int itemID, int userID) {

        List users = new ArrayList();

        try {
            for (int i = 1; i <= userNum; i++) {
                if (data[i][itemID] != -1 && i != userID) {
                    users.add(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return users;
    }

    public List getItemsForUser(int userID, int itemID) {

        List items = new ArrayList();

        try {
            for (int i = 1; i <= itemNum; i++) {
                if (data[userID][i] != -1 && i != itemID) {
                    items.add(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    public List getNewItemsForUser(int userID) {
        //Connection con=null;

        List items = new ArrayList();

        try {
            for (int i = 1; i <= itemNum; i++) {
                if (data[userID][i] == -1) {
                    items.add(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<Preference> getAllPrefs() {

        Statement stmt = null;
        List<Preference> all = new ArrayList<Preference>();
        int userID;
        int itemID;
        float value;
        String getAllSQL = "SELECT " + userIDColumn + ", " + itemIDColumn + ", " + preferenceColumn + " FROM " + preferenceTable + " ORDER BY " + userIDColumn + ", " + itemIDColumn;
        try {
            stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(getAllSQL);

            while (rs.next()) {
                userID = rs.getInt(1);
                itemID = rs.getInt(2);
                value = rs.getFloat(3);
                all.add(new Preference(userID, itemID, value));
            }

            stmt.close();
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return all;
    }

}
