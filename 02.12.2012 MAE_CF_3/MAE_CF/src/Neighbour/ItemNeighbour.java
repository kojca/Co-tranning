/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Neighbour;

import datasource.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import model.DataModel;
import model.ItemCorrelation;
import similarity.Similarity;
import cf.heuristic.simi.pearson.ItemSimilarity;

/**
 *
 * 
 */
public class ItemNeighbour implements Neighbour {

    private Similarity simi;
    private DataModel model;
    private int userID;
    private int itemID;

    public ItemNeighbour(Similarity simi, DataModel model, int userID, int itemID) {
        this.simi = simi;
        this.model = model;
        this.userID = userID;
        this.itemID = itemID;
    }

    public List<ItemCorrelation> getKNeighbour(int k, int gamma) throws SQLException {
        List items = model.getItemsForUser(userID, itemID);
        List<ItemCorrelation> ic = new ArrayList<ItemCorrelation>();
        int itID;
        for (int i = 0; i < items.size(); i++) {
            itID = (Integer) items.get(i);
            ItemCorrelation itemCorr = (ItemCorrelation) simi.getCorrelation(itemID, itID, gamma);
            // Chi lay cac item co do tuong tu khac 0
            if (itemCorr.getCorrelation() != 0) {
                ic.add(itemCorr);
            }
        }

        // Sap xep lai tap hang xom
        Collections.sort(ic, new CorrComparator());
        // Lay ve tap gom k phan tu co do tuong tu cao nhat
        if (k < ic.size()) {
            ic = ic.subList(0, k - 1);
        }
        return ic;
    }

    class CorrComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            ItemCorrelation uc1 = (ItemCorrelation) o1;
            ItemCorrelation uc2 = (ItemCorrelation) o2;

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
