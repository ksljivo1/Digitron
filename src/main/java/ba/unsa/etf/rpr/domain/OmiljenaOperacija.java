package ba.unsa.etf.rpr.domain;

import java.util.Objects;

/**
 * bean for omiljenaoperacija
 *
 * @author ksljivo1
 */

public class OmiljenaOperacija implements Idable {
    private int id;
    private String operacija;
    private int brojPonavljanja;
    private int idKorisnik;

    public OmiljenaOperacija() {}

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

    public String getOperacija() {
        return operacija;
    }

    public void setOperacija(String operacija) {
        this.operacija = operacija;
    }

    public int getBrojPonavljanja() {
        return brojPonavljanja;
    }

    public void setBrojPonavljanja(int brojPonavljanja) {
        this.brojPonavljanja = brojPonavljanja;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OmiljenaOperacija that = (OmiljenaOperacija) o;
        return id == that.id && brojPonavljanja == that.brojPonavljanja && Objects.equals(operacija, that.operacija);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, operacija, brojPonavljanja);
    }

    @Override
    public String toString() {
        return "OmiljenaOperacija{" +
                "id=" + id +
                ", operacija='" + operacija + '\'' +
                ", brojPonavljanja=" + brojPonavljanja +
                '}';
    }
}
