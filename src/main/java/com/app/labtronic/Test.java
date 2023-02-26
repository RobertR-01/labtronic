package com.app.labtronic;

import javafx.beans.binding.NumberBinding;
import javafx.beans.property.SimpleDoubleProperty;

public class Test {
    public static void main(String[] args) {
        SimpleDoubleProperty doubleProperty = new SimpleDoubleProperty(10);
        NumberBinding binding = doubleProperty.add(1);
        doubleProperty.set(binding.doubleValue());
        System.out.println(doubleProperty.get());
        System.out.println(binding.getValue());
    }
}
