package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Racun;
import ba.unsa.etf.rpr.exceptions.DigitronException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * MySQL implementation of the DAO
 * @author ksljivo1
 */

public class RacunDaoSQLImpl extends AbstractDao<Racun> implements RacunDao {
    private static RacunDaoSQLImpl instance = null;

    public RacunDaoSQLImpl() {
        super("Racun");
    }

    public static RacunDaoSQLImpl getInstance() {
        if(instance == null) instance = new RacunDaoSQLImpl();
        return instance;
    }

    public static void removeInstance() {
        if(instance != null) instance = null;
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
        }
        catch(SQLException e) {
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

    @Override
    public List<Racun> getRacuniByKorisnikId(int korisnikId) throws DigitronException {
        List<Racun> racuni = new ArrayList<>();
        String query = "SELECT * FROM Racun WHERE idKorisnik = ?";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, korisnikId);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) racuni.add(row2object(rs));
        }
        catch(Exception e) {
            throw new DigitronException(e.getMessage(), e);
        }
        return racuni;
    }
}
