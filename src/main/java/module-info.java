module ba.unsa.etf.rpr {
    requires java.sql;
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.postgresql.jdbc;

    opens ba.unsa.etf.rpr to javafx.fxml;
    exports ba.unsa.etf.rpr;
}