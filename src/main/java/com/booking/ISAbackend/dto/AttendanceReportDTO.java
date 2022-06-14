package com.booking.ISAbackend.dto;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AttendanceReportDTO {
    private String offerName;
    private TreeMap<LocalDate,Integer> value;
    private List<String> dates = new ArrayList<>();
    private List<Integer> vales = new ArrayList<>();



    public AttendanceReportDTO(String offerName, TreeMap<LocalDate,Integer> value) {
        this.offerName = offerName;
        this.value = value;
        for (Map.Entry<LocalDate, Integer> entry : value.entrySet()){
            dates.add(entry.getKey().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            vales.add(entry.getValue());
        }

    }
    public AttendanceReportDTO(){}

    public String getOfferName() {
        return offerName;
    }

    public TreeMap<LocalDate,Integer> getValue() {
        return value;
    }
    public List<String> getDates() {
        return dates;
    }

    public List<Integer> getVales() {
        return vales;
    }
}
