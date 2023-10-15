package com.example.stock_analysis;

import java.util.ArrayList;
public class Ana
{
    private ArrayList<Long> pList;
    private double pMargin;
    private double peRatio;
    private long mCap;
    public Ana(ArrayList<Long> pList, double pMargin, double peRatio, long mCap){
        this.pList = pList;
        this.pMargin = pMargin;
        this.peRatio = peRatio;
        this.mCap = mCap;
    }
    public double pMarginScore(){
        double score = 0;
        for(double i = 0; i<0.6; i+=0.006){
            if(pMargin > i)
                score+=0.1;
        }
        score = (int)(score*10);
        score = score/10;
        return score;
    }
    public double mCapScore(){
        double score = 10.0;
        long optVal = 5000000000L;
        if(mCap >= optVal){
            for (long i = mCap; i>=optVal; i-=5000000000L){
                if(i>optVal)
                    score-=0.1;
                if(score<=0)
                    return 0;
            }
        }
        else{
            for (long i = mCap; i<optVal; i+=50000000){
                if(i<optVal)
                    score-=0.1;
            }
        }
        score = (int)(score*10);
        score = score/10;
        return score;
    }
    public double peRatioScore(){
        double score = 0;
        for(double i = 70; i>0; i-=0.7){
            if(peRatio < i)
                score+=0.1;
        }
        score = (int)(score*10);
        score = score/10;
        return score;
    }
    public double volResScore(){
        double score = 0;
        for(int i = 3; i>=0; i--){
            if(pList.get(i) - pList.get(i+1) > 0)
                score+=2.5;
        }
        return score;
    }
    public double profGrowthScore(){
        double score = 0;
        for(double i = 1.2; i<=2.0; i+=0.008){
            if(pList.get(4)>0 && pList.get(0)>0){
                if(((double)pList.get(0)/(double)pList.get(4)) > i)
                    score+=0.1;
            }
            if(pList.get(4)<0 && pList.get(0)>0){
                double diff = (double)pList.get(0) - (double)pList.get(4);
                double baseVal = (double)pList.get(4) * -1;
                if((diff/baseVal) > i)
                    score+=0.1;
            }
        }
        score = (int)(score*10);
        score = score/10;
        return score;
    }
    public double signProfScore(){
        int score = 0;
        if(pList.get(0) > 0)
            score+=10;
        return score;
    }
    public double totalScore(){
        double total = pMarginScore() + peRatioScore() + volResScore() + profGrowthScore() + signProfScore() + mCapScore();
        total = (int)(total*10);
        total = total/10;
        return total;
    }
}
