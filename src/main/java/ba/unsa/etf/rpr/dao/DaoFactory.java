package ba.unsa.etf.rpr.dao;

public class DaoFactory {
    private static final KorisnikDao korisnikDao = new KorisnikDaoSQLImpl();
    private static final RacunDao racunDao = new RacunDaoSQLImpl();
    private static final OmiljenaOperacijaDao omiljenaOperacijaDao = new OmiljenaOperacijaDaoSQLImpl();

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
