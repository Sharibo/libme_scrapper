package com.gmail.alexejkrawez.libme_scrapper;

import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.slf4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.net.URI;

import static org.slf4j.LoggerFactory.getLogger;

public class HelpWindowController {

    private static final Logger log = getLogger(HelpWindowController.class);
    private static final String HELP = "     lib.me scrapper позволяет выкачивать книги с сайта ranobelib.me в виде вордовских документов.\n" +
            "     Для начала вставьте ссылку нужного произведения (любую главу или главную страницу). " +
            "Нажмите кнопку \"Загрузить главы\" и дождитесь отображения списка глав в окне ниже. Это может занимать продолжительное время, " +
            "в зависимости от количества глав в произведении.\n" +
            "     Далее требуется отметить желаемые главы.\n" +
            "     После выберите путь, по которому будет происходить сохранение, нажав на троеточие в правой части поля \"Путь для сохранения\". " +
            "Откроется диалоговое окно с выбором папки. Подтвердив выбор, вы увидите данный путь в поле \"Путь для сохранения\".\n" +
            "     Нажмите кнопку \"Сохранить\" и дождитесь сообщения об успешном сохранении. Это также может занимать продолжительное время, " +
            "на произведении в 1000+ глав сохранение может длится более 10 минут.";
    private static final String ABOUT = "\n     Автор: Sharibo\n     Версия: 1.0. Последнюю версию см. на github.com:";
    private static final String GITHUB = "https://github.com";

    @FXML
    Label helpText;
    @FXML
    Label aboutText;
    @FXML
    Hyperlink githubLink;

    @FXML
    public void initialize() {
        helpText.setText(HELP);
        aboutText.setText(ABOUT);
        githubLink.setText(GITHUB);
        githubLink.setOnAction(e -> {
            githubLink.setVisited(false);
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(URI.create(githubLink.getText().strip()));
            } catch (IOException ex) {
                log.error("Error by opening github-url");
            }
        });
    }

    @FXML
    public void closeDialog() {
        Stage stage = (Stage) helpText.getScene().getWindow();
        stage.close();
    }
}
