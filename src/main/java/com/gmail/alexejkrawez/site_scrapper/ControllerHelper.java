package com.gmail.alexejkrawez.site_scrapper;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import org.slf4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class ControllerHelper {

    private static final Logger log = getLogger(ControllerHelper.class);

    protected static void initializeTableView(TableView<TableRow> tableView, Label footerLabel) {
        tableView.getStyleClass().add("noheader"); // TODO переделать
        tableView.setEditable(true);

        TableColumn<TableRow, Boolean> checkboxColumn = new TableColumn<>("First Name"); //TODO переименовать
        checkboxColumn.setCellValueFactory(new PropertyValueFactory<>("checkbox"));
        checkboxColumn.setCellFactory( tc -> new CheckBoxTableCell<>());

        TableColumn<TableRow, String> nameColumn = new TableColumn<>("Two Name");  //TODO переименовать
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<TableRow, Hyperlink> urlColumn = new TableColumn<>("First Name");  //TODO переименовать
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));

        tableView.getColumns().add(checkboxColumn);
        tableView.getColumns().add(nameColumn);
        tableView.getColumns().add(urlColumn);

        // selecting actions
        TableView.TableViewSelectionModel<TableRow> selectionModel = tableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.MULTIPLE);
        selectionModel.selectedItemProperty().addListener(new ChangeListener<TableRow>(){
            @Override
            public void changed(ObservableValue<? extends TableRow> observable, TableRow oldValue, TableRow newValue) {
                if(newValue != null) {
                    footerLabel.setText("Selected: " + newValue.getName());
                    ObservableList<Integer> selectedIndices = selectionModel.getSelectedIndices();
                    log.info(selectedIndices.toString());
                }
            }
        });
    }

    protected static void showChapters(List<Chapter> tableOfContents,
                                       TableView<TableRow> tableView,
                                       Label footerLabel) { // TODO придумать что писать в футер

        if (tableOfContents != null && !tableOfContents.isEmpty()) {

            for (Chapter chapter : tableOfContents) {
                Hyperlink url = new Hyperlink(chapter.getChapterLink());
                url.setText("New link text");
                url.setOnAction(e -> {
                    Desktop desktop = Desktop.getDesktop();
                    try {
                        desktop.browse(java.net.URI.create(chapter.getChapterLink()));
                    } catch (IOException ex) {
                        log.error("Error");
                    }
                });
                TableRow tableRow = new TableRow(false, chapter.getChapterName(), url);

                tableView.getItems().add(tableRow);
            }



//            int size = 0;
//            for (Chapter file : tableOfContents) {
//                listView.getItems().add(file); // TODO переделать
//                size++;
//            }
//
//            if (size > 0) {
//                if (size == 1) {
//                    footerLabel.setText("Added 1 file"); // TODO переделать
//                } else {
//                    footerLabel.setText("Added " + size + " files");
//                }
//            }

//            if (listView.getItems().size() == 1) {
//                removeSelected.setDisable(false);
//                removeAll.setDisable(false);
//            } else if (listView.getItems().size() > 1) {
//                setButtonsActive();
//            }

        }
    }







}
