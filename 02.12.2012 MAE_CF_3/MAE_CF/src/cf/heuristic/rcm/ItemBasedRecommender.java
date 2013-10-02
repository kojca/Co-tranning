/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cf.heuristic.rcm;

import Neighbour.ItemNeighbour;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import model.DataModel;
import model.ItemCorrelation;
import model.PrefComparator;
import model.Preference;
import recommender.Recommender;
import similarity.Similarity;

/**
 *
 * 
 */
public class ItemBasedRecommender implements Recommender {

    private Similarity simi;
    private DataModel model;
    private int kItem;
    private int kNeigh;
    private int userID;
    private float[][] data;

    public ItemBasedRecommender(Similarity simi, DataModel model, int kItem, int kNeigh, int userID) throws SQLException {
        this.simi = simi;
        this.model = model;
        this.kItem = kItem;
        this.kNeigh = kNeigh;
        this.userID = userID;
        data = model.getData();

    }

    public ItemBasedRecommender(Similarity simi, DataModel model, int kItem, int kNeigh) {
        this.simi = simi;
        this.model = model;
        this.kItem = kItem;
        this.kNeigh = kNeigh;
        data = model.getData();
    }

    public List getRecommendations(int userID, int gamma) throws SQLException {
        this.userID = userID;
        return getRecommendations(gamma);
    }

    public List getRecommendations(int userID, List items, int gamma) throws SQLException {
        this.userID = userID;
        return getRecommendations(items,gamma);
    }

    public List getRecommendations(int gamma) throws SQLException {
        List newItems = model.getNewItemsForUser(userID);
        List<Preference> prefs = new ArrayList<Preference>();
        Preference value;
        for (int i = 0; i < newItems.size(); i++) {
            System.out.println("Estimating item " + (Integer) newItems.get(i));
            value = estimatePreference((Integer) newItems.get(i), gamma);
            if (!Float.isNaN(value.getValue())) {
                prefs.add(value);
            } else {
                continue;
            }

        }

        Collections.sort(prefs, new PrefComparator());
        if (kItem < prefs.size() && kItem > 0) {
            prefs = prefs.subList(0, kItem);
        }
        return prefs;
    }

    public List getRecommendations(List items,int gamma) throws SQLException {
        List<Preference> prefs = new ArrayList<Preference>();
        Preference value;
        for (int i = 0; i < items.size(); i++) {
            System.out.println("Estimating item " + (Integer) items.get(i));
            value = estimatePreference((Integer) items.get(i),gamma);
            if (!Float.isNaN(value.getValue())) {
                prefs.add(value);
            } else {
                continue;
            }
        }

        Collections.sort(prefs, new PrefComparator());
        if (kItem < prefs.size() && kItem > 0) {
            prefs = prefs.subList(0, kItem);
        }
        return prefs;
    }

    public Preference estimatePreference(int userID, int itemID, int gamma) throws SQLException {
        this.userID = userID;
        return estimatePreference(itemID, gamma);
    }

    public Preference estimatePreference(int itemID, int gamma) throws SQLException {

        ItemNeighbour in = new ItemNeighbour(simi, model, userID, itemID);
        List<ItemCorrelation> w = in.getKNeighbour(kNeigh, gamma);

        float mau = 0;
        float tu = 0;
        float corre;
        int itID;
        for (int i = 0; i < w.size(); i++) {
            itID = w.get(i).getItemIDB();
            corre = w.get(i).getCorrelation();
            tu += data[userID][itID] * corre;
            mau += (corre < 0) ? -corre : corre;
        }

        float est = 3;
        if (mau != 0) {
            est = (float) tu / mau;
        }
        return new Preference(userID, itemID, est);
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
