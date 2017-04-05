package com.echopen.asso.echopen.utils;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by mehdibenchoufi on 18/09/15.
 */
public class Randomize {

    public static Randomize randomize = null;

    public Random rand;

    public ArrayList<Integer> randomPixels;

    public Randomize getInstance(int min, int max, int Num) {
        if (randomize == null) {
            randomize = new Randomize();
            randomize.randInt(min, max, Num);
        }
        return randomize;
    }

    private void randInt(int min, int max, int Num) {
        if(min> max|| min <0 || max <0 || Num<0)
            throw new IndexOutOfBoundsException("this is too much less ");
        for (int i = 0; i < Num; i++) {
            int randomNum = rand.nextInt((max - min) + 1) + min;
            randomPixels.set(i, randomNum);
        }
    }
}
