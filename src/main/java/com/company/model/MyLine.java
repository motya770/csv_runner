package com.company.model;

import lombok.Data;

@Data
public class MyLine implements Comparable {
    private String line;
    private double value;

    @Override
    public int compareTo(Object o) {
        return Double.compare(value, ((MyLine) o).value);
    }
}
