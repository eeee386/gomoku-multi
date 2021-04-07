module hu.alkfejl.controller {
    requires javafx.controls;
    requires javafx.fxml;
    requires hu.alkfejl.model;

    opens hu.alkfejl.controller to javafx.fxml;
    exports hu.alkfejl.controller;
}