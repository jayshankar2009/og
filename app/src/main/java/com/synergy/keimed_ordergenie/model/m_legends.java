package com.synergy.keimed_ordergenie.model;

/**
 * Created by 1132 on 08-02-2017.
 */

public class m_legends {

         Integer  LegendID;

    public Integer getLegendID() {
        return LegendID;
    }

    public void setLegendID(Integer legendID) {
        LegendID = legendID;
    }

    public String getLegendName() {
        return LegendName;
    }

    public void setLegendName(String legendName) {
        LegendName = legendName;
    }

    public String getColorCode() {
        return ColorCode;
    }

    public void setColorCode(String colorCode) {
        ColorCode = colorCode;
    }

    public Integer getStartRange() {
        return StartRange;
    }

    public void setStartRange(Integer startRange) {
        StartRange = startRange;
    }

    public Integer getEndRange() {
        return EndRange;
    }

    public void setEndRange(Integer endRange) {
        EndRange = endRange;
    }

    public Integer getStockLegendID() {
        return StockLegendID;
    }

    public void setStockLegendID(Integer stockLegendID) {
        StockLegendID = stockLegendID;
    }

    public Integer getLegendMode() {
        return LegendMode;
    }

    public void setLegendMode(Integer legendMode) {
        LegendMode = legendMode;
    }

    String  LegendName;
    String  ColorCode;
    Integer StartRange;
    Integer EndRange;
    Integer StockLegendID;
    Integer LegendMode;

}
