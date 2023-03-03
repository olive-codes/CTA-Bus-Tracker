package com.oliviabecht.obechtbustracker;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class prediction implements Serializable, Comparable<prediction> {

    private final String predictvid;
    private final String predictrtdir;
    private final String predictdes;
    private final String predictprdtm;
    private final String predictdly;
    private final String predictprdctdn;


    public prediction(String predictvid, String predictrtdir, String predictdes, String predictprdtm, String predictdly, String predictprdctdn) {
        this.predictvid = predictvid;
        this.predictrtdir = predictrtdir;
        this.predictdes = predictdes;
        this.predictprdtm = predictprdtm;
        this.predictdly = predictdly;
        this.predictprdctdn = predictprdctdn;
    }

    public String getPredictvid() {
        return predictvid;
    }

    public String getPredictrtdir() {
        return predictrtdir;
    }

    public String getPredictdes() { return predictdes; }

    public String getPredictprdtm() {
        return predictprdtm;
    }

    public String getPredictdly() { return predictdly; }

    public String getPredictprdctdn() {
        return predictprdctdn;
    }


    @Override
    public int compareTo (prediction predict) { return predictvid.compareTo(predict.predictvid); }

    @NonNull
    @Override
    public String toString() {
        return "Stop{" +
                "predictvid='" + predictvid + '\'' +
                ", predictrtdir='" + predictrtdir + '\'' +
                ", predictdes='" + predictdes + '\'' +
                ", predictprdtm='" + predictprdtm + '\'' +
                ", predictdly='" + predictdly + '\'' +
                ", predictprdctdn='" + predictprdctdn + '\'' +
                '}';
    }


}
