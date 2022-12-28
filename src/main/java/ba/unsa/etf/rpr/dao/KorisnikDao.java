package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Korisnik;
import ba.unsa.etf.rpr.exceptions.DigitronException;

public interface KorisnikDao extends Dao<Korisnik> {
    public Korisnik getKorisnikByUsername(String username) throws DigitronException;
}
