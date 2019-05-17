package com.example.usk.glotus_final.SuperviserApp.ReceptionFiles;

import java.io.Serializable;

public class PdfData implements Serializable {
    String fromCity;
    String toCity;
    String otpravitel;
    String poluchatel;
    String kolvoMest;
    String ves;
    String obiem;
    String typeTrans;
    String rasp;
    String numZakaz;
    String date;
    String mesta;


    private String numotpr;
    private String numpolu;
    private String platel;
    private String namegruz;
    private String haracgruz;


    public PdfData() {
    }

    public PdfData(String fromCity, String toCity, String otpravitel, String poluchatel, String kolvoMest,
                   String ves, String obiem, String typeTrans, String rasp, String numZakaz, String date,
                   String mesta) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.otpravitel = otpravitel;
        this.poluchatel = poluchatel;
        this.kolvoMest = kolvoMest;
        this.ves = ves;
        this.obiem = obiem;
        this.typeTrans = typeTrans;
        this.rasp = rasp;
        this.numZakaz = numZakaz;
        this.date = date;
        this.mesta = mesta;

    }

    public PdfData(String fromCity, String toCity, String otpravitel, String poluchatel, String kolvoMest,
                   String ves, String obiem, String typeTrans, String rasp, String numZakaz, String date,
                   String mesta, String numotpr,String numpolu,String platel,String namegruz,String haracgruz) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.otpravitel = otpravitel;
        this.poluchatel = poluchatel;
        this.kolvoMest = kolvoMest;
        this.ves = ves;
        this.obiem = obiem;
        this.typeTrans = typeTrans;
        this.rasp = rasp;
        this.numZakaz = numZakaz;
        this.date = date;
        this.mesta = mesta;


        this.numotpr=numotpr;
        this.numpolu=numpolu;
        this.platel=platel;
        this.namegruz=namegruz;
        this.haracgruz=haracgruz;

    }


    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public String getOtpravitel() {
        return otpravitel;
    }

    public void setOtpravitel(String otpravitel) {
        this.otpravitel = otpravitel;
    }

    public String getPoluchatel() {
        return poluchatel;
    }

    public void setPoluchatel(String poluchatel) {
        this.poluchatel = poluchatel;
    }

    public String getKolvoMest() {
        return kolvoMest;
    }

    public void setKolvoMest(String kolvoMest) {
        this.kolvoMest = kolvoMest;
    }

    public String getVes() {
        return ves;
    }

    public void setVes(String ves) {
        this.ves = ves;
    }

    public String getObiem() {
        return obiem;
    }

    public void setObiem(String obiem) {
        this.obiem = obiem;
    }

    public String getTypeTrans() {
        return typeTrans;
    }

    public void setTypeTrans(String typeTrans) {
        this.typeTrans = typeTrans;
    }

    public String getRasp() {
        return rasp;
    }

    public void setRasp(String rasp) {
        this.rasp = rasp;
    }

    public String getNumZakaz() {
        return numZakaz;
    }

    public void setNumZakaz(String numZakaz) {
        this.numZakaz = numZakaz;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMesta() {
        return mesta;
    }

    public void setMesta(String mesta) {
        this.mesta = mesta;
    }

    public String getPlatel() {
        return platel;
    }

    public String getNumpolu() {
        return numpolu;
    }

    public String getNumotpr() {
        return numotpr;
    }

    public String getNamegruz() {
        return namegruz;
    }

    public String getHaracgruz() {
        return haracgruz;
    }

}
