package ba.unsa.etf.rpr.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;

public class GraphingController {
    public BorderPane borderPane;

    @FXML
    public void initialize() {
        NumberAxis xAxis = new NumberAxis("X", 0, 2*Math.PI, Math.PI/4);
        NumberAxis yAxis = new NumberAxis("Y", -1, 1, 0.2);

        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Draw a graph of trig functions");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("");
        lineChart.getData().add(series);
        borderPane.centerProperty().setValue(lineChart);
    }

    public void drawSin() {
        NumberAxis xAxis = new NumberAxis("X", 0, 2*Math.PI, Math.PI/4);
        NumberAxis yAxis = new NumberAxis("Y", -1, 1, 0.2);


        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Sine Function Graph");


        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("y = sin(x)");
        for(double x = 0; x <= 2*Math.PI; x += 0.1) {
            double y = Math.sin(x);
            series.getData().add(new XYChart.Data<>(x, y));
        }

        lineChart.getData().add(series);
        borderPane.centerProperty().setValue(lineChart);
    }

    public void drawCos(ActionEvent actionEvent) {
        NumberAxis xAxis = new NumberAxis("X", 0, 2*Math.PI, Math.PI/4);
        NumberAxis yAxis = new NumberAxis("Y", -1, 1, 0.2);


        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Cosine Function Graph");


        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("y = cos(x)");
        for(double x = 0; x <= 2*Math.PI; x += 0.1) {
            double y = Math.cos(x);
            series.getData().add(new XYChart.Data<>(x, y));
        }
        /*NumberAxis xAxis = new NumberAxis("X", 0, 10, 1);
        NumberAxis yAxis = new NumberAxis("Y", 0, 25, 5);


        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);


        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("y = 2x + 1");
        for (int x = 0; x <= 10; x++) {
            int y = 2 * x + 1;
            series.getData().add(new XYChart.Data<>(x, y));
        }*/

        lineChart.getData().add(series);
        borderPane.centerProperty().setValue(lineChart);
    }

    public void drawTan() {
        NumberAxis xAxis = new NumberAxis("X", -6*Math.PI, 6*Math.PI, Math.PI/4);
        NumberAxis yAxis = new NumberAxis("Y", -50, 50, 0.2);


        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Tangent Function Graph");


        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("y = tan(x)");
        for(double x = -6*Math.PI; x <= 6*Math.PI; x += 0.5) {
            Double y = Math.tan(x);
            if(y.isInfinite() || y.isNaN()) continue;
            series.getData().add(new XYChart.Data<>(x, y));
        }

        lineChart.getData().add(series);
        borderPane.centerProperty().setValue(lineChart);
    }

    public void drawCot() {
        NumberAxis xAxis = new NumberAxis("X", -6*Math.PI, 6*Math.PI, Math.PI/4);
        NumberAxis yAxis = new NumberAxis("Y", -50, 50, 0.2);


        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Contangent Function Graph");


        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("y = cot(x)");
        for(double x = -6*Math.PI; x <= 6*Math.PI; x += 0.1) {
            Double y = 1 / Math.tan(x);
            if(y.isInfinite() || y.isNaN()) continue;
            series.getData().add(new XYChart.Data<>(x, y));
        }

        lineChart.getData().add(series);
        borderPane.centerProperty().setValue(lineChart);
    }
}
