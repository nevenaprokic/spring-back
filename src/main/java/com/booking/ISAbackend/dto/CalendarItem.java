package com.booking.ISAbackend.dto;

public class CalendarItem {
    int id;
    boolean isReservation;
    String startDate;
    String endDate;
    String title;
    boolean isAction;

    public CalendarItem(int id, boolean isReservation, String startDate, String endDate, String title, boolean isAction) {
        this.id = id;
        this.isReservation = isReservation;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.isAction = isAction;
    }

    public int getId() {
        return id;
    }

    public boolean isReservation() { return isReservation; }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getTitle() {
        return title;
    }

    public boolean isAction(){return  isAction;}
}
