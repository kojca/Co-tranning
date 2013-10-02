/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cf.heuristic.simi.pearson;

import java.sql.SQLException;
import java.util.List;
import model.DataModel;
import model.UserCorrelation;
import similarity.Similarity;

/**
 *  Tinh do tuong tu
 *  
 */
public class UserSimilarity implements Similarity {

    private int userIDA;
    private int userIDB;
    private List commonItems;
    DataModel model;
    private float avgA;
    private float avgB;
    private float[][] data;

    public UserSimilarity(DataModel model) throws SQLException {
        this.model = model;
    }

    public float getCorrelationValue() throws SQLException {
        float value;

        float pA, pB;
        float denominatorA = 0;
        float denominatorB = 0;
        float numerator = 0;
        int item;
        for (int i = 0; i < commonItems.size(); i++) {
            item = (Integer) commonItems.get(i);
            pA = (Float) data[userIDA][item] - avgA;  // = (rix - ri)
            pB = (Float) data[userIDB][item] - avgB;  // = (rjx- rj)
            numerator += pA * pB;
            denominatorA += pA * pA;
            denominatorB += pB * pB;
        }

        if (numerator == 0) {
            value = 0;
        } else {
            value = numerator / (float) Math.sqrt(denominatorA * denominatorB);
        }

        return value;
    }
    // tinh r(trung binh) cua nguoi dung a va nguoi dung b

    private void init(int userIDA, int userIDB) throws SQLException {
        this.userIDA = userIDA;
        this.userIDB = userIDB;

        data = model.getData();
        float valueA = 0;
        float valueB = 0;
        int itemId;

        for (int i = 0; i < commonItems.size(); i++) {
            itemId = (Integer) commonItems.get(i);
            valueA += (Float) data[userIDA][itemId];
            valueB += (Float) data[userIDB][itemId];
        }
        this.avgA = valueA / commonItems.size();
        this.avgB = valueB / commonItems.size();
    }

    public UserCorrelation getCorrelation(int a, int b, int gamma) throws SQLException {
        // Lay ve danh sach cac phan tu cung co chung nhan xet
        float value;
        commonItems = model.getConjItems(userIDA, userIDB);
        // Neu so phan tu chung nhau qua it thi gan do tuong tu = 0 luon
        // ko can xem xet tinh toan gi them
        if (commonItems.size() < gamma) {
            value = 0;
        } else {
            init(a, b);
            value = getCorrelationValue();
        }
        UserCorrelation uc = new UserCorrelation(userIDA, userIDB, value);
        return uc;
    }
}
