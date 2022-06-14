package com.booking.ISAbackend.dto;

import com.booking.ISAbackend.model.Impression;

public class NewReservationReportDTO {
    private Integer clientId;
    private String comment;
    private Boolean valueShowUp;
    private Impression valueImpression;
    private Integer reservationId;


    public NewReservationReportDTO(Integer clientId, String comment, Boolean valueShowUp, Impression valueImpression, Integer reservationId) {
        this.clientId = clientId;
        this.comment = comment;
        this.valueShowUp = valueShowUp;
        this.valueImpression = valueImpression;
        this.reservationId = reservationId;
    }
    public NewReservationReportDTO(){}

    public Integer getClientId() {
        return clientId;
    }

    public String getComment() {
        return comment;
    }

    public Boolean getValueShowUp() {
        return valueShowUp;
    }

    public Impression getValueImpression() {
        return valueImpression;
    }
    public Integer getReservationId() {
        return reservationId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setValueShowUp(Boolean valueShowUp) {
        this.valueShowUp = valueShowUp;
    }

    public void setValueImpression(Impression valueImpression) {
        this.valueImpression = valueImpression;
    }

    public void setReservationId(Integer reservationId) {
        this.reservationId = reservationId;
    }
}
