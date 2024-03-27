package com.mpedroni.aprendatech.domain.reports.utils;

public class NpsCalculator {
    public static int nps(int promoters, int detractors, int total) {
        return (int) Math.round((promoters - detractors) / (double) total * 100);
    }
}
