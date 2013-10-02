/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package metrics;

import cf.heuristic.rcm.ItemBasedRecommender;
import cf.heuristic.rcm.UserBasedRecommender;
import datasource.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import model.DataModel;
import model.Preference;
import model.TestModel;
import recommender.Recommender;
import similarity.Similarity;

/**
 *
 * 
 */
public class MAE {

    TestModel test;
    DataModel data;
    Recommender rcm;
    UserBasedRecommender userBase;
    ItemBasedRecommender itemBase;

    public MAE(TestModel test, DataModel data, Recommender rcm) {
        this.test = test;
        this.data = data;
        this.rcm = rcm;
    }

    public MAE(TestModel test, DataModel data, UserBasedRecommender userBase, ItemBasedRecommender itemBase) {
        this.test = test;
        this.itemBase = itemBase;
        this.userBase = userBase;
        this.data = data;
    }

    public void getMAE(int gamma) throws SQLException {
        List<Preference> l = test.getData();
        float tu = 0;
        int listSize = l.size();

        for (int i = 0; i < listSize; i++) {
            int uId = l.get(i).getUserID();
            int iId = l.get(i).getItemID();
            System.out.println("NO " + i);
            float actual = l.get(i).getValue();

            float predict = rcm.estimatePreference(uId, iId, gamma).getValue();
            tu += Math.abs(actual - predict);
//            if(i==5000) break;
        }

        System.out.println("MAE: " + tu / l.size());
//        System.out.println("MAE: "+ tu/5001);

    }

    // Tinh MAE theo co trainning
    public void getMAE(float data[][]) throws SQLException {
        List<Preference> l = test.getData();
        float tu = 0;
        int listSize = l.size();

        for (int i = 0; i < listSize; i++) {
            int uId = l.get(i).getUserID();
            int iId = l.get(i).getItemID();
            System.out.println("NO " + i);
            float actual = l.get(i).getValue();

            float predict = data[uId][iId];
            tu += Math.abs(actual - predict);
//            if(i==5000) break;
        }

        System.out.println("MAE: " + tu / l.size());
//        System.out.println("MAE: "+ tu/5001);

    }

    public float[][] coTraining(int gamma) throws SQLException {
        int check = 0;
        // Lay ve ma tran co ban, chua dc dien gia tri
        float dataCotraining[][] = this.data.getData();
        int numberUser = this.data.getUserNum();
        int numberItem = this.data.getItemNum();
        // Mang data luu tam gia tri
        float tmp1[][];
        float tmp2[][];
        DataModel newModel;
       
        while (check != 30) {
            // Fill du lieu cho ma tran bang userbase
            for (int i = 0; i < numberUser; i++) {
                for (int j = 0; j < numberItem; j++) {
                    int uId = i;
                    int iId = j;
                    // Chi tinh toan tai cac diem chua co gia tri (=-1)
                    if (dataCotraining[uId][iId] == -1 && dataCotraining[uId][iId] != 0) {
                        float predict = userBase.estimatePreference(uId, iId, gamma).getValue();
                        dataCotraining[uId][iId] = predict;
                        System.out.println("Userbase dataCotraining["+uId+"]["+iId+"] = "+predict);
                    }
                }
            }
            //Luu tam gia tri sau khi tinh toan = userbase
            tmp1 = dataCotraining;
            newModel = userBase.getModel();
            newModel.setData(dataCotraining);
            // Su dung ket qua cua userbase lam input cho itembase
            itemBase.setModel(newModel);
            System.out.println("-----------------------------Start ItemBase-----------------------------");
            // Fill du lieu cho ma tran bang itembase
            for (int i = 0; i < numberUser; i++) {
                for (int j = 0; j < numberItem; j++) {
                    int uId = i;
                    int iId = j;
                    // Chi tinh toan tai cac diem chua co gia tri (=-1)
                    if (dataCotraining[uId][iId] == -1 && dataCotraining[uId][iId] != 0) {
                        float predict = itemBase.estimatePreference(uId, iId, gamma).getValue();
                        dataCotraining[uId][iId] = predict;
                        System.out.println("Item dataCotraining["+uId+"]["+iId+"] = "+predict);
                    }
                }  
            }
            tmp2 = dataCotraining;
            newModel = userBase.getModel();
            newModel.setData(dataCotraining);
            // Su dung ket qua cua userbase lam input cho itembase
            userBase.setModel(newModel);
            check++;
        }
        this.data.setData(dataCotraining);
        return dataCotraining;
    }

