/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datasource;


import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * 
 */
public class test {
    public static void main(String args[]) throws SQLException{
        Connection con=new DataSource().getConnection();
        DatabaseMetaData mtdata = con.getMetaData();
        String catalog = con.getCatalog();
        ResultSet rs;
        rs=mtdata.getImportedKeys(catalog, null, "ratings");
//         rs=mtdata.getExportedKeys(catalog, null, "ratings");
        while (rs.next()) {
            System.out.println(rs.getString(3)+" "+rs.getString(4)+" "+rs.getString(7)+" "+rs.getString(8));
        }
    }
}
