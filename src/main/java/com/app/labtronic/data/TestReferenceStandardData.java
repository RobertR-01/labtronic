package com.app.labtronic.data;

import java.util.ArrayList;
import java.util.List;

public class TestReferenceStandardData {
    private static TestReferenceStandardData instance = new TestReferenceStandardData();
    private static List<String> vdcRangeList = new ArrayList<>();

    private static List<String> vdc1VErrorList = new ArrayList<>();
    private static List<String> vdc1VUncertaintyList = new ArrayList<>();

    public static TestReferenceStandardData getInstance() {
        return instance;
    }

    private TestReferenceStandardData() {
        vdcRangeList.add("1 V");
        vdc1VErrorList = List.of("0.00000001", "0.00000001", "0.00000001", "-0.00000001", "-0.00000001", "-0.00000001");
        vdc1VUncertaintyList = List.of("0.00000004", "0.00000003", "0.00000002", "-0.00000002", "-0.00000003",
                "-0.00000004");
    }


}
