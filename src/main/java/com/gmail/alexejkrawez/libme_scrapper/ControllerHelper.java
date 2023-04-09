package com.gmail.alexejkrawez.libme_scrapper;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import org.slf4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.slf4j.LoggerFactory.getLogger;

public class ControllerHelper {

    private static final Logger log = getLogger(ControllerHelper.class);
    private static final ObservableList<TableRow> tableList = FXCollections.observableArrayList();
    private static final ObservableList<TableRow> tableListReversed = FXCollections.observableArrayList();
    private static boolean isReversed = false;
    private static ObservableList<Integer> selectedIndices;


    protected static void initializeTableView(TableView<TableRow> tableView) {
        tableView.setId("tableView");
        tableView.getStyleClass().add("noheader");
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<TableRow, Boolean> checkboxColumn = new TableColumn<>(); //TODO переименовать
        checkboxColumn.setCellValueFactory(new PropertyValueFactory<>("checkbox"));
        checkboxColumn.setCellFactory(new Callback<TableColumn<TableRow, Boolean>, TableCell<TableRow, Boolean>>() {
            @Override
            public TableCell<TableRow, Boolean> call( TableColumn<TableRow, Boolean> param ) {
                CheckBoxTableCell<TableRow, Boolean> cell = new CheckBoxTableCell<TableRow, Boolean>();
                cell.addEventFilter(MouseEvent.MOUSE_CLICKED, new CheckboxMouseClickEventHandler());
                return cell;
            }
        } );
        checkboxColumn.setId("checkboxColumn");
        checkboxColumn.setMinWidth(32.0);
        checkboxColumn.setMaxWidth(32.0);


        TableColumn<TableRow, String> nameColumn = new TableColumn<>("Содержание");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setId("nameColumn");

        TableColumn<TableRow, Hyperlink> urlColumn = new TableColumn<>();
        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
        urlColumn.setId("urlColumn");
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
                    selectedIndices = selectionModel.getSelectedIndices();
                }
            }
        });

    }

    protected static class CheckboxMouseClickEventHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent t) {
            TableCell c = (TableCell) t.getSource();
            int cellIndex = c.getIndex();
            Boolean valueOld = (Boolean) c.itemProperty().get();
            Boolean valueNew = tableList.get(cellIndex).isCheckbox();
            if (valueOld != valueNew) {
                ObservableList<TableRow> items = (ObservableList<TableRow>) c.getTableView().getItems();
                if (selectedIndices != null && !selectedIndices.isEmpty()) {
                    for (int index : selectedIndices) {
                        items.get(index).checkboxProperty().set(valueNew);
                    }
                }
                // TODO в футер "Отмечено глав: столько-то"
            }
        }
    }

    protected static int showChapters(List<Chapter> tableOfContents,
                                      TableView<TableRow> tableView) {

        isReversed = false;
        if (!tableList.isEmpty()) {
            tableList.clear();
        }

        for (Chapter chapter : tableOfContents) {
            Hyperlink url = new Hyperlink(chapter.getChapterLink());

            Tooltip tooltip = new Tooltip(chapter.getChapterLink());
            tooltip.setShowDelay(new Duration(700));
            url.setTooltip(tooltip);

            url.setText("url \u2B0F");
            url.setOnAction(e -> {
                url.setVisited(false);
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(java.net.URI.create(chapter.getChapterLink()));
                } catch (IOException ex) {
                    log.error("Error by opening chapter-url");
                }
            });

            TableRow tableRow = new TableRow(false, chapter.getChapterName(), url);
            tableList.add(tableRow);
        }

        tableListReversed.setAll(tableList);
        FXCollections.reverse(tableListReversed);

        tableView.getItems().setAll(tableList);

        return tableList.size();
    }

    protected static List<Chapter> getCheckedChapters(List<Chapter> tableOfContents) {
        ArrayList<Chapter> chapters = new ArrayList<>();

        for (int i = 0, size = tableList.size(); i < size; i++) {
            if (tableList.get(i).isCheckbox()) {
                chapters.add(tableOfContents.get(i));
            }
        }

        if (chapters.isEmpty()) {
            log.error("Zero checkboxes selected");
            throw new IllegalArgumentException("Zero checkboxes selected");
        }

        return chapters;
    }

    protected static LinkedHashMap<String, ArrayList<Chapter>> checkVolumes(List<Chapter> checkedChapters) {
        LinkedHashMap<String, ArrayList<Chapter>> chapters = new LinkedHashMap<>();
//        chapters.put("unknown", new ArrayList<Chapter>()); // TODO сделать проверку на ненахождение томов
        Pattern p = Pattern.compile("[Ттом\\u00A0\\s]{3,5}([0-9.,:;\\-]+)");
        Matcher m;
        String s = "";

        for (Chapter chapter : checkedChapters) {
            m = p.matcher(chapter.getChapterName());
            if (m.find()) {
                chapter.setChapterName(
                        chapter.getChapterName().replaceFirst("[Ттом\\u00A0\\s]{3,5}[0-9.,:;\\-\\u00A0\\s]+", "")
                );

                if ( s.contains(m.group(1)) ) {
                    chapters.get(s).add(chapter);
                } else {
                    s = m.group(1);
                    chapters.put(s, new ArrayList<Chapter>());
                    chapters.get(s).add(chapter);
                }
            } else {
//                if (s.equals("")) {
//                    chapters.get("unknown").add(chapter); // TODO сделать проверку на ненахождение томов
//                } else {
//                    chapters.get(s).add(chapter);
//                }

                log.warn("Chapter has no volume: " + chapter.getChapterName());
            }
        }

        return chapters;
    }

    protected static boolean checkUrl(String url, Label footerLabel) {
        if (url.isBlank()) {
            footerLabel.setText("URL-адрес не задан!");
            return false;
        }

        Pattern p = Pattern.compile("^https://ranobelib.me/[A-Za-z0-9-]+[/?].*$");
        Matcher m = p.matcher(url);
        if (m.find()) {
            return true;
        } else {
            log.error("Url is not walid: " + url);
            footerLabel.setText("Проверьте URL-адрес!");
        }

        return false;
    }

    protected static boolean checkPath(String pathToSave, Label footerLabel) {
        if (pathToSave.isBlank()) {
            footerLabel.setText("Путь для сохранения не задан!");
            return false;
        }

        try {
            java.nio.file.Paths.get(pathToSave);
            return true;
        } catch (InvalidPathException e) {
            log.error(e.getLocalizedMessage());
            footerLabel.setText("Проверьте путь для сохранения!");
        }

        return false;
    }

    protected static void reverseTable(TableView<TableRow> tableView, Label footerLabel) {
        if (isReversed) {
            tableView.getItems().setAll(tableList);
            isReversed = false;
            footerLabel.setText("Включен прямой порядок");
        } else {
            tableView.getItems().setAll(tableListReversed);
            isReversed = true;
            footerLabel.setText("Включен обратный порядок");
        }
    }

}
