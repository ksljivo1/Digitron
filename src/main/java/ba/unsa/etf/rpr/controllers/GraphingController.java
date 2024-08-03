package ba.unsa.etf.rpr.controllers;

import ba.unsa.etf.rpr.evaluation.DigitronParser;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.ZoomEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
//import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;

import java.io.IOException;

public class GraphingController {
    public BorderPane borderPane;
    public TextField input;
    private double scaleFactor = 1;

    @FXML
    public void initialize() {
        borderPane.setOnScroll(event -> {
            scaleFactor = scaleFactor * (event.getDeltaY() > 0 ? 0.9 : 1.1);
            String text = input.getText();

            PauseTransition pauseTransition = new PauseTransition(Duration.millis(10));
            pauseTransition.setOnFinished(e -> renderGraphAsync(text));
            pauseTransition.play();
        });

        input.textProperty().addListener((observable, oldValue, newValue) -> {
            PauseTransition pauseTransition = new PauseTransition(Duration.millis(10));
            pauseTransition.setOnFinished(e -> renderGraphAsync(input.getText()));
            pauseTransition.play();
        });

    }

    /*@FXML
    public void initialize() {
       /* borderPane.setOnScroll(event -> {
            scaleFactor = scaleFactor * (event.getDeltaY() > 0 ? 0.9 : 1.1);
            String text = input.getText();
            input.setText("0");
            input.setText(text);
        });

        input.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                NumberAxis xAxis = new NumberAxis("X", -10 * scaleFactor, 10 * scaleFactor, 0.1);
                NumberAxis yAxis = new NumberAxis("Y", -10 * scaleFactor, 10 * scaleFactor, 0.1);

                LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
                lineChart.setTitle("Function Graph");
                XYChart.Series<Number, Number> series = new XYChart.Series<>();
                lineChart.setCreateSymbols(false);
                series.setName("y = f(x)");

                for(double x = -10 * scaleFactor; x <= 10 * scaleFactor; x += 0.01) {
                    String roundedNumber = String.format("%.5f", x);
                    var value = Double.parseDouble(roundedNumber);
                    String inputA = newValue == null ? "" : newValue;
                    inputA = inputA.replaceAll("x", "(" + value + ")");
                    DigitronParser digitronParser = new DigitronParser(inputA);
                    double y = 0;
                    try {
                        y = Double.parseDouble(digitronParser.evaluate().peek().getValue());
                    }
                    catch (IOException ioException){
                        if(ioException.getMessage().contains("Division")) continue;
                    }
                    series.getData().add(new XYChart.Data<>(x, y));
                }
                lineChart.getData().add(series);
                borderPane.centerProperty().setValue(lineChart);
            } catch (Exception ignored) {
                ignored.printStackTrace();
            }

        });
    }*/


    private void renderGraphAsync(String newValue) {
        Task<Void> renderTask = new Task<>() {
            @Override
            protected Void call() {
                try {
                    // Your graph rendering code here

                    // NumberAxis xAxis = new NumberAxis("X", 0, 2*Math.PI, Math.PI/4);
                    NumberAxis xAxis = new NumberAxis("X", -10 * scaleFactor, 10 * scaleFactor, 0.1);
                    NumberAxis yAxis = new NumberAxis("Y", -10 * scaleFactor, 10 * scaleFactor, 0.1);

                    LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
                    lineChart.setTitle("Function Graph");
                    XYChart.Series<Number, Number> series = new XYChart.Series<>();
                    lineChart.setCreateSymbols(false);
                    series.setName("y = f(x)");

                    //SplineInterpolator splineInterpolator = new SplineInterpolator();

                    var low = -10 * scaleFactor;
                    var high = 10 * scaleFactor;
                    var increment = 0.1 * scaleFactor;
                    for (double x = low; x < high; x += increment) {
                        String roundedNumber = String.format("%.5f", x);
                        var value = Double.parseDouble(roundedNumber);
                        String inputA = newValue == null ? "" : newValue;
                        inputA = inputA.replaceAll("x", "(" + value + ")");
                        DigitronParser digitronParser = new DigitronParser(inputA);
                        double y = 0;
                        try {
                            y = Double.parseDouble(digitronParser.evaluate().peek().getValue());
                        }
                        catch (IOException ioException){
                            if(ioException.getMessage().contains("Division")) continue;
                        }

                        series.getData().add(new XYChart.Data<>(x, y));
                    }

                    // Update the UI with the new graph
                    Platform.runLater(() -> {
                        lineChart.getData().clear();
                        lineChart.getData().add(series);
                        borderPane.setCenter(lineChart);
                    });

                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }

                return null;
            }
        };

        Thread renderThread = new Thread(renderTask);
        renderThread.setDaemon(true);
        renderThread.start();
    }
}
