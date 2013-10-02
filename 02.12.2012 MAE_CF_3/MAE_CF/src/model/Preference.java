/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

/**
 *
 * 
 */
public class Preference {
    private int userID;
    private int itemID;
    private float value;

    public Preference(int userID, int itemID, float value) {
        this.userID = userID;
        this.itemID = itemID;
        this.value = value;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    

    
}
