package com.gmail.alexejkrawez.site_scrapper;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import org.slf4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class ControllerHelper {

    private static final Logger log = getLogger(ControllerHelper.class);
    private static boolean isChecked = false;
    private static ObservableList<Integer> selectedIndices;

    protected static void initializeTableView(TableView<TableRow> tableView, Label footerLabel) {
        tableView.setId("table-view");
        tableView.getStyleClass().add("noheader");
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<TableRow, Boolean> checkboxColumn = new TableColumn<>("First Name"); //TODO переименовать
        checkboxColumn.setCellValueFactory(new PropertyValueFactory<>("checkbox"));
        checkboxColumn.setCellFactory( tc -> new CheckBoxTableCell<>());
        checkboxColumn.setId("checkbox-column");
        checkboxColumn.setMinWidth(32.0);
        checkboxColumn.setMaxWidth(32.0);

        checkboxColumn.setCellFactory(new Callback<TableColumn<TableRow, Boolean>, TableCell<TableRow, Boolean>>() {
            @Override
            public TableCell<TableRow, Boolean> call( TableColumn<TableRow, Boolean> param ) {
                return new CheckBoxTableCell<TableRow, Boolean>() {
                    @Override
                    public void updateItem(Boolean checkbox, boolean empty) {
                        if (!empty) {
                            javafx.scene.control.TableRow<TableRow> row = getTableRow();

                            if (row != null) {
                                int rowNumber = row.getIndex();
                                TableView.TableViewSelectionModel<TableRow> sm = getTableView().getSelectionModel();
                                ObservableList<TableRow> items = tableView.getItems();
                                ObservableList<Integer> selectedNowIndices = sm.getSelectedIndices();
                                if (checkbox) {
                                    sm.select(rowNumber);
                                    for (int index : selectedNowIndices) {
                                        items.get(index).checkboxProperty().set(row.getItem().isCheckbox());
                                    }
                                } else {
                                    sm.clearSelection(rowNumber);
                                    for (int index : selectedNowIndices) {
                                        items.get(index).checkboxProperty().set(row.getItem().isCheckbox());
                                    }
                                }
                            }
                        }

                        super.updateItem(checkbox, empty);
                    }
                };
            }
        } );

//        checkboxColumn.setCellFactory(CheckBoxTableCell.forTableColumn(new Callback<Integer, ObservableValue<Boolean>>() {
//            @Override
//            public ObservableValue<Boolean> call(Integer param) {
//                TableView.TableViewSelectionModel<TableRow> selectionModel = tableView.getSelectionModel();
//                ObservableList<Integer> selectedNowIndices = selectionModel.getSelectedIndices();
//                ObservableList<TableRow> items = tableView.getItems();
//                if (!selectedNowIndices.equals(selectedIndices)) {
//                    isChecked = items.get(param).isCheckbox();
//                    selectedIndices = selectedNowIndices;
//                }
//
//                if (isChecked) {
//                    for (int index : selectedIndices) {
//                        items.get(index).checkboxProperty().set(true);
//                    }
//                } else {
//                    for (int index : selectedIndices) {
//                        items.get(index).checkboxProperty().set(false);
//                    }
//                }
//
//                return items.get(param).checkboxProperty();
//            }
//        }));

        TableColumn<TableRow, String> nameColumn = new TableColumn<>("Two Name");  //TODO переименовать
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setId("name-column");

        TableColumn<TableRow, Hyperlink> urlColumn = new TableColumn<>("First Name");  //TODO переименовать
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        urlColumn.setId("url-column");
        urlColumn.setMinWidth(48.0);
        urlColumn.setMaxWidth(48.0);

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
                    selectedIndices = selectionModel.getSelectedIndices();
                    log.info(selectedIndices.toString());


//                    ObservableList<TableRow> items = tableView.getItems();
//                    for (int index : selectedIndices) {
//                        items.get(index).checkboxProperty().set(newValue);
//                    }
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
                url.setText("url \u2B0F"); // TODO придумать что писать
                url.setOnAction(e -> {
                    url.setVisited(false);
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
