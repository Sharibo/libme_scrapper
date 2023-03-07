package com.gmail.alexejkrawez.site_scrapper;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.kordamp.ikonli.javafx.FontIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.gmail.alexejkrawez.site_scrapper.ControllerHelper.*;

public class Controller {

    @FXML
    private TextField addLink;
    @FXML
    private Button getTableOfContentsButton;
    //    @FXML
//    private Button mergeFiles;
    @FXML
    private ListView<TableRow> listView;
    @FXML
    private TableView<TableRow> tableView;
//    @FXML
//    private CheckBoxListCell<CheckBox> listCheckBox;
//    @FXML
//    private TextFieldListCell<TextField> listText;
//    @FXML
//    private TextFieldListCell<TextField> listUrl;
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

        if (SiteScrapper.getTheme().equals("css/dark-style.css")) {
            themeChangerIcon.setIconLiteral("fltfmz-weather-sunny-20");
        }

        initializeTableView(tableView, footerLabel);

//        addListViewFooterLabelInfoEventListener();
//        addListDragAndDropEventListener();
    }

    // TODO интерфейс
    // TODO поле для ввода ссылки
    // TODO поле ввода пути для сохранения + кнопка выбрать папку сохранения (запоминается в файлик настроек)
    // TODO поле с таблицей / список глав + галочками + ссылкой на главу + на эскейт снятие галок со всех глав?
    // TODO отметка всех глав / снятие выделения
    // TODO отметка части глав
    // TODO разбивать ли на тома с сохранением в отдельные файлы + возможно маска томов
    // TODO добавлять ли автора + можно перезаписать как зовут автора на кириллицу или что хочешь
    // TODO поле футера
    // TODO хелп с указанием автора и версии программы
    // TODO смена темы день/ночь

    @FXML
    protected void getTableOfContents() {
        String link = addLink.getText();
        tableOfContents = Parser.getTableOfContents(link);
        showChapters(tableOfContents, tableView, footerLabel);
    }

//        Parser.getData("https://ranobelib.me/ascendance-of-a-bookworm-novel/v1/c2?bid=12002"); //TODO: затычка
//        Parser.getData("https://ranobelib.me/quan-zhi-gao-shou-novel/v1/c0?bid=10511&ui=1709435"); //TODO: затычка


    @FXML //TODO пока нет такой кнопки
    protected void buttonDownloadChapters() {
        // TODO извлечение выделения / галочек

        // TODO получение данных и сразу сохранение или после загрузки?
        Parser.getData(1, 3, tableOfContents);
        // TODO + сделать анимацию что такая-то глава загрузилась? Новый поток?
    }

    @FXML
    protected void moveSelectedUp() {
//        buttonAddLink();
    }


//    @FXML
//    protected void buttonMergeFiles() {
//        final FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Select directory for save");
//        fileChooser.setInitialDirectory(new File(SiteScrapper.getLastOpenedDirectory()));
//        fileChooser.getExtensionFilters().addAll(
//                new FileChooser.ExtensionFilter("PDF", "*.pdf")
//        );
//
//        Stage stage = (Stage) addLink.getScene().getWindow();
//        File savedFile = fileChooser.showSaveDialog(stage);
//
//        try {
//            SiteScrapper.setLastOpenedDirectory(savedFile.getParent());
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//
//        boolean isMerged = mergePDFs(savedFile.getAbsolutePath());
//
//        if (isMerged) {
//            footerLabel.setText("Files merged as " + savedFile.getName());
//
//            Desktop desktop = Desktop.getDesktop();
//            try {
//                desktop.open(savedFile);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            footerLabel.setText("Files not bound: attempt to save to one of the bound files");
//        }
//
//    }

//    private boolean mergePDFs(String pathForSave) {
//
//        PDFMergerUtility mergerUtility = new PDFMergerUtility();
//
//        PDDocumentInformation documentInformation = new PDDocumentInformation();
//        documentInformation.setTitle("PDFMerger document");
//        documentInformation.setSubject("Merging PDF documents with Apache PDF Box by PDFMerger app");
//
//        try {
//            for (String path : listView.getItems()) {
//                if (pathForSave.equalsIgnoreCase(path)) {
//                    return false;
//                } else {
//                    mergerUtility.addSource(path);
//                    mergerUtility.setDestinationFileName(pathForSave);
//                    mergerUtility.setDestinationDocumentInformation(documentInformation);
//                    mergerUtility.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
//                }
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return true;
//    }


    @FXML
    protected void changeTheme() {
        Scene scene = themeChangerButton.getScene();

        if (SiteScrapper.getTheme().equals("css/light-style.css")) {
            scene.getStylesheets().set(0, getClass().getResource("css/dark-style.css").toExternalForm());
            themeChangerIcon.setIconLiteral("fltfmz-weather-sunny-20");
            SiteScrapper.setTheme("css/dark-style.css");
            footerLabel.setText("Dark theme enabled");
        } else {
            scene.getStylesheets().set(0, getClass().getResource("css/light-style.css").toExternalForm());
            themeChangerIcon.setIconLiteral("fltfmz-weather-moon-20");
            SiteScrapper.setTheme("css/light-style.css");
            footerLabel.setText("Light theme enabled");
        }
    }


//    private boolean isListViewReady() {
//        if (listView.getItems().size() <= 1) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//
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


//    private void addListViewFooterLabelInfoEventListener() {
//        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//
//        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
//            @Override
//            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//
//                footerLabel.setText("Selected: " + listView.getSelectionModel().getSelectedIndices().size());
//            }
//        });
//    }

//    private void addListDragAndDropEventListener() {
//        listView.setOnDragOver(event -> {
//            event.acceptTransferModes(TransferMode.COPY);
//            event.consume();
//        });
//
//        listView.setOnDragDropped((DragEvent event) -> {
//            Dragboard dragboard = event.getDragboard();
//
//            if (dragboard.hasFiles()) {
//                showFiles(dragboard.getFiles());
//
//                event.setDropCompleted(true);
//            } else {
//                event.setDropCompleted(false);
//            }
//
//            event.consume();
//        });
//    }

}