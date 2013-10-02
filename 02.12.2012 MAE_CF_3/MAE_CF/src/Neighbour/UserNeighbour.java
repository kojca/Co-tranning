/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Neighbour;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import model.DataModel;
import model.UserCorrelation;
import similarity.Similarity;

/**
 * Lay ve tap hang xom
 * 
 */
public class UserNeighbour implements Neighbour {

    private Similarity simi;
    private DataModel model;
    private int userID;
    private int itemID;

    public UserNeighbour(Similarity simi, DataModel model, int userID, int itemID) {
        this.simi = simi;
        this.model = model;
        this.userID = userID;
        this.itemID = itemID;
    }

    // Lay ve tap danh sach hang xom
    public List<UserCorrelation> getKNeighbour(int k, int gamma) throws SQLException {
        List users = model.getUserForItem(itemID, userID);
        List<UserCorrelation> us = new ArrayList<UserCorrelation>();
        int uId;
        for (int i = 0; i < users.size(); i++) {
            uId = (Integer) users.get(i);
            UserCorrelation userCorr = (UserCorrelation) simi.getCorrelation(userID, uId, gamma);
            // Tap hang xom se~ chi bao gom user co do tuong tu khac 0
            // Tuc la cac user thoa man nguo~ng gamma
            if (userCorr.getCorrelation() != 0) {
                //add user co do tuong tu vao trong tap hang xom
                us.add(userCorr);
            }
        }
        // Sap xep lai tap hang xom voi
        Collections.sort(us, new CorrComparator());
        // Lay ve tap gom k phan tu co do tuong tu cao nhat
        if (k < us.size()) {
            us = us.subList(0, k);
        }
        return us;
    }

    // So sanh va sap xep doi tuong trong list
    class CorrComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            UserCorrelation uc1 = (UserCorrelation) o1;
            UserCorrelation uc2 = (UserCorrelation) o2;

            if (uc1.getCorrelation() > uc2.getCorrelation()) {
                return -1;
            } else if (uc1.getCorrelation() < uc2.getCorrelation()) {
                return 1;
            } else {
                return 0;
            }
        }
    }
}
