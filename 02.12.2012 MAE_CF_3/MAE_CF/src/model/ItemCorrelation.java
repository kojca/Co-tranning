/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

/**
 *
 * 
 */
public class ItemCorrelation {
    private int itemIDA;
    private int itemIDB;
    private float correlation;

    public ItemCorrelation(int itemIDA, int itemIDB, float correlation) {
        this.itemIDA = itemIDA;
        this.itemIDB = itemIDB;
        this.correlation = correlation;
    }

    public float getCorrelation() {
        return correlation;
    }

    public void setCorrelation(float correlation) {
        this.correlation = correlation;
    }

    public int getItemIDA() {
        return itemIDA;
    }

    public void setItemIDA(int itemIDA) {
        this.itemIDA = itemIDA;
    }

    public int getItemIDB() {
        return itemIDB;
    }

    public void setItemIDB(int itemIDB) {
        this.itemIDB = itemIDB;
    }   
}
