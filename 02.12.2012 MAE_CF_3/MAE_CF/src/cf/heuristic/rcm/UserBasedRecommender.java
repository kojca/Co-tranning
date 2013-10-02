/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cf.heuristic.rcm;

import Neighbour.UserNeighbour;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import model.DataModel;
import model.Preference;
import model.UserCorrelation;
import recommender.Recommender;
import similarity.Similarity;

/**
 * Tinh so danh gia de dua ra tu van
 * 
 */
public class UserBasedRecommender implements Recommender {

    private int kNeighbour;
    private Similarity simi;
    private DataModel model;
    private int kItem;
    private int userID;
    private float[][] data;

    public UserBasedRecommender(int kNeighbour, Similarity simi, DataModel model, int kItem, int userID) {
        this.kNeighbour = kNeighbour;
        this.simi = simi;
        this.model = model;
        this.kItem = kItem;
        this.userID = userID;
        data = model.getData();
    }

    public UserBasedRecommender(Similarity simi, DataModel model, int kNeighbour, int kItem) {
        this.kNeighbour = kNeighbour;
        this.kItem = kItem;
        this.simi = simi;
        this.model = model;
        data = model.getData();
    }

    private float getAverage(int userID, int itemID) throws SQLException {

        float avg = 0;
        int count = 0;
        for (int i = 1; i <= model.getItemNum(); i++) {
            if (i != itemID && data[userID][i] != -1) {
                avg += data[userID][i];
                count++;
            }

        }
        avg = avg / count;
        return avg;
    }

    // tinh toan du doan
    public Preference estimatePreference(int itemID, int gamma) throws SQLException {
        UserNeighbour unh = new UserNeighbour(simi, model, userID, itemID);
        List<UserCorrelation> neighbs = unh.getKNeighbour(kNeighbour, gamma);
        float param1 = param1(neighbs, itemID);
        float param2 = param2(neighbs);
        float value = 3;
        if (param2 != 0) {
            value = getAverage(userID, itemID) + param1 / param2;
        }
        // Tao ra bien luu lai gia tri tinh duoc
        Preference pref = new Preference(userID, itemID, value);
        return pref;
    }

    public Preference estimatePreference(int userID, int itemID, int gamma) throws SQLException {
        this.userID = userID;
        return estimatePreference(itemID, gamma);

    }

    private float param1(List<UserCorrelation> neighbs, int itemID) throws SQLException {
        float value = 0;
        UserCorrelation uc;
        float avg;
        float pref;

        for (int i = 0; i < neighbs.size(); i++) {
            uc = neighbs.get(i);
            int userB = uc.getUserIDB();
            avg = getAverage(userB, itemID);
            pref = data[userB][itemID];
            value += (pref - avg) * uc.getCorrelation();

        }
        return value;
    }

    private float param2(List<UserCorrelation> neighbs) {
        float value = 0;
        float corre;
        for (int i = 0; i < neighbs.size(); i++) {
            corre = neighbs.get(i).getCorrelation();
            value += (corre < 0) ? -corre : corre;
        }
        return value;
    }

    public List<Preference> getRecommendations(int gamma) throws SQLException {
        List newItems = model.getNewItemsForUser(userID);
        List<Preference> prefs = new ArrayList<Preference>();
        Preference value;
        for (int i = 0; i < newItems.size(); i++) {
            System.out.println("Estimating item " + (Integer) newItems.get(i));
            value = estimatePreference((Integer) newItems.get(i), gamma);
            if (!Float.isNaN(value.getValue())) {
                prefs.add(value);

            } else {
                System.out.println("Not a number");
                continue;
            }

        }

        Collections.sort(prefs, new PrefComparator());
        if (kItem < prefs.size() && kItem > 0) {
            prefs = prefs.subList(0, kItem);
        }
        return prefs;
    }

    public List<Preference> getRecommendations(List items, int gamma) throws SQLException {
        List<Preference> prefs = new ArrayList<Preference>();
        Preference value;
        for (int i = 0; i < items.size(); i++) {
            System.out.println("Estimating item " + (Integer) items.get(i));
            value = estimatePreference((Integer) items.get(i), gamma);
            if (!Float.isNaN(value.getValue())) {
                prefs.add(value);

            } else {
                System.out.println("Not a number");
                continue;
            }

        }

        Collections.sort(prefs, new PrefComparator());
        if (kItem < prefs.size() && kItem > 0) {
            prefs = prefs.subList(0, kItem);
        }
        return prefs;
    }

    public List getRecommendations(int userID, int gamma) throws SQLException {
        this.userID = userID;
        return getRecommendations(gamma);
    }

    public List getRecommendations(int userID, List items, int gamma) throws SQLException {
        this.userID = userID;
        return getRecommendations(items, gamma);
    }

    class PrefComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            Preference p1 = (Preference) o1;
            Preference p2 = (Preference) o2;

            if (p1.getValue() > p2.getValue()) {
                return -1;
            } else if (p1.getValue() < p2.getValue()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public float[][] getData() {
        return data;
    }

    public void setData(float[][] data) {
        this.data = data;
    }

    public DataModel getModel() {
        return model;
    }

    public void setModel(DataModel model) {
        this.model = model;
    }

    public Similarity getSimi() {
        return simi;
    }

    public void setSimi(Similarity simi) {
        this.simi = simi;
    }

    
}
