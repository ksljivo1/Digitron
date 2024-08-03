package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.OmiljenaOperacija;
import ba.unsa.etf.rpr.exceptions.DigitronException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.TreeMap;

/**
 * MySQL implementation of the DAO
 * @author ksljivo1
 */

public class OmiljenaOperacijaDaoSQLImpl extends AbstractDao<OmiljenaOperacija> implements OmiljenaOperacijaDao {
    private static OmiljenaOperacijaDaoSQLImpl instance = null;

    public OmiljenaOperacijaDaoSQLImpl() {
        super("Omiljena_Operacija");
    }

    public static OmiljenaOperacijaDaoSQLImpl getInstance() {
        if(instance == null) instance = new OmiljenaOperacijaDaoSQLImpl();
        return instance;
    }

    public static void removeInstance() {
        if(instance != null) instance = null;
    }

    @Override
    public OmiljenaOperacija row2object(ResultSet rs) throws DigitronException {
        try {
            OmiljenaOperacija omiljenaOperacija = new OmiljenaOperacija();
            omiljenaOperacija.setId(rs.getInt(1));
            omiljenaOperacija.setBrojPonavljanja(rs.getInt(2));
            omiljenaOperacija.setIdKorisnik(rs.getInt(3));
            omiljenaOperacija.setOperacija(rs.getString(4));
            return omiljenaOperacija;
        }
        catch(SQLException e) {
            throw new DigitronException(e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> object2row(OmiljenaOperacija object) {
        Map<String, Object> row = new TreeMap<>();
        row.put("id", object.getId());
        row.put("operacija", object.getOperacija());
        row.put("broj_ponavljanja", object.getBrojPonavljanja());
        row.put("idKorisnik", object.getIdKorisnik());
        return row;
    }

    @Override
    public OmiljenaOperacija getOmiljenaOperacijaByKorisnikId(int korisnikId) throws DigitronException {
        String query = "SELECT * FROM Omiljena_Operacija WHERE idKorisnik = ?";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setInt(1, korisnikId);
            ResultSet rs = statement.executeQuery();
            if(!rs.next()) return null;
            return row2object(rs);
        }
        catch(Exception e) {
            throw new DigitronException(e.getMessage(), e);
        }
    }
}
