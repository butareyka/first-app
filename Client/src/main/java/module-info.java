module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    requires org.apache.commons.lang3;
    requires org.jline.reader;
    requires org.jline.terminal;

    opens org.example.models to javafx.base;

    opens org.example to javafx.fxml;
    exports org.example;
    exports org.example.stages;
    opens org.example.stages to javafx.fxml;
}
