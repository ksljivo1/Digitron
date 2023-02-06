package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.OmiljenaOperacija;
import ba.unsa.etf.rpr.exceptions.DigitronException;

public interface OmiljenaOperacijaDao extends Dao<OmiljenaOperacija> {
    OmiljenaOperacija getOmiljenaOperacijaByKorisnikId(int korisnikId) throws DigitronException;
}
