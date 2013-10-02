/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package recommender;

import java.sql.SQLException;
import java.util.List;
import model.Preference;

/**
 *
 * 
 */
public interface Recommender {
    public List getRecommendations(int gamma) throws SQLException;
    public List getRecommendations(int userID, int gamma) throws SQLException;
    public Preference estimatePreference (int userID,int itemID, int gamma) throws SQLException;

}
