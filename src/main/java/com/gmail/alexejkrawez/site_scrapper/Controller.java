package com.gmail.alexejkrawez.site_scrapper;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

import static com.gmail.alexejkrawez.site_scrapper.ControllerHelper.*;

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

        savePathField.setText(SiteScrapper.getLastOpenedDirectory());

        if (SiteScrapper.getTheme().equals("css/dark-style.css")) {
            themeChangerIcon.setIconLiteral("fltfmz-weather-sunny-20");
        }

        initializeTableView(tableView, footerLabel);

        // TODO вынести (глобальный чекбокс)
//        TableColumn<TableRow, ?> tableRowTableColumn = tableView.getColumns().get(0);
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
        String link = addLinkField.getText();
        if (checkUrl(link, footerLabel)) {
            tableOfContents = Parser.getTableOfContents(link);
//            tableOfContents = Parser.getTableOfContents("https://ranobelib.me/ascendance-of-a-bookworm-novel/v1/c2?bid=12002"); //TODO: затычка
            showChapters(tableOfContents, tableView, footerLabel);

            getTableOfContentsButton.setDisable(false);
            saveToLocalButton.setDisable(false); // TODO вынести отдельно
            globalCheckbox.setDisable(false);
            reverseTableShowButton.setDisable(false);
        }
    }

    @FXML
    public void setLocalPath() {
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        File lastOpenedDirectory = new File(SiteScrapper.getLastOpenedDirectory());
        directoryChooser.setInitialDirectory(lastOpenedDirectory);
        directoryChooser.setTitle("Выберите путь для сохранения");

        Stage stage = (Stage) setLocalPathButton.getScene().getWindow();
        File file = directoryChooser.showDialog(stage);

        if (file == null) {
            return;
        }

        SiteScrapper.setLastOpenedDirectory(file.getAbsolutePath());
        savePathField.setText(file.getAbsolutePath());
        footerLabel.setText("Путь сохранения: " + file.getAbsolutePath());
    }

    @FXML
    protected void saveToLocal() {
        // TODO блокировать кнопки
        // TODO получение и проверка пути сохранения
        String pathToSave = savePathField.getText();

        if (checkPath(pathToSave, footerLabel)) {
            // TODO извлечение выделения / галочек +/-
            List<Chapter> checkedChapters = getCheckedChapters(tableOfContents);
            // TODO получение данных +
            Parser.getData(checkedChapters);
            // TODO и сохранение
            Parser.saveDocument(pathToSave, footerLabel);
            // TODO + сделать анимацию что такая-то глава загрузилась? Новый поток?
        }
        // TODO разблокировать кнопки
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

        showHelpWindow();

//        Dialog<String> dialog = new Dialog<>();
//        dialog.initOwner(addLinkField.getScene().getWindow());
//        dialog.setTitle("Справка");
//        dialog.setHeaderText("Look, a Custom Login Dialog");
//
//        ButtonType okButton = new ButtonType("Понятно", ButtonBar.ButtonData.OK_DONE);
//
//        dialog.setDialogPane(new DialogPane() {
//            @Override protected Node createButtonBar() {
//                var buttonBar = (ButtonBar)super.createButtonBar();
//                buttonBar.setButtonOrder("L_O_R");
//                return buttonBar;
//            }
//        });
//
//        dialog.getDialogPane().getButtonTypes().addAll(okButton);
//        DialogPane dialogPane = dialog.getDialogPane();
//        dialogPane.setContentText("ButtonCentered Button");
//
//        Label label = new Label(text);
//
//        VBox layout = new VBox(label);
//
////        dialogPane.setContent();
//
//
////        dialog.getDialogPane().getScene().getStylesheets().add(getClass().getResource(SiteScrapper.getTheme()).toExternalForm());
////
//        dialog.showAndWait();
    }





    @FXML
    protected void changeTheme() {
        Scene scene = themeChangerButton.getScene();

        if (SiteScrapper.getTheme().equals("css/light-style.css")) {
            scene.getStylesheets().set(0, getClass().getResource("css/dark-style.css").toExternalForm());
            themeChangerIcon.setIconLiteral("fltfmz-weather-sunny-20");
            SiteScrapper.setTheme("css/dark-style.css");
            footerLabel.setText("Включена тёмная тема");
        } else {
            scene.getStylesheets().set(0, getClass().getResource("css/light-style.css").toExternalForm());
            themeChangerIcon.setIconLiteral("fltfmz-weather-moon-20");
            SiteScrapper.setTheme("css/light-style.css");
            footerLabel.setText("Включена светлая тема");
        }
    }




//    private void setButtonsActive() {
//        moveSelectedUp.setDisable(false);
//        moveSelectedDown.setDisable(false);
//        removeSelected.setDisable(false);
//        removeAll.setDisable(false);
//        if (isListViewReady()) {
//            mergeFiles.setDisable(false);
//        }
//    }
//
//    private void setButtonsInactive() {
//        moveSelectedUp.setDisable(true);
//        moveSelectedDown.setDisable(true);
//        removeSelected.setDisable(true);
//        removeAll.setDisable(true);
//        mergeFiles.setDisable(true);
//    }


}