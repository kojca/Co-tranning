/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package model;

import java.util.Comparator;

/**
 *
 *
 */
public class PrefComparator implements Comparator {

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