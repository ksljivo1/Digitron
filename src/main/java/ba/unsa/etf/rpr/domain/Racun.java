package ba.unsa.etf.rpr.domain;

import java.util.Date;
import java.util.Objects;

/**
 * bean for racun
 *
 * @author ksljivo1
 */

public class Racun implements Idable {
    private int id;
    private Date datum;
    private String rezultat;
    private int idKorisnik;

    public Racun() {}

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public int getIdKorisnik() {
        return idKorisnik;
    }

    public void setIdKorisnik(int idKorisnik) {
        this.idKorisnik = idKorisnik;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public String getRezultat() {
        return rezultat;
    }

    public void setRezultat(String rezultat) {
        this.rezultat = rezultat;
    }

    @Override
    public String toString() {
        return "Racun{" +
                "id=" + id +
                ", datum=" + datum +
                ", rezultat='" + rezultat + '\'' +
                ", idKorisnik=" + idKorisnik +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Racun racun = (Racun) o;
        return id == racun.id && Objects.equals(datum, racun.datum) && Objects.equals(rezultat, racun.rezultat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, datum, rezultat);
    }
}

