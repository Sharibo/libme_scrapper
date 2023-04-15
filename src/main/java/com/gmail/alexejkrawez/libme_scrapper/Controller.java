package com.gmail.alexejkrawez.libme_scrapper;

import com.google.common.collect.Lists;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static com.gmail.alexejkrawez.libme_scrapper.ControllerHelper.checkPath;
import static com.gmail.alexejkrawez.libme_scrapper.ControllerHelper.checkUrl;
import static com.gmail.alexejkrawez.libme_scrapper.ControllerHelper.checkVolumes;
import static com.gmail.alexejkrawez.libme_scrapper.ControllerHelper.getCheckedChapters;
import static com.gmail.alexejkrawez.libme_scrapper.ControllerHelper.initializeTableView;
import static com.gmail.alexejkrawez.libme_scrapper.ControllerHelper.reverseTable;
import static com.gmail.alexejkrawez.libme_scrapper.ControllerHelper.showChapters;

public class Controller {

    @FXML
    private TextField addLinkField;
    @FXML
    private Button getTableOfContentsButton;
    @FXML
    private TextField savePathField;
    @FXML
    private Button setLocalPathButton;
    @FXML
    private Button saveToLocalButton;
    @FXML
    private HBox hBox3;
    @FXML
    private CheckBox globalCheckbox;
    @FXML
    private Button reverseTableShowButton;
    @FXML
    private ToggleButton isDividedByVolumesButton;
    @FXML
    private ToggleButton isDividedByNChaptersButton;
    @FXML
    private TextField nChaptersField;
    @FXML
    private TableView<TableRow> tableView;
    @FXML
    private Label footerLabel;
    @FXML
    private Button themeChangerButton;
    @FXML
    private FontIcon themeChangerIcon;

    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    private boolean isDividedByVolumes = false;
    private boolean isDividedByNChapters = false;
    private int nChapters = 50;
    private List<Chapter> tableOfContents;


    public boolean getIsDividedByVolumes() {
        return isDividedByVolumes;
    }
    public void setIsDividedByVolumesButton(boolean isDividedByVolumesButton) {
        this.isDividedByVolumesButton.setSelected(isDividedByVolumesButton);
    }

    public boolean getIsDividedByNChapters() {
        return isDividedByNChapters;
    }
    public void setIsDividedByNChaptersButton(boolean isDividedByNChaptersButton) {
        this.isDividedByNChaptersButton.setSelected(isDividedByNChaptersButton);
    }

    public TextField getNChaptersField() {
        return nChaptersField;
    }
    public void setNChapters(int nChapters) {
        this.nChapters = nChapters;
    }


