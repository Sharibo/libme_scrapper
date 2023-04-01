package com.gmail.alexejkrawez.libme_scrapper;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.gmail.alexejkrawez.libme_scrapper.ControllerHelper.checkPath;
import static com.gmail.alexejkrawez.libme_scrapper.ControllerHelper.checkUrl;
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
    private CheckBox globalCheckbox;
    @FXML
    private Button reverseTableShowButton;
    @FXML
    private TableView<TableRow> tableView;
    @FXML
    private Label footerLabel;
    @FXML
    private Button themeChangerButton;
    @FXML
    private FontIcon themeChangerIcon;

    private static final Logger log = LoggerFactory.getLogger(Controller.class);
    private List<Chapter> tableOfContents;

    @FXML
    public void initialize() {

        savePathField.setText(LibMeScrapper.getLastOpenedDirectory());

        if (LibMeScrapper.getTheme().equals("css/dark-style.css")) {
            themeChangerIcon.setIconLiteral("fltfmz-weather-sunny-20");
        }

        initializeTableView(tableView, footerLabel);

        globalCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            for (TableRow item : tableView.getItems()) {
                item.checkboxProperty().set(newValue);
            }
        });

    }

    // TODO интерфейс +/-
    // TODO поле для ввода ссылки +
    // TODO поле ввода пути для сохранения + кнопка выбрать папку сохранения (запоминается в файлик настроек) +
    // TODO поле с таблицей / список глав + галочками + ссылкой на главу +
    // TODO отметка всех глав / снятие выделения +
    // TODO отметка части глав +
    // TODO разбивать ли на тома с сохранением в отдельные файлы + возможно маска томов
    // TODO добавлять ли автора + можно перезаписать как зовут автора на кириллицу или что хочешь
    // TODO поле футера +
    // TODO хелп с указанием автора и версии программы +/-
    // TODO смена темы день/ночь +

    @FXML
    protected void getTableOfContents() {
        getTableOfContentsButton.setDisable(true);

        String url = addLinkField.getText().strip();

        if (checkUrl(url, footerLabel)) {
            footerLabel.setText("Ожидайте, загружается оглавление...");
            setDisable(saveToLocalButton, globalCheckbox, reverseTableShowButton);

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
        setDisable(getTableOfContentsButton, saveToLocalButton, setLocalPathButton,
                   globalCheckbox, reverseTableShowButton, tableView);

        String pathToSave = savePathField.getText().strip();

        // получение и проверка пути сохранения
        if (checkPath(pathToSave, footerLabel)) {

            CompletableFuture.supplyAsync(() -> getCheckedChapters(tableOfContents))
                    .whenCompleteAsync((checkedChapters, throwable) -> {
                        if (throwable == null) {
                            Parser.getData(checkedChapters);
                            setFooterLabelAsync( Parser.saveDocument(pathToSave) );
                        } else if (throwable.getCause() instanceof IllegalArgumentException) {
                            log.error("saveToLocal() slave thread failed: " + throwable.getLocalizedMessage());
                            setFooterLabelAsync("Не выделено ни одной главы для сохранения!");
                        } else {
                            log.error("saveToLocal() slave thread failed: " + throwable.getLocalizedMessage());
                            setFooterLabelAsync("Возникла ошибка при обработке глав!");
                        }
                        setEnable(getTableOfContentsButton, saveToLocalButton, setLocalPathButton,
                                globalCheckbox, reverseTableShowButton, tableView);
                    }); // non-blocking
        } else {
            setEnable(getTableOfContentsButton, saveToLocalButton, setLocalPathButton,
                      globalCheckbox, reverseTableShowButton, tableView);
        }

    }


    @FXML
    protected void reverseTableShow() {
        reverseTable(tableView, footerLabel);
    }



//    @FXML
//    protected void saveToLocal() {
//
//    }

//    @FXML
//    protected void checkAll() {
//        TableColumn<TableRow, ?> tableRowTableColumn = tableView.getColumns().get(0);
//        globalCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
//            for (TableRow item : tableView.getItems()) {
//                item.checkboxProperty().set(newValue);
//            }
//        });
//    }

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