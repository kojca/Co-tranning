/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Neighbour;

import java.sql.SQLException;
import java.util.List;

/**
 *
 * 
 */
public interface Neighbour {
    public List getKNeighbour(int k , int gamma)throws SQLException;
}
