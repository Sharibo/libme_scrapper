//package com.gmail.alexejkrawez.site_scrapper;
//
//import javafx.beans.value.ObservableValue;
//import javafx.scene.control.Label;
//import javafx.scene.control.ListView;
//import javafx.scene.control.cell.CheckBoxListCell;
//import javafx.scene.control.cell.TextFieldListCell;
//import javafx.util.Callback;
//import org.slf4j.Logger;
//
//import java.util.List;
//
//import static org.slf4j.LoggerFactory.getLogger;
//
//public class ControllerHelper {
//
//    private static final Logger log = getLogger(ControllerHelper.class);
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
//
//
//
//
//}
