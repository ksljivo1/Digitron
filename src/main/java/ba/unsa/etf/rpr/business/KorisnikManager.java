package ba.unsa.etf.rpr.business;

import ba.unsa.etf.rpr.dao.DaoFactory;
import ba.unsa.etf.rpr.domain.Korisnik;
import ba.unsa.etf.rpr.exceptions.DigitronException;

/**
 * Business Logic Layer for management of Korisnik instances
 *
 * @author ksljivo1
 */

public class KorisnikManager {
    public boolean comparePasswords(Korisnik korisnik, String password) {
        return korisnik != null && korisnik.getPassword().equals(password);
    }
    public void validateKorisnikUsername(String name) throws DigitronException {
        if(name == null || name.length() > 255 || name.length() < 8) {
            throw new DigitronException("Username must be between 8 and 255 chars");
        }
    }

    public void validateKorisnikPassword(String password) throws DigitronException {
        try {
            validateKorisnikUsername(password);
        }
        catch(DigitronException e) {
            throw new DigitronException("Password must be between 8 and 255 chars");
        }
    }

    public Korisnik add(Korisnik korisnik) throws DigitronException {
        if(korisnik.getId() != 0) throw new DigitronException("Can't add korisnik with ID. ID is autogenerated");

        validateKorisnikUsername(korisnik.getUsername());
        validateKorisnikPassword(korisnik.getPassword());

        try {
            return DaoFactory.korisnikDao().add(korisnik);
        }
        catch(DigitronException e) {
            if(e.getMessage().contains("UNIQUE")) throw new DigitronException("User with same name already exists");
            throw e;
        }
    }
}
