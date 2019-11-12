package com.portfolio.guardian;

import java.util.Date;

public class Crime {

    private String type;
    private Date date;
    private String neighborhood;
    private long x;
    private long y;

    public Crime(){}

    public Crime (String type, Date date, String neighborhood, long x, long y) {
        this.type = type;
        this.date = date;
        this.neighborhood = neighborhood;
        this.x = x;
        this.y = y;
    }

    public String getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public void setX(long x) {
        this.x = x;
    }

    public void setY(long y) {
        this.y = y;
    }
}
