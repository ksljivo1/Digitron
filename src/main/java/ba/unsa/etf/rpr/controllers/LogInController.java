package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.dao.KorisnikDaoSQLImpl;
import ba.unsa.etf.rpr.domain.Korisnik;
import ba.unsa.etf.rpr.exceptions.DigitronException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInController {
    public TextField textFld;
    public PasswordField passwordFld;

    public void onBtnKreirajNoviClicked(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/registracija.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Radi");
        stage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
    }

    public void onPrijaviSeBtnClicked(ActionEvent actionEvent) throws DigitronException, IOException {
        KorisnikDaoSQLImpl korisnikDaoSQLImpl = new KorisnikDaoSQLImpl();
        Korisnik korisnik = korisnikDaoSQLImpl.getKorisnikByUsername(textFld.getText());
        if(!korisnik.getPassword().equals(passwordFld.getText())) return; // ovdje treba dodati
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/digitron.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Digitron");
        stage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
        stage.show();
    }
}
