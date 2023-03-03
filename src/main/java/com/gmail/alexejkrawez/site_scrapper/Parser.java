package com.gmail.alexejkrawez.site_scrapper;

import org.checkerframework.checker.nullness.Opt;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Parser {

    private static final Logger log = LoggerFactory.getLogger(Parser.class);
    private static Document document;
    private static DocumentCreator documentCreator;

//    private static final String CHAPTERS = "/html/body/div[3]/div/div/div/div[2]/div[2]/div[3]/div/div[2]/div[1]/div[1]/div";
//    private static final String FOOTER = "footer__nav-link";
//    private static final String CHAPTERS = "vue-recycle-scroller__item-view";
//    private static final String LINK = "div/div[2]/div[1]/a";
//    private static final String LINK = "div/div[2]/div[1]/a";

    //    private static final String CHAPTER_TITLE = "//div[@data-reader-modal='chapters']";
    private static final String CHAPTERS_GETTER = "//div[@data-reader-modal='chapters']";
    private static final String CHAPTERS = "menu__item";
    private static final String NEXT_CHAPTERS_LINK = ".vue-recycle-scroller__item-view";
    //    private static final String CHAPTER_CONTAINER = "div[@class='reader-container container container_center']";
    private static final String CHAPTER_CONTAINER = "div.reader-container.container.container_center";


    protected static void getData(String url) {
        documentCreator = new DocumentCreator();
        documentCreator.createDocument("123");
        documentCreator.addTextParagraph("VALERA!^p Помоги, \nпожажа. . .");
        documentCreator.addImgParagraph("https://ranobelib.me/uploads/ranobe/ascendance-of-a-bookworm-novel/chapters/1795657/a674b50a-1ef1-4a92-8df7-9ed4f17981c3_R198.jpg");
        documentCreator.addImgParagraph("https://cover.imglib.info/uploads/cover/ascendance-of-a-bookworm-novel/cover/SyZ8gnRS2bI5_250x350.jpg");
        documentCreator.saveDocument("456");
        //create a browser
        EdgeOptions options = new EdgeOptions();
//        options.addArguments("window-size=1920x1080"); //not working?
        options.addArguments("--headless=new");
        options.addArguments("--start-maximized");
//        options.setExperimentalOption("useAutomationExtension", false);
//        options.addArguments("--disable-blink-features=AutomationControlled");
        EdgeDriver driver = new EdgeDriver(options);
//        driver.manage().window().maximize();
//        EdgeDriver driver = new EdgeDriver();

        //get url from TextField
        driver.navigate().to(url);

//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
//        WebElement element = wait
//                .until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(CHAPTER_TITLE))));

        //get table of contents
        List<Chapter> tableOfContents = getTableOfContents(driver);
        driver.quit();

        //get first chapter TODO: добавить цикл
//        driver.close();
//        driver.switchTo().newWindow(WindowType.TAB);
//        driver.navigate().to(tableOfContents.get(tableOfContents.size() - 1).getChapterLink());
//
//        List<WebElement> data = driver.findElements(By.xpath(CHAPTER_CONTAINER));
//        data.forEach(e -> log.info(e.getText() + "\n"));

        getChapter(1, tableOfContents);
    }


    private static List<Chapter> getTableOfContents(EdgeDriver driver) {
        WebElement chaptersGetter = driver.findElement(By.xpath(CHAPTERS_GETTER));
        new Actions(driver).moveToElement(chaptersGetter).click().perform();
        List<WebElement> chaptersList = driver.findElements(By.className(CHAPTERS));
        List<Chapter> chaptersLinks = new ArrayList<>(chaptersList.size());
        chaptersList.forEach(element ->
                chaptersLinks.add(new Chapter(element.getText(), element.getAttribute("href")))
        );
        Collections.reverse(chaptersLinks);
        return chaptersLinks;
    }

    private static void getChapter(int index, List<Chapter> tableOfContents) {
        try {
            Document document = Jsoup.connect(tableOfContents.get(index).getChapterLink())
                    .userAgent("Mozilla/5.0")
                    .get();
            Elements data = document.select(CHAPTER_CONTAINER).get(0).children();
            data.forEach(element -> parseData(element));
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
    }

    private static void parseData(Element element) {
        switch (element.tagName()) {
            case "p":
                documentCreator.addTextParagraph(element.text());
//                log.info(element.text());
                break;
            case "div":
                Optional<String> source = Optional.of(element.firstElementChild().attr("data-src"));
                source.ifPresentOrElse(url -> documentCreator.addImgParagraph(url),
                                        () -> log.error("Image not found! " + element.outerHtml()));
//                documentCreator.addImgParagraph(element.firstElementChild().attr("data-src"));
//                documentCreator.createImg("")

                log.info(element.firstElementChild().attr("data-src"));
                break;
            default:
                log.warn(element.tagName());
        }
    }

    //TODO: поиск ссылки на следующую главу или всё -
    //TODO: наполнение главы +
    //TODO: переход на следующую интернет-страницу и повтор
    //TODO: возможно вордовские объекты-документы делать новые (возможно через 5-10 глав) и потом склеивать их.
    //TODO: или даже после определённого количества глав временно их выгружать, а то поломается на 700+ главе и кирдык
    //TODO: сделать кнопочку+поле для записи только ограниченного числа глав, а не до победы

    //TODO: метод с созданием вордовского документа +
    //TODO: метод и переменная, содержащие стандартный абзац для текста с настроенным стилем (или стиль можно применить глобально?) +
    //TODO: метод и переменная, содержащие стандартный заголовок главы с настроенным стилем
    //TODO: метод с проверкой ошибок в этом абзаце
    //TODO: метод с добавлением текста в стандартный абзац и (сразу или нет?) добавлением в документ
    //TODO: метод с добавлением разрыва страницы в конце каждой главы
    //TODO: сохранение документа


}
