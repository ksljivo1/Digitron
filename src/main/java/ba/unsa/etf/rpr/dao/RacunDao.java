package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Racun;
import ba.unsa.etf.rpr.exceptions.DigitronException;

import java.util.List;

/**
 * Dao interface for Racun domain bean
 *
 * @author ksljivo1
 */

public interface RacunDao extends Dao<Racun> {
    public List<Racun> getRacuniByKorisnikId(int korisnikId) throws DigitronException;
}
