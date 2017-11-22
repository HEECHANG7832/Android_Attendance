package com.example.heechang.attendence;

/**
 * Created by ycdoh on 2017-11-12.
 */

import java.util.Random;

public class RandomCode {

    private Random random = null;
    private int randomcode;


    public RandomCode()
    {
        random =  new Random();
        this.randomcode = random.nextInt()%10000;
    }


    public int getRandomCode()
    {
        return randomcode;
    }
}
