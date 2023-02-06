package ba.unsa.etf.rpr.models;

import ba.unsa.etf.rpr.domain.OmiljenaOperacija;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class OmiljenaOperacijaModel {
    private SimpleIntegerProperty id;
    private SimpleStringProperty operacija;
    private SimpleIntegerProperty brojPonavljanja;
    private SimpleIntegerProperty idKorisnik;

    public OmiljenaOperacijaModel(OmiljenaOperacija omiljenaOperacija) {
        this.id = new SimpleIntegerProperty(omiljenaOperacija.getId());
        this.operacija = new SimpleStringProperty(omiljenaOperacija.getOperacija());
        this.brojPonavljanja = new SimpleIntegerProperty(omiljenaOperacija.getBrojPonavljanja());
        this.idKorisnik = new SimpleIntegerProperty(omiljenaOperacija.getIdKorisnik());
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

    public String getOperacija() {
        return operacija.get();
    }

    public SimpleStringProperty operacijaProperty() {
        return operacija;
    }

    public void setOperacija(String operacija) {
        this.operacija.set(operacija);
    }

    public int getBrojPonavljanja() {
        return brojPonavljanja.get();
    }

    public SimpleIntegerProperty brojPonavljanjaProperty() {
        return brojPonavljanja;
    }

    public void setBrojPonavljanja(int brojPonavljanja) {
        this.brojPonavljanja.set(brojPonavljanja);
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
}
