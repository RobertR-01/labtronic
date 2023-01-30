package com.app.labtronic.data;

import java.time.LocalDate;

public class CalData {
    private String kubackiRegNo;
    private String switezRegNo;
    private LocalDate orderDate;
    private String customerName;
    private String customerAddress;
    private String endUserName;
    private String endUserAddress;
    private Category category;
    private String manufacturer;
    private String type;
    private String serialNo;
    private String resolution;

    public CalData(String kubackiRegNo, String switezRegNo, LocalDate orderDate, String customerName,
                   String customerAddress, String endUserName, String endUserAddress, Category category,
                   String manufacturer, String type, String serialNo, String resolution) {
        this.kubackiRegNo = kubackiRegNo;
        this.switezRegNo = switezRegNo;
        this.orderDate = orderDate;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.endUserName = endUserName;
        this.endUserAddress = endUserAddress;
        this.category = category;
        this.manufacturer = manufacturer;
        this.type = type;
        this.serialNo = serialNo;
        this.resolution = resolution;
    }

    public String getKubackiRegNo() {
        return kubackiRegNo;
    }

    public String getSwitezRegNo() {
        return switezRegNo;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getEndUserName() {
        return endUserName;
    }

    public String getEndUserAddress() {
        return endUserAddress;
    }

    public Category getCategory() {
        return category;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getType() {
        return type;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public String getResolution() {
        return resolution;
    }

    public enum Category {
        DMM,
        CALIBRATOR
    }

    @Override
    public String toString() {
        return "CalData:" + '\n' +
                "kubackiRegNo = " + kubackiRegNo + '\n' +
                "switezRegNo = " + switezRegNo + '\n' +
                "orderDate = " + orderDate.toString() + '\n' +
                "customerName = " + customerName + '\n' +
                "customerAddress = " + customerAddress + '\n' +
                "endUserName = " + endUserName + '\n' +
                "endUserAddress = " + endUserAddress + '\n' +
                "category = " + category.toString() + '\n' +
                "manufacturer = " + manufacturer + '\n' +
                "type = " + type + '\n' +
                "serialNo = " + serialNo + '\n' +
                "resolution = " + resolution + '\n' +
                "---";
    }
}
