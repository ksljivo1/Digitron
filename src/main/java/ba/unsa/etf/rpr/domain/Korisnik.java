package ba.unsa.etf.rpr.domain;

import java.util.Objects;

/**
 * bean for korisnik
 *
 * @author ksljivo1
 */

public class Korisnik implements Idable {
    private int id;
    private String username, password;
    private boolean mode;

    public Korisnik() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isMode() {
        return mode;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Korisnik korisnik = (Korisnik) o;
        return id == korisnik.id && mode == korisnik.mode && Objects.equals(username, korisnik.username) && Objects.equals(password, korisnik.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password, mode);
    }

    @Override
    public String toString() {
        return "Korisnik{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", mode=" + mode +
                '}';
    }
}
