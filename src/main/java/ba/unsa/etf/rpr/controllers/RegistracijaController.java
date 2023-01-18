package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.dao.KorisnikDaoSQLImpl;
import ba.unsa.etf.rpr.domain.Korisnik;
import ba.unsa.etf.rpr.exceptions.DigitronException;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RegistracijaController {
    public TextField textFld;
    public PasswordField passwordFld;
    private KorisnikDaoSQLImpl korisnikDaoSQLImpl;

    public void onBtnClicked(ActionEvent actionEvent) throws DigitronException {
        korisnikDaoSQLImpl = new KorisnikDaoSQLImpl();
        Korisnik korisnik = new Korisnik();
        korisnik.setMode(true);
        korisnik.setUsername(textFld.getText());
        korisnik.setPassword(passwordFld.getText());
        korisnik = korisnikDaoSQLImpl.add(korisnik);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Registration Status:");
        alert.setContentText("Uspješno ste se registrovali!");
        alert.showAndWait();
        Stage stage = (Stage)textFld.getScene().getWindow();
        stage.close();
    }
}
