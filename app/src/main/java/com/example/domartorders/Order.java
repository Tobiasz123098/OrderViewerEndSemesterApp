package com.example.domartorders;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

@IgnoreExtraProperties
public class Order {

    private long id;
    private String nr;
    private String odbiorca;
    private String status;
    private double cena_brutto;
    private String waluta;
    private String adres_dostawy;
    private String forma_zap;
    private String rodzaj_dost;
    private long data_zmiana;
    private long data_dost;
    private long data_send;
    private long data_transp;
    private long termin_zap;
    private String uwagi_dost;
    private String pracownik;
    private double wartosc_netto;
    private boolean zaplacono;
    private boolean expanded = false;

    public Order() {
    }


    @PropertyName("id")
    public long getId() { return id; }
    @PropertyName("id")
    public void setId(long id) { this.id = id; }

    @PropertyName("nr")
    public String getNr() { return nr; }
    @PropertyName("nr")
    public void setNr(String nr) { this.nr = nr; }

    @PropertyName("odbiorca")
    public String getOdbiorca() { return odbiorca; }
    @PropertyName("odbiorca")
    public void setOdbiorca(String odbiorca) { this.odbiorca = odbiorca; }

    @PropertyName("status")
    public String getStatus() { return status; }
    @PropertyName("status")
    public void setStatus(String status) { this.status = status; }

    @PropertyName("cena_brutto")
    public double getCena_brutto() { return cena_brutto; }
    @PropertyName("cena_brutto")
    public void setCena_brutto(double cena_brutto) { this.cena_brutto = cena_brutto; }

    @PropertyName("waluta")
    public String getWaluta() { return waluta; }
    @PropertyName("waluta")
    public void setWaluta(String waluta) { this.waluta = waluta; }

    @PropertyName("adres_dostawy")
    public String getAdres_dostawy() { return adres_dostawy; }
    @PropertyName("adres_dostawy")
    public void setAdres_dostawy(String adres_dostawy) { this.adres_dostawy = adres_dostawy; }

    @PropertyName("forma_zap")
    public String getForma_zap() { return forma_zap; }
    @PropertyName("forma_zap")
    public void setForma_zap(String forma_zap) { this.forma_zap = forma_zap; }

    @PropertyName("rodzaj_dost")
    public String getRodzaj_dost() { return rodzaj_dost; }
    @PropertyName("rodzaj_dost")
    public void setRodzaj_dost(String rodzaj_dost) { this.rodzaj_dost = rodzaj_dost; }
    @PropertyName("data_zmiana")
    public long getData_zmiana() { return data_zmiana; }
    @PropertyName("data_zmiana")
    public void setData_zmiana(long data_zmiana) { this.data_zmiana = data_zmiana; }
    @PropertyName("data_dost")
    public long getData_dost() { return data_dost; }
    @PropertyName("data_dost")
    public void setData_dost(long data_dost) { this.data_dost = data_dost; }

    @PropertyName("data_send")
    public long getData_send() { return data_send; }
    @PropertyName("data_send")
    public void setData_send(long data_send) { this.data_send = data_send; }

    @PropertyName("data_transp")
    public long getData_transp() { return data_transp; }
    @PropertyName("data_transp")
    public void setData_transp(long data_transp) { this.data_transp = data_transp; }

    @PropertyName("termin_zap")
    public long getTermin_zap() { return termin_zap; }
    @PropertyName("termin_zap")
    public void setTermin_zap(long termin_zap) { this.termin_zap = termin_zap; }

    @PropertyName("uwagi_dost")
    public String getUwagi_dost() { return uwagi_dost; }
    @PropertyName("uwagi_dost")
    public void setUwagi_dost(String uwagi_dost) { this.uwagi_dost = uwagi_dost; }

    @PropertyName("pracownik")
    public String getPracownik() { return pracownik; }
    @PropertyName("pracownik")
    public void setPracownik(String pracownik) { this.pracownik = pracownik; }

    @PropertyName("wartosc_netto")
    public double getWartosc_netto() { return wartosc_netto; }
    @PropertyName("wartosc_netto")
    public void setWartosc_netto(double wartosc_netto) { this.wartosc_netto = wartosc_netto; }

    @PropertyName("zaplacono")
    public boolean isZaplacono() { return zaplacono; }
    @PropertyName("zaplacono")
    public void setZaplacono(boolean zaplacono) { this.zaplacono = zaplacono; }

    public boolean isExpanded() { return expanded; }
    public void setExpanded(boolean expanded) { this.expanded = expanded; }
}
