package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.dao.DaoFactory;
import ba.unsa.etf.rpr.dao.KorisnikDaoSQLImpl;
import ba.unsa.etf.rpr.domain.Korisnik;
import ba.unsa.etf.rpr.exceptions.DigitronException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
        stage.setTitle("Registration form");
        stage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
        stage.setResizable(false);
        stage.show();
    }

    public void onPrijaviSeBtnClicked(ActionEvent actionEvent) throws DigitronException, IOException {
        Korisnik korisnik = DaoFactory.korisnikDao().getKorisnikByUsername(textFld.getText());
        if(korisnik == null || !korisnik.getPassword().equals(passwordFld.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login failed");
            alert.setContentText("Username or password is wrong!");
            alert.showAndWait();
        }
        else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/digitron.fxml"));
            Parent root = loader.load();
            DigitronController digitronController = loader.getController();
            digitronController.setKorisnik(korisnik);

            Stage stage = new Stage();
            stage.setTitle("Digitron");
            stage.setScene(new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE));
            stage.setMinWidth(600);
            stage.setMinHeight(400);
            stage.show();
        }
    }
}
