package ba.unsa.etf.rpr.models;

import ba.unsa.etf.rpr.domain.Racun;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Date;

/**
 * Helper Model class that supports 2 way data binding between labels and records in Racun
 * @author ksljivo1
 *
 */

public class RacunModel {
    private SimpleIntegerProperty id;
    private SimpleObjectProperty<Date> datum;
    private SimpleStringProperty rezultat;
    private SimpleIntegerProperty idKorisnik;

    public RacunModel(int id, Date datum, String rezultat, int idKorisnik) {
        this.id = new SimpleIntegerProperty(id);
        this.datum = new SimpleObjectProperty<>(datum);
        this.rezultat = new SimpleStringProperty(rezultat);
        this.idKorisnik = new SimpleIntegerProperty(idKorisnik);
    }

    public RacunModel(Racun racun) {
        this.id = new SimpleIntegerProperty(racun.getId());
        this.datum = new SimpleObjectProperty<>(racun.getDatum());
        this.rezultat = new SimpleStringProperty(racun.getRezultat());
        this.idKorisnik = new SimpleIntegerProperty(racun.getId());
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public Date getDatum() {
        return datum.get();
    }

    public SimpleObjectProperty<Date> datumProperty() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum.set(datum);
    }

    public String getRezultat() {
        return rezultat.get();
    }

    public SimpleStringProperty rezultatProperty() {
        return rezultat;
    }

    public void setRezultat(String rezultat) {
        this.rezultat.set(rezultat);
    }

    public int getIdKorisnik() {
        return idKorisnik.get();
    }

    public SimpleIntegerProperty idKorisnikProperty() {
        return idKorisnik;
    }

    public void setIdKorisnik(int idKorisnik) {
        this.idKorisnik.set(idKorisnik);
    }

    @Override
    public String toString() {
        return "RacunModel{" +
                "id=" + id +
                ", datum=" + datum +
                ", rezultat=" + rezultat +
                ", idKorisnik=" + idKorisnik +
                '}';
    }
}
