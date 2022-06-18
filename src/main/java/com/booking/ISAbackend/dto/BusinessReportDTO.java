package com.booking.ISAbackend.dto;

import java.util.List;

public class BusinessReportDTO {
    List<OfferForReportDTO> adventuresIncome;
    List<OfferForReportDTO> cottagesIncome;
    List<OfferForReportDTO> shipsIncome;
    Double totalIncome;

    public BusinessReportDTO(List<OfferForReportDTO> adventuresIncome, List<OfferForReportDTO> cottagesIncome, List<OfferForReportDTO> shipsIncome, Double totalIncome) {
        this.adventuresIncome = adventuresIncome;
        this.cottagesIncome = cottagesIncome;
        this.shipsIncome = shipsIncome;
        this.totalIncome = totalIncome;
    }

    public BusinessReportDTO(){

    }
    public List<OfferForReportDTO> getAdventuresIncome() {
        return adventuresIncome;
    }

    public List<OfferForReportDTO> getCottagesIncome() {
        return cottagesIncome;
    }

    public List<OfferForReportDTO> getShipsIncome() {
        return shipsIncome;
    }

    public Double getTotalIncome() {
        return totalIncome;
    }

    public void setAdventuresIncome(List<OfferForReportDTO> adventuresIncome) {
        this.adventuresIncome = adventuresIncome;
    }

    public void setCottagesIncome(List<OfferForReportDTO> cottagesIncome) {
        this.cottagesIncome = cottagesIncome;
    }

    public void setShipsIncome(List<OfferForReportDTO> shipsIncome) {
        this.shipsIncome = shipsIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        this.totalIncome = totalIncome;
    }
}

