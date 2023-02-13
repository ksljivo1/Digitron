package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.dao.OmiljenaOperacijaDaoSQLImpl;
import ba.unsa.etf.rpr.dao.RacunDaoSQLImpl;
import ba.unsa.etf.rpr.domain.Korisnik;
import ba.unsa.etf.rpr.domain.OmiljenaOperacija;
import ba.unsa.etf.rpr.domain.Racun;
import ba.unsa.etf.rpr.evaluation.DigitronParser;
import ba.unsa.etf.rpr.exceptions.DigitronException;
import ba.unsa.etf.rpr.models.OmiljenaOperacijaModel;
import ba.unsa.etf.rpr.models.RacunModel;
import ba.unsa.etf.rpr.models.RacuniModel;
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

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DigitronController {
    public Label display;
    public ListView historyView;
    public Label omiljenaOperacijaLabel;
    public Label brojPonavljanjaLabel;
    public Label resultLabel;
    public Button backspaceBtn;
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
            });
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
                List<String> results = racunDaoSQL.getRacuniByKorisnikId(korisnik.getId()).stream().map(Racun::getRezultat).collect(Collectors.toList());
                Optional<String> combined = results.stream().reduce(String::concat);

                long brPluseva = combined.map(s -> s.chars().filter(c -> c == '+')).orElseGet(IntStream::empty).count();
                long brMinusa = combined.map(s -> s.chars().filter(c -> c == '-')).orElseGet(IntStream::empty).count();
                long brDijeljenja = combined.map(s -> s.chars().filter(c -> c == '/')).orElseGet(IntStream::empty).count();
                long brMnozenja = combined.map(s -> s.chars().filter(c -> c == '*')).orElseGet(IntStream::empty).count();

                String operacija = "";
                long max = 0;
                if(brPluseva > max) {
                    max = brPluseva;
                    operacija = "+";
                }
                if(brMinusa > max) {
                    max = brMinusa;
                    operacija = "-";
                }
                if(brMnozenja > max) {
                    max = brMnozenja;
                    operacija = "*";
                }
                if(brDijeljenja > max) {
                    max = brDijeljenja;
                    operacija = "/";
                }

                omiljenaOperacijaModel.setOperacija(operacija);
                omiljenaOperacijaModel.setBrojPonavljanja((int) max);

                OmiljenaOperacija update = omiljenaOperacijaDaoSQL.getOmiljenaOperacijaByKorisnikId(korisnik.getId());
                update.setBrojPonavljanja((int) max);
                update.setOperacija(operacija);
                omiljenaOperacijaDaoSQL.update(update);
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

        ImageView image = new ImageView("slike/backspace.png");
        image.setFitHeight(15);
        image.setFitWidth(15);
        image.setPreserveRatio(true);
        backspaceBtn.setGraphic(image);
    }

    public void btn0Click(ActionEvent actionEvent) {
        String tekst = display.getText().strip();
        if(tekst.equals("0") || tekst.contains("=") || tekst.contains("r")) display.setText("0");
        else if(!tekst.isEmpty() && (Character.isDigit(tekst.charAt(tekst.length() - 1)) || tekst.charAt(tekst.length() - 1) == '.')) {
            display.setText(tekst + "0");
        }
        else display.setText(tekst + " 0");
    }

    public void btn1Click(ActionEvent actionEvent) {
        String tekst = display.getText().strip();
        if(tekst.equals("0") || tekst.contains("=") || tekst.contains("r")) display.setText("1");
        else if(!tekst.isEmpty() && (Character.isDigit(tekst.charAt(tekst.length() - 1)) || tekst.charAt(tekst.length() - 1) == '.')) {
            display.setText(tekst + "1");
        }
        else display.setText(tekst + " 1");
    }

    public void btn2Click(ActionEvent actionEvent) {
        String tekst = display.getText().strip();
        if(tekst.equals("0") || tekst.contains("=") || tekst.contains("r")) display.setText("2");
        else if(!tekst.isEmpty() && (Character.isDigit(tekst.charAt(tekst.length() - 1)) || tekst.charAt(tekst.length() - 1) == '.')) {
            display.setText(tekst + "2");
        }
        else display.setText(tekst + " 2");
    }

    public void btn3Click(ActionEvent actionEvent) {
        String tekst = display.getText().strip();
        if(tekst.equals("0") || tekst.contains("=") || tekst.contains("r")) display.setText("3");
        else if(!tekst.isEmpty() && (Character.isDigit(tekst.charAt(tekst.length() - 1)) || tekst.charAt(tekst.length() - 1) == '.')) {
            display.setText(tekst + "3");
        }
        else display.setText(tekst + " 3");
    }

    public void btn4Click(ActionEvent actionEvent) {
        String tekst = display.getText().strip();
        if(tekst.equals("0") || tekst.contains("=") || tekst.contains("r")) display.setText("4");
        else if(!tekst.isEmpty() && (Character.isDigit(tekst.charAt(tekst.length() - 1)) || tekst.charAt(tekst.length() - 1) == '.')) {
            display.setText(tekst + "4");
        }
        else display.setText(tekst + " 4");
    }

    public void btn5Click(ActionEvent actionEvent) {
        String tekst = display.getText().strip();
        if(tekst.equals("0") || tekst.contains("=") || tekst.contains("r")) display.setText("5");
        else if(!tekst.isEmpty() && (Character.isDigit(tekst.charAt(tekst.length() - 1)) || tekst.charAt(tekst.length() - 1) == '.')) {
            display.setText(tekst + "5");
        }
        else display.setText(tekst + " 5");
    }

    public void btn6Click(ActionEvent actionEvent) {
        String tekst = display.getText().strip();
        if(tekst.equals("0") || tekst.contains("=") || tekst.contains("r")) display.setText("6");
        else if(!tekst.isEmpty() && (Character.isDigit(tekst.charAt(tekst.length() - 1)) || tekst.charAt(tekst.length() - 1) == '.')) {
            display.setText(tekst + "6");
        }
        else display.setText(tekst + " 6");
    }

    public void btn7Click(ActionEvent actionEvent) {
        String tekst = display.getText().strip();
        if(tekst.equals("0") || tekst.contains("=") || tekst.contains("r")) display.setText("7");
        else if(!tekst.isEmpty() && (Character.isDigit(tekst.charAt(tekst.length() - 1)) || tekst.charAt(tekst.length() - 1) == '.')) {
            display.setText(tekst + "7");
        }
        else display.setText(tekst + " 7");
    }

    public void btn8Click(ActionEvent actionEvent) {
        String tekst = display.getText().strip();
        if(tekst.equals("0") || tekst.contains("=") || tekst.contains("r")) display.setText("8");
        else if(!tekst.isEmpty() && (Character.isDigit(tekst.charAt(tekst.length() - 1)) ||tekst.charAt(tekst.length() - 1) == '.')) {
            display.setText(tekst + "8");
        }
        else display.setText(tekst + " 8");
    }

    public void btn9Click(ActionEvent actionEvent) {
        String tekst = display.getText().strip();
        if(tekst.equals("0") || tekst.contains("=") || tekst.contains("r")) display.setText("9");
        else if(!tekst.isEmpty() && (Character.isDigit(tekst.charAt(tekst.length() - 1)) || tekst.charAt(tekst.length() - 1) == '.')) {
            display.setText(tekst + "9");
        }
        else display.setText(tekst + " 9");
    }

    public void dotBtnClicked(ActionEvent actionEvent) {
        String tekst = display.getText();
        display.setText(tekst + ".");
    }

    public void minusBtnClicked(ActionEvent actionEvent) {
        String tekst = display.getText().strip();
        display.setText(tekst + " - ");
    }

    public void multipliesBtnClicked(ActionEvent actionEvent) {
        String tekst = display.getText().strip();
        display.setText(tekst + " * ");
    }

    public void dividesBtnClicked(ActionEvent actionEvent) {
        String tekst = display.getText().strip();
        display.setText(tekst + " / ");
    }

    public void plusBtnClicked(ActionEvent actionEvent) {
        String tekst = display.getText().strip();
        display.setText(tekst + " + ");
    }

    public void equalsBtnClicked(ActionEvent actionEvent) throws DigitronException {
        String expr = display.getText();
        try {
            DigitronParser digitronParser = new DigitronParser(expr);
            double rez = Double.parseDouble(digitronParser.evaluate().peek().getValue()) + 0.0;
            resultLabel.setText(expr + " = ");
            display.setText(rez + "");
            Racun racun = new Racun();
            racun.setRezultat(expr + " = " + rez);
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
        catch(IOException ioException) {
            resultLabel.setText(expr + " = ");
            display.setText(ioException.getMessage());
        }
    }

    public void leftParenthesisClick(ActionEvent actionEvent) {
        String tekst = display.getText().strip();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("(");
        else {
            display.setText(tekst + " (");
        }
    }

    public void rightParenthesisClick(ActionEvent actionEvent) {
        String tekst = display.getText().strip();
        if(tekst.equals("0") || tekst.contains("=")) display.setText(")");
        else {
            display.setText(tekst + " )");
        }
    }

    public void backspaceClick(ActionEvent actionEvent) {
        String text = display.getText().strip();
        if(text.isEmpty()) return;
        int n = text.length() - 1;
        while(n > 0 && text.charAt(n) == ' ') n--;
        text = text.substring(0, n);
        text = text.strip();
        display.setText(text);
    }

    public void cBtnClicked(ActionEvent actionEvent) {
        display.setText("0");
    }
}