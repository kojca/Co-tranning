/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package similarity;

import java.sql.SQLException;
import model.UserCorrelation;

/**
 *
 * 
 */
public interface Similarity {
    // gamma la nguong, yeu cau phai co toi thieu bao nhieu danh gia giong nhau
    public Object getCorrelation(int a,int b,int gamma) throws SQLException;
}
