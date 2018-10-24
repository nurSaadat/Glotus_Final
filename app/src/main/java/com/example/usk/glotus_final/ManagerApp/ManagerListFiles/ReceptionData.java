package com.example.usk.glotus_final.ManagerApp.ManagerListFiles;

import java.io.Serializable;

public class ReceptionData implements Serializable {
    private String code;
    private String date;
    private String zakazchik;
    private String pochta;
    private String dogovor;
    private String otpavitel;
    private String otkuda;
    private String address;
    private String kontakt;
    private String telefon;
    private String poluchatel;
    private String dateEdit;
    private String planKolich;
    private String planVes;
    private String planObiem;
    private String factKolich;
    private String factVes;
    private String factObiem;
    private String info;
    private String vid;
    private String dostavka;
    private String stoimost;
    private String status;
    private String komment;

    public ReceptionData() {
    }

    public ReceptionData(String code, String date, String zakazchik, String pochta, String dogovor,
                         String otpavitel, String otkuda, String address, String kontakt, String telefon,
                         String poluchatel, String dateEdit, String planKolich, String planVes, String planObiem,
                         String factKolich, String factVes, String factObiem, String info, String vid, String dostavka,
                         String stoimost, String status, String komment) {
        this.code = code;
        this.date = date;
        this.zakazchik = zakazchik;
        this.pochta = pochta;
        this.dogovor = dogovor;
        this.otpavitel = otpavitel;
        this.otkuda = otkuda;
        this.address = address;
        this.kontakt = kontakt;
        this.telefon = telefon;
        this.poluchatel = poluchatel;
        this.dateEdit = dateEdit;
        this.planKolich = planKolich;
        this.planVes = planVes;
        this.planObiem = planObiem;
        this.factKolich = factKolich;
        this.factVes = factVes;
        this.factObiem = factObiem;
        this.info = info;
        this.vid = vid;
        this.dostavka = dostavka;
        this.stoimost = stoimost;
        this.status = status;
        this.komment = komment;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getZakazchik() {
        return zakazchik;
    }

    public void setZakazchik(String zakazchik) {
        this.zakazchik = zakazchik;
    }

    public String getPochta() {
        return pochta;
    }

    public void setPochta(String pochta) {
        this.pochta = pochta;
    }

    public String getDogovor() {
        return dogovor;
    }

    public void setDogovor(String dogovor) {
        this.dogovor = dogovor;
    }

    public String getOtpavitel() {
        return otpavitel;
    }

    public void setOtpavitel(String otpavitel) {
        this.otpavitel = otpavitel;
    }

    public String getOtkuda() {
        return otkuda;
    }

    public void setOtkuda(String otkuda) {
        this.otkuda = otkuda;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getKontakt() {
        return kontakt;
    }

    public void setKontakt(String kontakt) {
        this.kontakt = kontakt;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getPoluchatel() {
        return poluchatel;
    }

    public void setPoluchatel(String poluchatel) {
        this.poluchatel = poluchatel;
    }

    public String getDateEdit() {
        return dateEdit;
    }

    public void setDateEdit(String dateEdit) {
        this.dateEdit = dateEdit;
    }

    public String getPlanKolich() {
        return planKolich;
    }

    public void setPlanKolich(String planKolich) {
        this.planKolich = planKolich;
    }

    public String getPlanVes() {
        return planVes;
    }

    public void setPlanVes(String planVes) {
        this.planVes = planVes;
    }

    public String getPlanObiem() {
        return planObiem;
    }

    public void setPlanObiem(String planObiem) {
        this.planObiem = planObiem;
    }

    public String getFactKolich() {
        return factKolich;
    }

    public void setFactKolich(String factKolich) {
        this.factKolich = factKolich;
    }

    public String getFactVes() {
        return factVes;
    }

    public void setFactVes(String factVes) {
        this.factVes = factVes;
    }

    public String getFactObiem() {
        return factObiem;
    }

    public void setFactObiem(String factObiem) {
        this.factObiem = factObiem;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getDostavka() {
        return dostavka;
    }

    public void setDostavka(String dostavka) {
        this.dostavka = dostavka;
    }

    public String getStoimost() {
        return stoimost;
    }

    public void setStoimost(String stoimost) {
        this.stoimost = stoimost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getKomment() {
        return komment;
    }

    public void setKomment(String komment) {
        this.komment = komment;
    }
}