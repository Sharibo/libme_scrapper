package com.gmail.alexejkrawez.libme_scrapper;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.slf4j.LoggerFactory.getLogger;

public class LibMeScrapper extends Application {

    private static final Logger log = getLogger(LibMeScrapper.class);
    private static final Stage helpStage = new Stage();
    private static String lastOpenedDirectory = "user.home";
    private static String theme = "css/light-style.css";
    private static boolean isMaximized = false;

    protected static String getLastOpenedDirectory() {
        return lastOpenedDirectory;
    }
    protected static void setLastOpenedDirectory(String lastOpenedDirectory) {
        LibMeScrapper.lastOpenedDirectory = lastOpenedDirectory;
    }

    protected static String getTheme() {
        return theme;
    }
    protected static void setTheme(String theme) {
        LibMeScrapper.theme = theme;
    }

    public static Stage getHelpStage() {
        return helpStage;
    }


    public static void main(String[] args) {
        launch();
    }


    @Override
    public void start(Stage stage) throws IOException {

        final String location = System.getProperty("user.dir");
        final String settingsFile = "/settings.properties";
        final Properties properties = new Properties();
        double savedPosX = 300.0;
        double savedPosY = 300.0;
        double savedWidth = 450.0;
        double savedHeight = 160.0;


        try (InputStream settingsStream = new FileInputStream(location + settingsFile)) {
            properties.load(settingsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            savedPosX = Double.parseDouble(properties.getProperty("windowPosX", "300.0"));
            savedPosY = Double.parseDouble(properties.getProperty("windowPosY", "300.0"));
            savedWidth = Double.parseDouble(properties.getProperty("windowWith", "432.0"));
            savedHeight = Double.parseDouble(properties.getProperty("windowHeight", "160.0"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        theme = properties.getProperty("theme", "css/light-style.css");
        lastOpenedDirectory = properties.getProperty("lastOpenedDirectory", System.getProperty("user.home"));


        FXMLLoader fxmlLoader = new FXMLLoader(LibMeScrapper.class.getResource("index.fxml"));
        Scene scene = new Scene(fxmlLoader.load());


        try (InputStream logoStream = getClass().getResourceAsStream("icons/logo.png")) { // TODO другое лого
            Image logo = new Image(logoStream);
            stage.getIcons().add(logo);
        } catch (NullPointerException e) {
            log.error(e.getLocalizedMessage());
        }


        scene.getStylesheets().add(getClass().getResource(theme).toExternalForm());

        stage.setTitle("lib.me scrapper");
        stage.setMinWidth(420.0);
        stage.setMinHeight(230.0);
        stage.setWidth(savedWidth);
        stage.setHeight(savedHeight);
        stage.setX(savedPosX);
        stage.setY(savedPosY);

        stage.setScene(scene);
        stage.show();

        initializeHelpStage();
        setOnExitActions(stage, location, settingsFile, properties);

    }


    private static void setOnExitActions(Stage stage, String location, String settingsFile, Properties properties) {
        stage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {

            if (stage.maximizedProperty().get()) {
                properties.setProperty("theme", theme);
                properties.setProperty("lastOpenedDirectory", lastOpenedDirectory);

                try (FileOutputStream fus = new FileOutputStream(location + settingsFile)) {
                    properties.store(fus, "Saved settings");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                properties.setProperty("windowPosX", Double.toString(stage.getX()));
                properties.setProperty("windowPosY", Double.toString(stage.getY()));
                properties.setProperty("windowWith", Double.toString(stage.getWidth()));
                properties.setProperty("windowHeight", Double.toString(stage.getHeight()));
                properties.setProperty("theme", theme);
                properties.setProperty("lastOpenedDirectory", lastOpenedDirectory);

                try (FileOutputStream fus = new FileOutputStream(location + settingsFile)) {
                    properties.store(fus, "Saved settings");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            helpStage.close();
        });
    }

    private static void initializeHelpStage() {
        FXMLLoader fxmlLoader = new FXMLLoader(LibMeScrapper.class.getResource("help.fxml"));
        Scene scene = null;

        try {
            scene = new Scene(fxmlLoader.load());
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }

        try (InputStream logoStream = ControllerHelper.class.getResourceAsStream("icons/logo.png")) { // TODO другое лого
            javafx.scene.image.Image logo = new Image(logoStream);
            LibMeScrapper.getHelpStage().getIcons().add(logo);
        } catch (NullPointerException | IOException e) {
            log.error(e.getLocalizedMessage());
        }

        if (LibMeScrapper.getTheme().equals("css/light-style.css")) {
            scene.getStylesheets().add(ControllerHelper.class.getResource("css/help-light-style.css").toExternalForm());
        } else {
            scene.getStylesheets().add(ControllerHelper.class.getResource("css/help-dark-style.css").toExternalForm());
        }

        LibMeScrapper.getHelpStage().setTitle("Справка");
        LibMeScrapper.getHelpStage().setMinHeight(450.0);
        LibMeScrapper.getHelpStage().setMinWidth(375.0);

        LibMeScrapper.getHelpStage().setScene(scene);
    }

}