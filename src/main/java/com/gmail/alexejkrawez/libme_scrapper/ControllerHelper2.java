//package com.gmail.alexejkrawez.libme_scrapper;
//
//import javafx.beans.value.ObservableValue;
//import javafx.scene.control.Hyperlink;
//import javafx.scene.control.Label;
//import javafx.scene.control.ListView;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.cell.CheckBoxListCell;
//import javafx.scene.control.cell.CheckBoxTableCell;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.util.Callback;
//import org.slf4j.Logger;
//
//import java.util.List;
//
//import static org.slf4j.LoggerFactory.getLogger;
//
//public class ControllerHelper2 {
//
//    private static final Logger log = getLogger(ControllerHelper2.class);
//
//    protected static void initializeTableView(TableView<ListViewRow> tableView) {
//        tableView.getStyleClass().add("noheader"); // TODO переделать
//        tableView.setEditable(true);
//
//        TableColumn<ListViewRow, Boolean> checkboxColumn = new TableColumn<>("First Name"); //TODO переименовать
//        checkboxColumn.setCellValueFactory(new PropertyValueFactory<>("checkbox"));
//        checkboxColumn.setCellFactory( tc -> new CheckBoxTableCell<>());
//
//        TableColumn<ListViewRow, String> nameColumn = new TableColumn<>("Two Name");  //TODO переименовать
//        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
//
//        TableColumn<ListViewRow, Hyperlink> urlColumn = new TableColumn<>("First Name");  //TODO переименовать
//        urlColumn.setCellValueFactory(new PropertyValueFactory<>("url"));
//
//        tableView.getColumns().add(checkboxColumn);
//        tableView.getColumns().add(nameColumn);
//        tableView.getColumns().add(urlColumn);
//    }
//
//    protected static void showChapters(ListView<ListViewRow> listView,
//                                       List<Chapter> tableOfContents,
//                                       Label footerLabel) {
//
//        if (tableOfContents != null && !tableOfContents.isEmpty()) {
//
//            for (Chapter chapter : tableOfContents) {
//                ListViewRow listViewRow = new ListViewRow(false, chapter.getChapterName(), chapter.getChapterLink());
//
//                // observe item's on property and display message if it changes:
//                listViewRow.checkboxProperty().addListener((obs, wasOn, isNowOn) -> {
//                    log.info(listViewRow.getName() + " changed on state from " + wasOn + " to " + isNowOn);
//                });
//
//                listView.getItems().add(listViewRow);
//            }
//
//            listView.setCellFactory(CheckBoxListCell.forListView(new Callback<ListViewRow, ObservableValue<Boolean>>() {
//                @Override
//                public ObservableValue<Boolean> call(ListViewRow listViewRow) {
//                    return listViewRow.checkboxProperty();
//                }
//            }));
//
////            setCellFactory(TextFieldListCell.forListView(new Callback<ListViewRow, ObservableValue<String>>() {
////                @Override
////                public ObservableValue<String> call(ListViewRow listViewRow) {
////                    return listViewRow.nameProperty();
////                }
////            }));
////
//
//
//
////            int size = 0;
////            for (Chapter file : tableOfContents) {
////                listView.getItems().add(file); // TODO переделать
////                size++;
////            }
////
////            if (size > 0) {
////                if (size == 1) {
////                    footerLabel.setText("Added 1 file"); // TODO переделать
////                } else {
////                    footerLabel.setText("Added " + size + " files");
////                }
////            }
//
////            if (listView.getItems().size() == 1) {
////                removeSelected.setDisable(false);
////                removeAll.setDisable(false);
////            } else if (listView.getItems().size() > 1) {
////                setButtonsActive();
////            }
//
//        }
//    }
//
//
//
//}


//            CheckBox checkBox = new CheckBox();
//            TextField name = new TextField(chapter.getChapterName());
//            name.setMaxWidth(150.0);
//            name.setMinHeight(20);
//            TextField url = new TextField(chapter.getChapterLink());
//            url.setMaxWidth(50.0);
//            url.setMinHeight(20);


//            ListViewRow listViewRow = new ListViewRow(false, chapter.getChapterName(), chapter.getChapterLink());
//
//            // observe item's on property and display message if it changes:
//            listViewRow.checkboxProperty().addListener((obs, wasOn, isNowOn) -> {
//                log.info(listViewRow.getName() + " changed on state from " + wasOn + " to " + isNowOn);
//            });
//
////            listView.getItems().add(listViewRow);
//            listCheckBox.setSelectedStateCallback(new Callback<CheckBox, ObservableValue<Boolean>>() {
//                @Override
//                public ObservableValue<Boolean> call(CheckBox param) {
//                    return listViewRow.checkboxProperty();
//                }
//            });
//            listText.setText(chapter.getChapterName());
//            listUrl.setText(chapter.getChapterLink());
//        }

//        if (tableOfContents.isEmpty()) {
//            footerLabel.setText(""); // TODO что-то внятное сообщить
//        } else {
//            //TODO вывести на экран + анимация ожидания / блок интерфейса?
//            ControllerHelper.showChapters(listView, tableOfContents, footerLabel);
//        }


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
