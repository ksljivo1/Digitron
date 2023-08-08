package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.ParallelCount;
import ba.unsa.etf.rpr.dao.DaoFactory;
import ba.unsa.etf.rpr.dao.RacunDao;
import ba.unsa.etf.rpr.domain.Korisnik;
import ba.unsa.etf.rpr.domain.OmiljenaOperacija;
import ba.unsa.etf.rpr.domain.Racun;
import ba.unsa.etf.rpr.evaluation.DigitronParser;
import ba.unsa.etf.rpr.exceptions.DigitronException;
import ba.unsa.etf.rpr.models.OmiljenaOperacijaModel;
import ba.unsa.etf.rpr.models.RacunModel;
import ba.unsa.etf.rpr.models.RacuniModel;
import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * JavaFX controller for alteration and deletion of calculations
 *
 * @author Dino Keco
 */

public class DigitronController {
    private final RacunDao racunDao = DaoFactory.racunDao();
    public Label display;
    public ListView historyView;
    public Label omiljenaOperacijaLabel;
    public Label brojPonavljanjaLabel;
    public Label resultLabel;
    public Button backspaceBtn;
    public GridPane background;
    public RadioButton radioBtn;

    public GridPane gridPane;
    public HBox hBox1;
    public HBox hbox2;
    public GridPane gridPane2;
    public VBox vBox;
    public Button equalsBtn;
    private Korisnik korisnik;
    private RacuniModel racuniModel = new RacuniModel();
    private OmiljenaOperacijaModel omiljenaOperacijaModel;

    public void tanBtnClicked(ActionEvent actionEvent) {
        String tekst = display.getText().strip();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("tan");
        else {
            display.setText(tekst + " tan");
        }
    }

    public void cotBtnClicked(ActionEvent actionEvent) {
        String tekst = display.getText().strip();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("cot");
        else {
            display.setText(tekst + " cot");
        }
    }

    public void sinBtnClicked(ActionEvent actionEvent) {
        String tekst = display.getText().strip();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("sin");
        else display.setText(tekst + " sin");
    }

    public void cosBtnClicked(ActionEvent actionEvent) {
        String tekst = display.getText().strip();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("cos");
        else {
            display.setText(tekst + " cos");
        }
    }

