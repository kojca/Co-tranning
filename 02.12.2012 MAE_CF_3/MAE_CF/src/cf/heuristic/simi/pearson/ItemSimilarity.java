/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cf.heuristic.simi.pearson;

import java.sql.SQLException;
import java.util.List;
import model.DataModel;
import model.ItemCorrelation;
import similarity.Similarity;

/**
 *
 *
 */
public class ItemSimilarity implements Similarity {

    private int itemIDA;
    private int itemIDB;
    private List commonUsers;
    private DataModel model;
    private float avgA, avgB;
    private float[][] data;

    public ItemSimilarity(DataModel model) {
        this.model = model;
    }

    private void init(int itemIDA, int itemIDB) {
        this.itemIDA = itemIDA;
        this.itemIDB = itemIDB;
        float valueA = 0;
        float valueB = 0;
        int userId;
        data = model.getData();


        for (int i = 0; i < commonUsers.size(); i++) {
            userId = (Integer) commonUsers.get(i);
            valueA += (Float) data[userId][itemIDA];
            valueB += (Float) data[userId][itemIDB];
        }
        this.avgA = valueA / commonUsers.size();
        this.avgB = valueB / commonUsers.size();
    }

    public ItemCorrelation getCorrelation(int a, int b, int gamma) throws SQLException {
        float value;

        float pA, pB;
        float denominatorA = 0;
        float denominatorB = 0;
        float numerator = 0;
        int user;

        // Lay ve list item co chung nhan xet
        commonUsers = model.getConjUsers(itemIDA, itemIDB);
        // Neu it qua thi do tuong tu = 0 luon ko can tinh toan gi them
        if (commonUsers.size() < gamma) {
            value = 0;
        } else {
            init(a, b);

            for (int i = 0; i < commonUsers.size(); i++) {
                user = (Integer) commonUsers.get(i);
                pA = (Float) data[user][itemIDA] - avgA;
                pB = (Float) data[user][itemIDB] - avgB;
                numerator += pA * pB;
                denominatorA += pA * pA;
                denominatorB += pB * pB;
            }

            if (numerator == 0) {
                value = 0;
            } else {
                value = numerator / (float) Math.sqrt(denominatorA * denominatorB);
            }
        }
        ItemCorrelation uc = new ItemCorrelation(itemIDA, itemIDB, value);
        return uc;
    }
}
