package com.app.labtronic.data;

import javafx.scene.control.Tab;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CalData {
    private String kubackiRegNo;
    private String switezRegNo;
    private LocalDate orderDate;
    private boolean accreditation;
    private String customerName;
    private String customerAddress;
    private boolean isEndUserPresent;
    private String endUserName;
    private String endUserAddress;
    private Category category;
    private String manufacturer;
    private String type;
    private String serialNo;
    private String resolution;
    private List<Tab> subTabsList;

    private final long ID;

    public CalData(String kubackiRegNo, String switezRegNo, LocalDate orderDate, boolean accreditation,
                   String customerName, String customerAddress, boolean isEndUserPresent, String endUserName,
                   String endUserAddress, Category category, String manufacturer, String type, String serialNo,
                   String resolution) {
        this.kubackiRegNo = kubackiRegNo;
        this.switezRegNo = switezRegNo;
        this.orderDate = orderDate;
        this.accreditation = accreditation;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.isEndUserPresent = isEndUserPresent;
        this.endUserName = endUserName;
        this.endUserAddress = endUserAddress;
        this.category = category;
        this.manufacturer = manufacturer;
        this.type = type;
        this.serialNo = serialNo;
        this.resolution = resolution;
        this.ID = this.hashCode() * 3L + 11;
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

    public boolean isAccreditation() {
        return accreditation;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public boolean isEndUserPresent() {
        return isEndUserPresent;
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

    public long getID() {
        return ID;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public List<Tab> getSubTabsList() {
        return Collections.unmodifiableList(subTabsList);
    }

    // TODO: boolean return type?
    public void setSubTabsList(List<Tab> tabsList) {
        // TODO: check for the number of tabs already present
        if (tabsList != null && !tabsList.isEmpty()) {
            this.subTabsList = new ArrayList<>(tabsList);
            System.out.println("setting subTabsList");
            System.out.println(subTabsList);
        }
    }

    public enum Category {
        DMM,
        CALIBRATOR
    }

    // for testing only:
    @Override
    public String toString() {
        return "CalData:" + '\n' +
                "kubackiRegNo = " + kubackiRegNo + '\n' +
                "switezRegNo = " + switezRegNo + '\n' +
                "orderDate = " + orderDate.toString() + '\n' +
                "accreditation = " + accreditation + '\n' +
                "customerName = " + customerName + '\n' +
                "customerAddress = " + customerAddress + '\n' +
                "isEndUserPresent = " + isEndUserPresent + '\n' +
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
