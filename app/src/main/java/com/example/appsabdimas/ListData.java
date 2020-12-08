package com.example.appsabdimas;

public class ListData {
    private String Tegangan;
    private String Arus;
    private String Daya;
    private String Saklar;

    public ListData(String Tegangan, String Arus, String Daya, String Saklar) {
        this.Tegangan = Tegangan;
        this.Arus = Arus;
        this.Daya = Daya;
        this.Saklar = Saklar;
    }

    public ListData() {
    }

    public String getTegangan()
    { return Tegangan; }

    public void setTegangan(String Tegangan)
    { this.Tegangan = Tegangan; }

    public String getArus()
    { return Arus; }

    public void setArus(String Arus)
    { this.Arus = Arus; }

    public String getDaya()
    { return Daya; }

    public void setDaya(String Daya)
    { this.Daya = Daya; }

    public String getSaklar()
    { return Saklar; }

    public void setSaklar(String Saklar)
    { this.Saklar = Saklar; }
}

