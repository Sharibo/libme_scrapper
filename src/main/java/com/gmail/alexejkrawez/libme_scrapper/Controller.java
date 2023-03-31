package com.gmail.alexejkrawez.libme_scrapper;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.CheckBox;
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

import static com.gmail.alexejkrawez.libme_scrapper.ControllerHelper.*;

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

    private static final Logger log = LoggerFactory.getLogger(Controller.class); // TODO если не нужен будет убрать
    private List<Chapter> tableOfContents;

    @FXML
    public void initialize() {

        savePathField.setText(LibMeScrapper.getLastOpenedDirectory());

        if (LibMeScrapper.getTheme().equals("css/dark-style.css")) {
            themeChangerIcon.setIconLiteral("fltfmz-weather-sunny-20");
        }

        initializeTableView(tableView, footerLabel);

        // TODO вынести (глобальный чекбокс)
        globalCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            for (TableRow item : tableView.getItems()) {
                item.checkboxProperty().set(newValue);
            }
        });

    }

    // TODO интерфейс
    // TODO поле для ввода ссылки +
    // TODO поле ввода пути для сохранения + кнопка выбрать папку сохранения (запоминается в файлик настроек) +
    // TODO поле с таблицей / список глав + галочками + ссылкой на главу + на эскейт снятие галок со всех глав? +/-
    // TODO отметка всех глав / снятие выделения +
    // TODO отметка части глав +
    // TODO разбивать ли на тома с сохранением в отдельные файлы + возможно маска томов
    // TODO добавлять ли автора + можно перезаписать как зовут автора на кириллицу или что хочешь
    // TODO поле футера +
    // TODO хелп с указанием автора и версии программы
    // TODO смена темы день/ночь +

    @FXML
    protected void getTableOfContents() {
        getTableOfContentsButton.setDisable(true);

        String url = addLinkField.getText();

        if (checkUrl(url, footerLabel)) {
            tableOfContents = Parser.getTableOfContents(url);
//            tableOfContents = Parser.getTableOfContents("https://ranobelib.me/ascendance-of-a-bookworm-novel/v1/c2?bid=12002"); //TODO: затычка
            showChapters(tableOfContents, tableView, footerLabel);

            setEnable(saveToLocalButton, globalCheckbox, reverseTableShowButton);
        }

        getTableOfContentsButton.setDisable(false);
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
        setDisable(getTableOfContentsButton, saveToLocalButton, setLocalPathButton);
        String pathToSave = savePathField.getText();

        // получение и проверка пути сохранения
        if (checkPath(pathToSave, footerLabel)) {
            // извлечение выделения / галочек
            List<Chapter> checkedChapters = getCheckedChapters(tableOfContents);
            // получение данных
            Parser.getData(checkedChapters);
            // и сохранение
            Parser.saveDocument(pathToSave, footerLabel);
            // TODO + сделать анимацию что такая-то глава загрузилась? Новый поток?
        }
        // TODO возможно остальные кнопки тоже блокировать
        setEnable(getTableOfContentsButton, saveToLocalButton, setLocalPathButton);
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




    private void setEnable(ButtonBase... items) {
        for (ButtonBase item : items) {
            item.setDisable(false);
        }
    }

    private void setDisable(ButtonBase... items) {
        for (ButtonBase item : items) {
            item.setDisable(true);
        }
    }


}