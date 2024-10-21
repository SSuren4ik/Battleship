module org.example.battleship {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.rmi;

    opens org.example.battleship to javafx.fxml;
    exports org.example.battleship.server;
    exports org.example.battleship.client;
    opens org.example.battleship.client to javafx.fxml;
}