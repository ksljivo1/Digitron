package ba.unsa.etf.rpr.dao;

import ba.unsa.etf.rpr.domain.Korisnik;
import ba.unsa.etf.rpr.exceptions.DigitronException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class KorisnikDaoSQLImpl extends AbstractDao<Korisnik> implements KorisnikDao {
    public KorisnikDaoSQLImpl() {
        super("Korisnik");
    }

    @Override
    public Korisnik row2object(ResultSet rs) throws DigitronException {
        try {
            Korisnik korisnik = new Korisnik();
            korisnik.setId(rs.getInt(1));
            korisnik.setUsername(rs.getString(2));
            korisnik.setPassword(rs.getString(3));
            korisnik.setMode(rs.getInt(4) == 1);
            return korisnik;
        }
        catch(SQLException e) {
            throw new DigitronException(e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> object2row(Korisnik object) {
        Map<String, Object> row = new HashMap<>();
        row.put("id", object.getId());
        row.put("username", object.getUsername());
        row.put("password", object.getPassword());
        row.put("mode", object.isMode() ? 1 : 0);
        return row;
    }

    @Override
    public Korisnik getKorisnikByUsername(String username) throws DigitronException {
        String query = "SELECT * FROM Korisnik WHERE username = ?";
        try {
            PreparedStatement statement = getConnection().prepareStatement(query);
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();
            if(!rs.next()) return null;
            return row2object(rs);
        }
        catch(SQLException e) {
            throw new DigitronException(e.getMessage(), e);
        }
    }
}