    public void graphBtnClicked(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/graphing.fxml"));

        Stage stage = new Stage();
        stage.setTitle("Digitron");
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.setTitle("Graphing");
        //stage.setResizable(false);

        Scene scene = new Scene(root, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        stage.setScene(scene);

        stage.show();
    }

    /**
     * Helper cell factory class for ListView control
     */
    public class XCell extends ListCell<RacunModel> {
        GridPane gridPane = new GridPane();
        Label rezultat = new Label("");
        Label datum = new Label("");
        Button button = new Button();

        public XCell() {
            super();
            rezultat.setId("rezultat");
            datum.setId("datum");

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
                    racunDao.delete(getItem().getId());
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

    public DigitronController() {}

    public void setKorisnik(Korisnik korisnik) throws DigitronException {
        this.korisnik = korisnik;
        historyView.setCellFactory(param -> new XCell());
        ObservableList<RacunModel> racuni = FXCollections.observableArrayList(racunDao.getRacuniByKorisnikId(korisnik.getId())
                .stream().map(RacunModel::new).collect(Collectors.toList()));
        racuniModel.setRacuni(racuni);
        historyView.setItems(racuniModel.getRacuni());
        historyView.getItems().addListener((ListChangeListener<? super RacunModel>) observable -> {
            try {
                List<String> results = racunDao.
                        getRacuniByKorisnikId(korisnik.getId()).stream().map(racun -> {
                            String str = racun.getRezultat();
                            return str.substring(0, str.indexOf('='));
                        }).collect(Collectors.toList());
                Optional<String> combined = results.stream().reduce(String::concat);

                ParallelCount searchPlus = new ParallelCount('+', combined);
                ParallelCount searchMinus = new ParallelCount('-', combined);
                ParallelCount searchMultiply = new ParallelCount('⨯', combined);
                ParallelCount searchDivide = new ParallelCount('/', combined);

                searchPlus.start();
                searchMinus.start();
                searchMultiply.start();
                searchDivide.start();

                try {
                    searchPlus.join();
                    searchMinus.join();
                    searchMultiply.join();
                    searchDivide.join();
                } catch (InterruptedException ignored) {

                }

                long brPluseva = searchPlus.getBroj();
                long brMinusa = searchMinus.getBroj();
                long brMnozenja = searchMultiply.getBroj();
                long brDijeljenja = searchDivide.getBroj();

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
                    operacija = "⨯";
                }
                if(brDijeljenja > max) {
                    max = brDijeljenja;
                    operacija = "/";
                }

                omiljenaOperacijaModel.setOperacija(operacija);
                omiljenaOperacijaModel.setBrojPonavljanja((int) max);

                OmiljenaOperacija update = DaoFactory.omiljenaOperacijaDao().getOmiljenaOperacijaByKorisnikId(korisnik.getId());
                update.setBrojPonavljanja((int) max);
                update.setOperacija(operacija);
                DaoFactory.omiljenaOperacijaDao().update(update);
            } catch (DigitronException e) {
                e.printStackTrace();
            }
        });
        omiljenaOperacijaModel = new OmiljenaOperacijaModel(DaoFactory.omiljenaOperacijaDao().getOmiljenaOperacijaByKorisnikId(korisnik.getId()));
        omiljenaOperacijaLabel.textProperty().bind(omiljenaOperacijaModel.operacijaProperty());
        brojPonavljanjaLabel.textProperty().bind(omiljenaOperacijaModel.brojPonavljanjaProperty().asString());
        if(korisnik.isMode()) lightMode();
        else {
            darkMode();
            radioBtn.fire();
        }
        radioBtn.selectedProperty().addListener((observableValue, wasSelected, isSelected) -> {
            if(isSelected) darkMode();
            else lightMode();
            korisnik.setMode(!isSelected);
            try {
                DaoFactory.korisnikDao().update(korisnik);
            } catch (DigitronException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    private void lightMode() {
        historyView.getStylesheets().removeAll("/css/historyview-black-mode.css");
        background.getStyleClass().removeAll("darkModeBackground");
        background.getStyleClass().add("lightModeBackground");
        hBox1.getChildren().forEach(node -> {
            if(node instanceof Label) ((Label) node).setTextFill(Color.BLACK);
        });
        hbox2.getChildren().forEach(node -> {
            if(node instanceof Label) ((Label) node).setTextFill(Color.BLACK);
        });
        vBox.getChildren().forEach(node -> {
            if(node instanceof Label) ((Label) node).setTextFill(Color.BLACK);
        });
        ImageView image = new ImageView("slike/backspace.png");
        image.setFitHeight(15);
        image.setFitWidth(15);
        image.setPreserveRatio(true);
        backspaceBtn.setGraphic(image);
        gridPane2.getChildren().forEach(node -> {
            if(node instanceof Button && !node.getId().equals(equalsBtn.getId())) {
                node.getStyleClass().removeAll("my-node");
                ((Button) node).setTextFill(Color.BLACK);
            }
        });
        radioBtn.setTextFill(Color.BLACK);
    }

    private void darkMode() {
        historyView.getStylesheets().add("/css/historyview-black-mode.css");
        background.getStyleClass().removeAll("lightModeBackground");
        background.getStyleClass().add("darkModeBackground");
        hBox1.getChildren().forEach(node -> {
            if(node instanceof Label) ((Label) node).setTextFill(Color.WHITE);
        });
        hbox2.getChildren().forEach(node -> {
            if(node instanceof Label) ((Label) node).setTextFill(Color.WHITE);
        });
        vBox.getChildren().forEach(node -> {
            if(node instanceof Label) ((Label) node).setTextFill(Color.WHITE);
        });
        ImageView image = new ImageView("slike/backspace_light.png");
        image.setFitHeight(15);
        image.setFitWidth(15);
        image.setPreserveRatio(true);
        backspaceBtn.setGraphic(image);

        gridPane2.getChildren().forEach(node -> {
            if(node instanceof Button && !node.getId().equals(equalsBtn.getId())) {
                node.getStyleClass().add("my-node");
                ((Button) node).setTextFill(Color.WHITE);
            }
        });
        radioBtn.setTextFill(Color.WHITE);
    }

    @FXML
    public void initialize() {
        Label praznaHistorija = new Label("Calculation history is empty");
        praznaHistorija.setTextFill(Color.SLATEBLUE);
        historyView.setPlaceholder(praznaHistorija);

        ImageView image = new ImageView("slike/backspace.png");
        image.setFitHeight(15);
        image.setFitWidth(15);
        image.setPreserveRatio(true);
        backspaceBtn.setGraphic(image);
    }

    /**
     * Event handler for digit buttons
     * @param event
     */
    public void handleDigitButtonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        String buttonId = clickedButton.getId();
        char digit = buttonId.charAt(buttonId.length() - 1);
        String tekst = display.getText().strip();

        if(tekst.equals("0") || tekst.contains("=") || tekst.contains("r")) display.setText(digit + "");
        else if(!tekst.isEmpty() && (Character.isDigit(tekst.charAt(tekst.length() - 1)) || tekst.charAt(tekst.length() - 1) == '.'))
            display.setText(tekst + digit);
        else display.setText(tekst + " " + digit);
    }

    public void dotBtnClicked() {
        String tekst = display.getText();
        display.setText(tekst + ".");
    }

    public void minusBtnClicked() {
        String tekst = display.getText().strip();
        display.setText(tekst + " - ");
    }

    public void multipliesBtnClicked() {
        String tekst = display.getText().strip();
        display.setText(tekst + " ⨯ ");
    }

    public void dividesBtnClicked() {
        String tekst = display.getText().strip();
        display.setText(tekst + " / ");
    }

    public void plusBtnClicked() {
        String tekst = display.getText().strip();
        display.setText(tekst + " + ");
    }

    /**
     * Event handler for equals button
     * @throws DigitronException
     */
    public void equalsBtnClicked() throws DigitronException {
        String expr = display.getText();
        Animation animation = new Transition() {
            {
                setCycleDuration(Duration.millis(1500));
                setInterpolator(Interpolator.EASE_OUT);
            }
            @Override
            protected void interpolate(double frac) {
                Color vColor = new Color(0.5, 1, 0.5, 1 - frac);
                display.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        };
        try {
            String replace = expr.replaceAll("⨯", "*");
            DigitronParser digitronParser = new DigitronParser(replace);
            double rez = Double.parseDouble(digitronParser.evaluate().peek().getValue()) + 0.0;
            resultLabel.setText(expr + " = ");
            display.setText(rez + "");
            animation.play();

            Racun racun = new Racun();
            racun.setRezultat(expr + " = " + rez + " ");
            racun.setIdKorisnik(korisnik.getId());
            LocalDateTime localDateTime = LocalDateTime.now();
            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("UTC"));
            Instant instant = zonedDateTime.toInstant();
            java.util.Date date = java.util.Date.from(instant);
            java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
            racun.setDatum(timestamp);
            racun = racunDao.add(racun);
            historyView.getItems().add(new RacunModel(racun));
        }
        catch(IOException ioException) {
            resultLabel.setText(expr + " = ");
            display.setText(ioException.getMessage());

            animation = new Transition() {
                {
                    setCycleDuration(Duration.millis(1500));
                    setInterpolator(Interpolator.EASE_OUT);
                }
                @Override
                protected void interpolate(double frac) {
                    Color vColor = new Color(1, 0.5, 0.5, 1 - frac);
                    display.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
                }
            };
            animation.play();
        }
    }

    public void leftParenthesisClick() {
        String tekst = display.getText().strip();
        if(tekst.equals("0") || tekst.contains("=")) display.setText("(");
        else {
            display.setText(tekst + " (");
        }
    }

    public void rightParenthesisClick() {
        String tekst = display.getText().strip();
        if(tekst.equals("0") || tekst.contains("=")) display.setText(")");
        else {
            display.setText(tekst + " )");
        }
    }

    public void backspaceClick() {
        String text = display.getText().strip();
        if(text.isEmpty()) return;
        int n = text.length() - 1;
        while(n > 0 && text.charAt(n) == ' ') n--;
        text = text.substring(0, n);
        text = text.strip();
        display.setText(text);
    }

    public void cBtnClicked() {
        display.setText("0");
    }
}