    @FXML
    public void initialize() {

        savePathField.setText(LibMeScrapper.getLastOpenedDirectory());

        if (LibMeScrapper.getTheme().equals("css/dark-style.css")) {
            themeChangerIcon.setIconLiteral("fltfmz-weather-sunny-20");
        }

        initializeTableView(tableView);

        globalCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            for (TableRow item : tableView.getItems()) {
                item.checkboxProperty().set(newValue);
            }
        });

        nChaptersField = new TextField(); // initialization in LibMeScrapper class
        hBox3.setMargin(nChaptersField, new Insets(0.0, 0.0, 2.0, 2.0));

    }

    // TODO в файл сохранять нажата или отжата кнопки по томам и по частям и
    // TODO дизейблить эти кнопки при сохранении +
    // TODO снятие выделения +
    // TODO маска названий файлов
    // TODO добавлять ли автора + можно перезаписать как зовут автора на кириллицу или что хочешь
    // TODO хелп с указанием автора и версии программы +/-

    @FXML
    protected void getTableOfContents() {
        getTableOfContentsButton.setDisable(true);

        String url = addLinkField.getText().strip();

        if (checkUrl(url, footerLabel)) {
            footerLabel.setText("Ожидайте, загружается оглавление...");
            setDisable(saveToLocalButton, globalCheckbox, reverseTableShowButton);
            tableView.requestFocus();

            if (tableOfContents != null && !tableOfContents.isEmpty()) {
                tableOfContents.clear();
            }

            CompletableFuture.supplyAsync(() -> Parser.getTableOfContents(url))
                    .whenComplete((tableOfContents, throwable) -> {
                        if (throwable == null) {
                            int chapters = showChapters(tableOfContents, tableView);
                            setFooterLabelAsync("Оглавление загружено. Всего глав: " + chapters);
                            setTableOfContentsAsync(tableOfContents);
                            setEnable(saveToLocalButton, globalCheckbox, reverseTableShowButton);
                        } else {
                            log.error("getTableOfContents() - slave thread failed: " + throwable.getLocalizedMessage());
                            setFooterLabelAsync("Возникла ошибка при загрузке!");
                        }

                        getTableOfContentsButton.setDisable(false);
                    }); // non-blocking
        } else {
            getTableOfContentsButton.setDisable(false);
        }

    }

    @FXML
    public void setLocalPath() {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        File lastOpenedDirectory = new File(LibMeScrapper.getLastOpenedDirectory());
        directoryChooser.setInitialDirectory(lastOpenedDirectory);
        directoryChooser.setTitle("Выберите путь для сохранения");

        Stage stage = (Stage) setLocalPathButton.getScene().getWindow();
        File file = directoryChooser.showDialog(stage);

        if (file == null) {
            return;
        }

        LibMeScrapper.setLastOpenedDirectory(file.getAbsolutePath());
        savePathField.setText(file.getAbsolutePath());
        footerLabel.setText("Путь сохранения: " + file.getAbsolutePath());
    }

    @FXML
    protected void saveToLocal() {
        setDisable(getTableOfContentsButton, saveToLocalButton, setLocalPathButton, globalCheckbox,
                   reverseTableShowButton, isDividedByVolumesButton, isDividedByNChaptersButton, tableView);

        String pathToSave = savePathField.getText().strip();

        // get and check path to saving
        if (checkPath(pathToSave, footerLabel)) {
            footerLabel.setText("Начато сохранение! Пожалуйста, подождите...");

            CompletableFuture.supplyAsync(() -> getCheckedChapters(tableOfContents))
                    .whenCompleteAsync((checkedChapters, throwable) -> {
                        if (throwable == null) {

                            if (isDividedByVolumes & isDividedByNChapters) {
                                String n = nChaptersField.getText();

                                if (!n.isBlank()) {
                                    nChapters = Integer.parseInt(n);
                                } else {
                                    setFooterLabelAsync("Не указано число глав для разделения на части!");
                                    setEnable(getTableOfContentsButton, saveToLocalButton, setLocalPathButton, globalCheckbox,
                                              reverseTableShowButton, isDividedByVolumesButton, isDividedByNChaptersButton,  tableView);
                                    return;
                                }

                                Map<String, ArrayList<Chapter>> map = checkVolumes(checkedChapters);

                                if (map.get("неизвестный").isEmpty()) {
                                    map.remove("неизвестный");
                                }

                                Set<String> volumeNumbers = map.keySet();

                                if (volumeNumbers.size() == 1 & volumeNumbers.contains("неизвестный")) {
                                    List<List<Chapter>> parts = Lists.partition(checkedChapters, nChapters);

                                    for (int i = 0, size = parts.size(); i < size; i++) {
                                        Parser.getData(parts.get(i), i + 1);
                                        setFooterLabelAsync(Parser.saveDocument(pathToSave, i + 1));
                                    }
                                } else {
                                    for (String volumeNumber : volumeNumbers) {
                                        List<List<Chapter>> parts = Lists.partition(map.get(volumeNumber), nChapters);

                                        if (parts.size() == 1) {
                                            Parser.getData(map.get(volumeNumber), volumeNumber);
                                            setFooterLabelAsync(Parser.saveDocument(pathToSave, volumeNumber));
                                        } else {
                                            for (int i = 0, size = parts.size(); i < size; i++) {
                                                Parser.getData(parts.get(i), volumeNumber, i + 1);
                                                setFooterLabelAsync(Parser.saveDocument(pathToSave, volumeNumber, i + 1));
                                            }
                                        }
                                    }
                                }

                            } else if (isDividedByVolumes) {
                                Map<String, ArrayList<Chapter>> map = checkVolumes(checkedChapters);

                                if (map.get("неизвестный").isEmpty()) {
                                    map.remove("неизвестный");
                                }

                                Set<String> volumeNumbers = map.keySet();


                                if (volumeNumbers.size() == 1 & volumeNumbers.contains("неизвестный")) {
                                    Parser.getData(checkedChapters);
                                    setFooterLabelAsync(Parser.saveDocument(pathToSave));
                                } else {
                                    for (String volumeNumber : volumeNumbers) {
                                        Parser.getData(map.get(volumeNumber), volumeNumber);
                                        setFooterLabelAsync(Parser.saveDocument(pathToSave, volumeNumber));
                                    }
                                }

                            } else if (isDividedByNChapters) {
                                String n = nChaptersField.getText();

                                if (!n.isBlank()) {
                                    nChapters = Integer.parseInt(n);
                                } else {
                                    setFooterLabelAsync("Не указано число глав для разделения на части!");
                                    setEnable(getTableOfContentsButton, saveToLocalButton, setLocalPathButton, globalCheckbox,
                                              reverseTableShowButton, isDividedByVolumesButton, isDividedByNChaptersButton, tableView);
                                    return;
                                }

                                List<List<Chapter>> parts = Lists.partition(checkedChapters, nChapters);

                                for (int i = 0, size = parts.size(); i < size; i++) {
                                    Parser.getData(parts.get(i), i + 1);
                                    setFooterLabelAsync(Parser.saveDocument(pathToSave, i + 1));
                                }

                            } else {

                                Parser.getData(checkedChapters);
                                setFooterLabelAsync(Parser.saveDocument(pathToSave));

                            }

                        } else if (throwable.getCause() instanceof IllegalArgumentException) {
                            log.error("saveToLocal() slave thread failed: " + throwable.getLocalizedMessage());
                            setFooterLabelAsync("Не выделено ни одной главы для сохранения!");
                        } else {
                            log.error("saveToLocal() slave thread failed: " + throwable.getLocalizedMessage());
                            setFooterLabelAsync("Возникла ошибка при обработке глав!");
                        }
                        setEnable(getTableOfContentsButton, saveToLocalButton, setLocalPathButton, globalCheckbox,
                                  reverseTableShowButton, isDividedByVolumesButton, isDividedByNChaptersButton, tableView);
                    }); // non-blocking


        } else {
            setEnable(getTableOfContentsButton, saveToLocalButton, setLocalPathButton, globalCheckbox,
                      reverseTableShowButton, isDividedByVolumesButton, isDividedByNChaptersButton,  tableView);
        }

    }



    @FXML
    protected void reverseTableShow() {
        reverseTable(tableView, footerLabel);
    }

    @FXML
    protected void isDividedByVolumesChapters() {
        if (isDividedByVolumesButton.isSelected()) {
            isDividedByVolumes = true;
        } else {
            isDividedByVolumes = false;
        }
    }

    @FXML
    protected void isDividedByNChapters() {
        if (isDividedByNChaptersButton.isSelected()) {
            isDividedByNChapters = true;
            hBox3.getChildren().add(nChaptersField);
        } else {
            isDividedByNChapters = false;
            hBox3.getChildren().remove(nChaptersField);
        }
    }



    @FXML
    protected void getAbout() {
        LibMeScrapper.getHelpStage().show();
    }

    @FXML
    protected void changeTheme() {
        Scene scene = themeChangerButton.getScene();

        if (LibMeScrapper.getTheme().equals("css/light-style.css")) {
            scene.getStylesheets().set(0, getClass().getResource("css/dark-style.css").toExternalForm());
            LibMeScrapper.getHelpStage().getScene()
                    .getStylesheets().set(0, getClass().getResource("css/help-dark-style.css").toExternalForm());
            themeChangerIcon.setIconLiteral("fltfmz-weather-sunny-20");
            LibMeScrapper.setTheme("css/dark-style.css");
            footerLabel.setText("Включена тёмная тема");
        } else {
            scene.getStylesheets().set(0, getClass().getResource("css/light-style.css").toExternalForm());
            LibMeScrapper.getHelpStage().getScene()
                    .getStylesheets().set(0, getClass().getResource("css/help-light-style.css").toExternalForm());
            themeChangerIcon.setIconLiteral("fltfmz-weather-moon-20");
            LibMeScrapper.setTheme("css/light-style.css");
            footerLabel.setText("Включена светлая тема");
        }
    }



    private void setFooterLabelAsync(String source) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                footerLabel.setText(source);
            }
        });
    }

    private void setTableOfContentsAsync(List<Chapter> source) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                tableOfContents = source;
            }
        });
    }

    private void setEnable(Control... items) {
        for (Control item : items) {
            item.setDisable(false);
        }
    }

    private void setDisable(Control... items) {
        for (Control item : items) {
            item.setDisable(true);
        }
    }


}