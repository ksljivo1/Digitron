package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Racun;
import ba.unsa.etf.rpr.exceptions.DigitronException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

public class RacunDaoSQLImpl extends AbstractDao<Racun> implements RacunDao {
    public RacunDaoSQLImpl() {
        super("Racun");
    }

    @Override
    public Racun row2object(ResultSet rs) throws DigitronException {
        try {
            Racun racun = new Racun();
            racun.setId(rs.getInt("id"));
            racun.setIdKorisnik(rs.getInt("idKorisnik"));
            racun.setDatum(rs.getDate("datum"));
            racun.setRezultat(rs.getString("rezultat"));
            return racun;
        } catch (SQLException e) {
            throw new DigitronException(e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> object2row(Racun object) {
        Map<String, Object> row = new TreeMap<>();
        row.put("id", object.getId());
        row.put("idKorisnik", object.getIdKorisnik());
        row.put("datum", object.getDatum());
        row.put("rezultat", object.getRezultat());
        return row;
    }
}
