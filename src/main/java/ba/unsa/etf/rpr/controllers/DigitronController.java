package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.dao.OmiljenaOperacijaDaoSQLImpl;
import ba.unsa.etf.rpr.dao.RacunDaoSQLImpl;
import ba.unsa.etf.rpr.domain.Korisnik;
import ba.unsa.etf.rpr.domain.OmiljenaOperacija;
import ba.unsa.etf.rpr.domain.Racun;
import ba.unsa.etf.rpr.exceptions.DigitronException;
import ba.unsa.etf.rpr.models.OmiljenaOperacijaModel;
import ba.unsa.etf.rpr.models.RacunModel;
import ba.unsa.etf.rpr.models.RacuniModel;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DigitronController {
    public Label display;
    public ListView historyView;
    public Label omiljenaOperacijaLabel;
    public Label brojPonavljanjaLabel;
    private Korisnik korisnik;
    private RacunDaoSQLImpl racunDaoSQL;
    private OmiljenaOperacijaDaoSQLImpl omiljenaOperacijaDaoSQL;
    private RacuniModel racuniModel = new RacuniModel();
    private OmiljenaOperacijaModel omiljenaOperacijaModel;

    public class XCell extends ListCell<RacunModel> {
        GridPane gridPane = new GridPane();
        Label rezultat = new Label("");
        Label datum = new Label("");
        Button button = new Button();

        public XCell() {
            super();
            ImageView image = new ImageView("slike/kanta.png");
            image.setFitHeight(15);
            image.setFitWidth(15);
            image.setPreserveRatio(true);
            button.setGraphic(image);
            gridPane.add(rezultat, 0, 0);
            datum.setFont(Font.font(10));
            datum.setTextFill(Color.LIGHTSLATEGRAY);
            gridPane.add(datum, 0, 1);
            gridPane.add(button, 1, 0, 1, 2);
            ColumnConstraints leftCol = new ColumnConstraints();
            ColumnConstraints rightCol = new ColumnConstraints();
            rightCol.setHalignment(HPos.RIGHT);
            rightCol.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().addAll(leftCol, rightCol);
            button.setOnAction(event ->
            {
                try {
                    racunDaoSQL.delete(getItem().getId());
                }
                catch (DigitronException e) {
                    e.printStackTrace();
                }
                getListView().getItems().remove(getItem());
            }
            );
        }

        @Override
        protected void updateItem(RacunModel item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);
            if(item != null && !empty) {
                rezultat.setText(item.getRezultat());
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                datum.setText(dtf.format(Instant.ofEpochMilli(item.getDatum().getTime())
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()));
                setGraphic(gridPane);
            }
        }
    }

    public DigitronController() {
        racunDaoSQL = new RacunDaoSQLImpl();
        omiljenaOperacijaDaoSQL = new OmiljenaOperacijaDaoSQLImpl();
    }
    public void setKorisnik(Korisnik korisnik) throws DigitronException {
        this.korisnik = korisnik;
        historyView.setCellFactory(param -> new XCell());
        ObservableList<RacunModel> racuni = FXCollections.observableArrayList(racunDaoSQL.getRacuniByKorisnikId(korisnik.getId())
                .stream().map(RacunModel::new).collect(Collectors.toList()));
        racuniModel.setRacuni(racuni);
        historyView.setItems(racuniModel.getRacuni());
        historyView.getItems().addListener((ListChangeListener<? super RacunModel>) observable -> {
            try {
                /*IntStream racuns = racunDaoSQL.getRacuniByKorisnikId(korisnik.getId()).stream().map(Racun::getRezultat).reduce(String::concat).
                        get().chars().map(x -> (char) x);*/
                long brPluseva = racunDaoSQL.getRacuniByKorisnikId(korisnik.getId()).stream().map(Racun::getRezultat).reduce(String::concat).
                        get().chars().map(x -> (char) x).filter(c -> c == '+').count();
                long brMinusa = racunDaoSQL.getRacuniByKorisnikId(korisnik.getId()).stream().map(Racun::getRezultat).reduce(String::concat).
                        get().chars().map(x -> (char) x).filter(c -> c == '-').count();
                long brDijeljenja = racunDaoSQL.getRacuniByKorisnikId(korisnik.getId()).stream().map(Racun::getRezultat).reduce(String::concat).
                        get().chars().map(x -> (char) x).filter(c -> c == '/').count();
                long brMnozenja = racunDaoSQL.getRacuniByKorisnikId(korisnik.getId()).stream().map(Racun::getRezultat).reduce(String::concat).
                        get().chars().map(x -> (char) x).filter(c -> c == '*').count();
                String operacija = "+";
                long max = brPluseva;
                if(brMinusa > max) {
                    max = brMinusa;
                    operacija = "-";
                }
                if(brDijeljenja > max) {
                    max = brDijeljenja;
                    operacija = "/";
                }
                if(brMnozenja > max) {
                    max = brMnozenja;
                    operacija = "*";
                }
                OmiljenaOperacija update = omiljenaOperacijaDaoSQL.getOmiljenaOperacijaByKorisnikId(korisnik.getId());
                update.setBrojPonavljanja((int) max);
                update.setOperacija(operacija);
                omiljenaOperacijaDaoSQL.update(update);
                //ovo prije
                omiljenaOperacijaModel.setOperacija(operacija);
                omiljenaOperacijaModel.setBrojPonavljanja((int) max);
            } catch (DigitronException e) {
                e.printStackTrace();
            }
        });
        omiljenaOperacijaModel = new OmiljenaOperacijaModel(omiljenaOperacijaDaoSQL.getOmiljenaOperacijaByKorisnikId(korisnik.getId()));
        omiljenaOperacijaLabel.textProperty().bind(omiljenaOperacijaModel.operacijaProperty());
        brojPonavljanjaLabel.textProperty().bind(omiljenaOperacijaModel.brojPonavljanjaProperty().asString());
    }

    @FXML
    public void initialize() throws DigitronException {
        Label praznaHistorija = new Label("Calculation history is empty");
        praznaHistorija.setTextFill(Color.SLATEBLUE);
        historyView.setPlaceholder(praznaHistorija);
    }

    // prva verzija funkcije koja parsira izraz, zatim ga racuna / baca izuzetak ukoliko izraz nije valjan ili se javlja dijeljenje nulom
    // podrzane su osnovne aritmeticke operacije (+, -, *, /)
    // podrzan je prioritet izvrsavanja operacija
    // nije podrzano koristenje zagrada
    // nije podrzan unarni minus

    private static String evaluate(String izraz) {
        try {
            Double.parseDouble(izraz);
            return izraz + ".0"; // ako je izraz vec broj, odmah se vraca
        }
        catch(Exception ignored) {

        }

        // lista koja sadrzi sve argumente osnovnih aritmetickih operacija koje se javljaju u izrazu
        List<String> listaArgumenata = new ArrayList<>(Arrays.asList(izraz.split(" \\+ | \\* | \\- | \\/ ")));
        // lista koja sadrzi sve osnovne aritmeticke operacije koje se javljaju u izrazu
        List<String> listaOperacija = new ArrayList<>(Arrays.asList(Arrays.stream(izraz.split("[+-]?([0-9]*[.]?[0-9]+|[0-9]+[.]?[0-9]*)"))
                .filter(s -> !(s.isBlank()))
                .toArray(String[]::new)));

        // posto su svi podrzani operatori binarni, valjan izraz ce uvijek imati broj operatora za jedan manji od broja argumenata
        if(listaOperacija.size() >= listaArgumenata.size()) return "ERROR: Invalid syntax";
        double rezultat = 0;
        try {
            for(int i = 0; i < listaArgumenata.size() - 1; i++) {
                double op1 = Double.parseDouble(listaArgumenata.get(i));
                if(i != 0) op1 = rezultat;
                int t = i + 1;
                while(t < listaOperacija.size() && (listaOperacija.get(t).trim().equals("*") || listaOperacija.get(t).trim().equals("/"))) {
                    if(listaOperacija.get(t).trim().equals("*"))
                        listaArgumenata.set(t,
                                String.valueOf(Double.parseDouble(listaArgumenata.get(t)) * Double.parseDouble(listaArgumenata.get(t + 1))));
                    else
                        listaArgumenata.set(t,
                                String.valueOf(Double.parseDouble(listaArgumenata.get(t)) / Double.parseDouble(listaArgumenata.get(t + 1))));
                    listaOperacija.remove(t);
                    listaArgumenata.remove(t + 1);
                }
                double op2 = Double.parseDouble(listaArgumenata.get(i + 1));
                String znak = listaOperacija.get(i).trim();
                if(znak.equals("+")) rezultat = (op1 + op2);
                else if(znak.equals("-")) rezultat = (op1 - op2);
                else if(znak.equals("*")) rezultat = (op1 * op2);
                else rezultat = (op1 / op2);
            }
            if(Double.valueOf(rezultat).isInfinite() || Double.valueOf(rezultat).isNaN()) return "ERROR: Division by zero";
            else return String.valueOf(rezultat);
        }
        catch (NumberFormatException e) {
            return "ERROR: Invalid syntax";
        }
    }

    public void btn0Click(ActionEvent actionEvent) {
        String tekst = display.getText();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("0");
        else {
            display.setText(tekst + "0");
        }
    }

    public void btn1Click(ActionEvent actionEvent) {
        String tekst = display.getText();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("1");
        else {
            display.setText(tekst + "1");
        }
    }

    public void btn2Click(ActionEvent actionEvent) {
        String tekst = display.getText();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("2");
        else {
            display.setText(tekst + "2");
        }
    }

    public void btn3Click(ActionEvent actionEvent) {
        String tekst = display.getText();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("3");
        else {
            display.setText(tekst + "3");
        }
    }

    public void btn4Click(ActionEvent actionEvent) {
        String tekst = display.getText();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("4");
        else {
            display.setText(tekst + "4");
        }
    }

    public void btn5Click(ActionEvent actionEvent) {
        String tekst = display.getText();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("5");
        else {
            display.setText(tekst + "5");
        }
    }

    public void btn6Click(ActionEvent actionEvent) {
        String tekst = display.getText();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("6");
        else {
            display.setText(tekst + "6");
        }
    }

    public void btn7Click(ActionEvent actionEvent) {
        String tekst = display.getText();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("7");
        else {
            display.setText(tekst + "7");
        }
    }

    public void btn8Click(ActionEvent actionEvent) {
        String tekst = display.getText();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("8");
        else {
            display.setText(tekst + "8");
        }
    }

    public void btn9Click(ActionEvent actionEvent) {
        String tekst = display.getText();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("9");
        else {
            display.setText(tekst + "9");
        }
    }

    public void dotBtnClicked(ActionEvent actionEvent) {
        String tekst = display.getText();
        display.setText(tekst + ".");
    }

    public void minusBtnClicked(ActionEvent actionEvent) {
        String tekst = display.getText();
        display.setText(tekst + " - ");
    }

    public void multipliesBtnClicked(ActionEvent actionEvent) {
        String tekst = display.getText();
        display.setText(tekst + " * ");
    }

    public void dividesBtnClicked(ActionEvent actionEvent) {
        String tekst = display.getText();
        display.setText(tekst + " / ");
    }

    public void plusBtnClicked(ActionEvent actionEvent) {
        String tekst = display.getText();
        display.setText(tekst + " + ");
    }

    public void equalsBtnClicked(ActionEvent actionEvent) throws DigitronException {
        String rez = display.getText() + " = " + evaluate(display.getText());
        if(!rez.contains("ERROR")) {
            Racun racun = new Racun();
            racun.setRezultat(rez);
            racun.setIdKorisnik(korisnik.getId());
            LocalDateTime localDateTime = LocalDateTime.now();
            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("UTC"));
            Instant instant = zonedDateTime.toInstant();
            java.util.Date date = java.util.Date.from(instant);
            java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
            racun.setDatum(timestamp);
            racun = racunDaoSQL.add(racun);
            historyView.getItems().add(new RacunModel(racun));
        }
        display.setText(rez);
    }

    public void cBtnClicked(ActionEvent actionEvent) {
        display.setText("0");
    }
}