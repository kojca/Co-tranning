/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;


import datasource.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */
public class TestModel {
    Connection con;
    List<Preference> data;
    List item;
    List user;

    public TestModel(Connection con) throws SQLException {
        this.con = con;
        this.user= new ArrayList();
        this.item=new ArrayList();
        this.data=retrieveData();
    }

    public Connection getCon() {
        return con;
    }

    public void setCon(Connection con) {
        this.con = con;
    }

    public List<Preference> getData() {
        return data;
    }



    private List<Preference> retrieveData() throws SQLException{
        String getItem="select distinct(movieId) from test2";
        String getUser="select distinct(userId) from test2";
        String getData="select userId,movieId,rating from test2 order by userId,movieId";
        Statement stm= con.createStatement();
        ResultSet rs=null;

        rs=stm.executeQuery(getUser);
        while(rs.next()){
            int uId=rs.getInt(1);
            user.add(uId);
        }

        rs=stm.executeQuery(getItem);
        while(rs.next()){
            int iId=rs.getInt(1);
            item.add(iId);
        }


        List<Preference> l= new ArrayList<Preference>();
        rs=stm.executeQuery(getData);
        while(rs.next()){
            int uId=rs.getInt(1);
            int iId=rs.getInt(2);
            float rate= rs.getFloat(3);
            Preference p=new Preference(uId,iId,rate);
            l.add(p);
        }

        return l;
    }

  
}
