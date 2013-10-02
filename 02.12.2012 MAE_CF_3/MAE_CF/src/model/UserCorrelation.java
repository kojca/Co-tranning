/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

/**
 *  Cac doi tuong co do tuong tu 
 * 
 */
public class UserCorrelation {
    private int userIDA;
    private int userIDB;
    private float correlation;

    public UserCorrelation(int userIDA, int userIDB, float correlation) {
        this.userIDA = userIDA;
        this.userIDB = userIDB;
        this.correlation = correlation;
    }

    public float getCorrelation() {
        return correlation;
    }

    public void setCorrelation(float correlation) {
        this.correlation = correlation;
    }

    public int getUserIDA() {
        return userIDA;
    }

    public void setUserIDA(int userIDA) {
        this.userIDA = userIDA;
    }

    public int getUserIDB() {
        return userIDB;
    }

    public void setUserIDB(int userIDB) {
        this.userIDB = userIDB;
    }

    
    
    

}