    public static void main(String args[]) throws SQLException, Exception {
        DataSource src = new DataSource();
        Connection con = src.getConnection();
        TestModel tm = new TestModel(con);
        List<String> t = new ArrayList<String>();
        t.add("keyword");
        t.add("actor");
        t.add("producer");
        t.add("writer");
        t.add("director");
        t.add("kind");
        t.add("company");
        t.add("genre");



//================        CF Model
//        DataModel model = new DataModel(src, "ratings","users","movies_content", "userId", "movieId", "rating");
//        cf.model.CFRecommender rcm = new cf.model.CFRecommender( model, 20);
//================        CBF model
//        DataModel model = new DataModel(src, "ratings","users", "movies_content", "userId", "movieId", "rating",t);
//        cbf.model.ContentBasedRecommender rcm= new cbf.model.ContentBasedRecommender(model,20);
//================        CBF heuristic
//        DataModel model = new DataModel(src, "ratings","users", "movies_content", "userId", "movieId", "rating",t);
//        Similarity sm = new cbf.heuristic.ItemSimilarity(model);
//        cbf.heuristic.ContentBasedRecommender rcm = new cbf.heuristic.ContentBasedRecommender(sm, model, 100, 35);
//================        CF Itembased
//        DataModel model = new DataModel(src, "ratings", "users", "movies_content", "userId", "movieId", "rating");
//
//        Similarity sm = new cf.heuristic.simi.cosine.ItemSimilarity(model);
//        cf.heuristic.rcm.ItemBasedRecommender rcm = new cf.heuristic.rcm.ItemBasedRecommender(sm, model, 20, 20);
//================      CF Userbased



        DataModel model = new DataModel(src, "train", "users", "movies", "userId", "movieId", "rating");

        Similarity usetSimi = new cf.heuristic.simi.pearson.UserSimilarity(model);
        Similarity itemSimi = new cf.heuristic.simi.pearson.ItemSimilarity(model);
        cf.heuristic.rcm.UserBasedRecommender userBase = new cf.heuristic.rcm.UserBasedRecommender(usetSimi, model, 50, 10);
        cf.heuristic.rcm.ItemBasedRecommender itembase = new cf.heuristic.rcm.ItemBasedRecommender(itemSimi, model, 50, 10);
//================        Hybird tuyen tinh

//        DataModel model = new DataModel(src, "ratings","users", "movies_content", "userId", "movieId", "rating",t);
//        cbf.model.ContentBasedRe7commender rcm1= new cbf.model.ContentBasedRecommender(model,20);
//        Similarity sm = new cf.heuristic.simi.pearson.UserSimilarity(model);
//        cf.heuristic.rcm.UserBasedRecommender rcm2 = new cf.heuristic.rcm.UserBasedRecommender(sm,model,50,10);
//        hybird.HybirdRecommender rcm= new hybird.HybirdRecommender(model, rcm1, rcm2, 1, 1);

        MAE mae = new MAE(tm, model, userBase, itembase);

        System.out.println("Moi nhap nguong gamma: ");


        int gamma;
        Scanner input = new Scanner(System.in);
        gamma = input.nextInt();

        // Co-trainning


        float data[][] = mae.coTraining(gamma);

        // Tinh MAE
        mae.getMAE(data);


    }
}
