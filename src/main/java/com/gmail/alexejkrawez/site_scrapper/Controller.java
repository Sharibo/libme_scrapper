package com.gmail.alexejkrawez.site_scrapper;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Controller {

    @FXML
    private TextField addLink;
    @FXML
    private Button moveSelectedUp;
//    @FXML
//    private Button mergeFiles;
//    @FXML
//    private ListView<String> listView;
    @FXML
    private Label footerLabel;
    @FXML
    private Button themeChanger;
    @FXML
    private FontIcon themeChangerIcon;


    @FXML
    public void initialize() {

        if (SiteScrapper.getTheme().equals("css/dark-style.css")) {
            themeChangerIcon.setIconLiteral("fltfmz-weather-sunny-20");
        }

//        addListViewFooterLabelInfoEventListener();
//        addListDragAndDropEventListener();
    }

    @FXML
    protected void buttonAddLink() {
        String link = addLink.getText();
//        Parser.getData(link);
        Parser.getData("https://ranobelib.me/ascendance-of-a-bookworm-novel/v1/c2?bid=12002"); //TODO: затычка
    }

    @FXML
    protected void moveSelectedUp() {
        buttonAddLink();
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
    protected void buttonThemeChanger() {
        Scene scene = themeChanger.getScene();

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