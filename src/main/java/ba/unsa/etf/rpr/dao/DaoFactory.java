package ba.unsa.etf.rpr.dao;

/**
 * Factory method for singleton implementation of DAOs
 *
 * @author ksljivo1
 */

public class DaoFactory {
    private static final KorisnikDao korisnikDao = KorisnikDaoSQLImpl.getInstance();
    private static final RacunDao racunDao = RacunDaoSQLImpl.getInstance();
    private static final OmiljenaOperacijaDao omiljenaOperacijaDao = OmiljenaOperacijaDaoSQLImpl.getInstance();

    private DaoFactory() {}

    public static KorisnikDao korisnikDao() {
        return korisnikDao;
    }

    public static RacunDao racunDao() {
        return racunDao;
    }

    public static OmiljenaOperacijaDao omiljenaOperacijaDao() {
        return omiljenaOperacijaDao;
    }
}
