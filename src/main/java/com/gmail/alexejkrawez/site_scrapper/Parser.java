package com.gmail.alexejkrawez.site_scrapper;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static final Logger log = LoggerFactory.getLogger(Parser.class);
    private static Document document;
    private static DocumentCreator documentCreator;
    private static String title;

    private static final String CHAPTERS_GETTER = "//div[@data-reader-modal='chapters']";
    private static final String CHAPTERS = "menu__item";
    private static final String CHAPTER_CONTAINER = "div.reader-container.container.container_center";
//    private static final String REGEXP = "--““А-а?Я не —могу- ‘поверить, что \"папа.\" это забыл!“- раздался' -–с \"кухни—- выкрик Тули.Насколько –я\u00A0 помню,папа ““попросил приготовь““  ““это““ \u00A0  для меня,    мне  оно  понадобится        на работе...”,отчего мама выглядела раздражённой,так как была всегда очень занята по утрам?а он не предупредил её заранее.";
    private static final String[][] PATTERNS = {
        // doubleSpace
        {"[\\u00A0\\s]{2,}", " "},
        // dash 1
        {"[\\u00A0\\s][-–—]{1,2}([^\\u00A0\\s])", " —\u00A0$1"},
        // dash 2
        {"([^\\u00A0\\s])[-–—]{1,2}[\\u00A0\\s]", "$1 —\u00A0"},
        // dash 3
        {"^[-–—]{1,2}([^\\u00A0\\s])", "—\u00A0$1"},
        // dot, question mark, exclamation mark, comma, semicolon with symbol
        {"([\\.,\\?!;])([^\\u00A0\\s\\.,\\?!\"'‚‘‛’‟„“”‹›«»「」『』(){}\\[\\]])", "$1 $2"},
        // quotes and dots 1
        {"\\.([\"'‚‘‛’‟„“”‹›«»「」『』〈〉《》(){}\\[\\]])", "$1."},
        // quotes and dots 2
        {"\\.\\.([\"'‚‘‛’‟„“”‹›«»「」『』〈〉《》(){}\\[\\]])\\.", "...$1"},
        // little spruces 1
        {"[\"'‚‘‛’‟„“”‹›«»(){}\\[\\]]([^\\u00A0\\s])", "«$1"},  // 「」『』〈〉《》 сомнительно
        // little spruces 2
        {"([^\\u00A0\\s])[\"'‚‘‛’‟„“”‹›«»(){}\\[\\]]", "$1»"},
        // little spruces 3
        {"[\"'‚‘‛’‟„“”‹›«»(){}\\[\\]]{2}([^\\u00A0\\s])", "««$1"},
        // little spruces 4
        {"([^\\u00A0\\s])[\"'‚‘‛’‟„“”‹›«»(){}\\[\\]]{2}", "$1»»"},
    };


    protected static List<Chapter> getTableOfContents(String url) {
//        // create a browser
//        EdgeOptions options = new EdgeOptions();
//        options.addArguments("--headless=new");
//        options.addArguments("--start-maximized");
//        EdgeDriver driver = new EdgeDriver(options);
//
//        // get url from TextField
//        driver.navigate().to(url);
//
//        // get table of contents
//        WebElement chaptersGetter = driver.findElement(By.xpath(CHAPTERS_GETTER));
//        new Actions(driver).moveToElement(chaptersGetter).click().perform();
//        List<WebElement> chaptersList = driver.findElements(By.className(CHAPTERS));
//        List<Chapter> tableOfContents = new ArrayList<>(chaptersList.size());
//        chaptersList.forEach(element ->
//                tableOfContents.add(new Chapter(element.getText(), element.getAttribute("href")))
//        );
//        Collections.reverse(tableOfContents);
//
//        log.info("Table of content downloaded successfully");
//        driver.quit();
        List<Chapter> tableOfContents = new ArrayList<>();
        tableOfContents.add(new Chapter("Том 1 Глава 123 Нисхождение владыки небес до земли и восхождение обратно", "https://ranobelib.me/"));
        tableOfContents.add(new Chapter("Том 2 Глава 123 Нисхождение владыки небес до земли и восхождение обратно", "https://ranobelib.me/"));
        tableOfContents.add(new Chapter("Том 3 Глава 123 Нисхождение владыки небес до земли и восхождение обратно", "https://ranobelib.me/"));
        tableOfContents.add(new Chapter("Том 4 Глава 123 Нисхождение владыки небес до земли и восхождение обратно", "https://ranobelib.me/"));
        tableOfContents.add(new Chapter("Том 5 Глава 123 Нисхождение владыки небес до земли и восхождение обратно", "https://ranobelib.me/"));
        tableOfContents.add(new Chapter("Том 6 Глава 123 Нисхождение владыки небес до земли и восхождение обратно", "https://ranobelib.me/"));
        tableOfContents.add(new Chapter("Том 7 Глава 123 Нисхождение владыки небес до земли и восхождение обратно", "https://ranobelib.me/"));
        tableOfContents.add(new Chapter("Том 8 Глава 123 Нисхождение владыки небес до земли и восхождение обратно", "https://ranobelib.me/"));
        tableOfContents.add(new Chapter("Том 9 Глава 123 Нисхождение владыки небес до земли и восхождение обратно", "https://ranobelib.me/"));
        tableOfContents.add(new Chapter("Том 10 Глава 123 Нисхождение владыки небес до земли и восхождение обратно", "https://ranobelib.me/"));
        tableOfContents.add(new Chapter("Том 11 Глава 123 Нисхождение владыки небес до земли и восхождение обратно", "https://ranobelib.me/"));
        tableOfContents.add(new Chapter("Том 12 Глава 123 Нисхождение владыки небес до земли и восхождение обратно", "https://ranobelib.me/"));
        tableOfContents.add(new Chapter("Том 13 Глава 123 Нисхождение владыки небес до земли и восхождение обратно", "https://ranobelib.me/"));
        tableOfContents.add(new Chapter("Том 14 Глава 123 Нисхождение владыки небес до земли и восхождение обратно", "https://ranobelib.me/"));
        return tableOfContents;
    }

//    private static List<Chapter> getTableOfContents(EdgeDriver driver) {
//        WebElement chaptersGetter = driver.findElement(By.xpath(CHAPTERS_GETTER));
//        new Actions(driver).moveToElement(chaptersGetter).click().perform();
//        List<WebElement> chaptersList = driver.findElements(By.className(CHAPTERS));
//        List<Chapter> chaptersLinks = new ArrayList<>(chaptersList.size());
//        chaptersList.forEach(element ->
//                chaptersLinks.add(new Chapter(element.getText(), element.getAttribute("href")))
//        );
//        Collections.reverse(chaptersLinks);
//        log.info("Table of content downloaded successfully");
//        return chaptersLinks;
//    }

    // to index is included
    protected static void getData(int from, int to, List<Chapter> tableOfContents) {
//        List<Chapter> tableOfContents = getTableOfContents(url);

        // get chapters
        title = getTitleName(tableOfContents.get(0).getChapterLink());
        documentCreator = new DocumentCreator();
        documentCreator.createDocument(title);
        getChapters(7, 8, tableOfContents); //TODO заменить на адекват
//        getChapters(tableOfContents);

        // save document
        documentCreator.saveDocument(title);
    }


    private static void getChapters(List<Chapter> tableOfContents) {
        getChapters(0, tableOfContents.size() - 1, tableOfContents);
    }

    // to index is included
    private static void getChapters(int from, int to, List<Chapter> tableOfContents) {
        List<Chapter> chapters = tableOfContents;

        if (from != 0 && tableOfContents.size() - 1 != to) {
            chapters = tableOfContents.subList(from, to + 1); //TODO: нарезать на несколько листов по 50- штук в каждом
        }

        for (int i = 0, chaptersSize = chapters.size() - 1; i < chaptersSize; i++) {
            Chapter chapter = chapters.get(i);
            documentCreator.addChapterName(chapter.getChapterName());
            getChapter(chapter);
            documentCreator.addPageBreak();
        }

        // last chapter without page break
        Chapter chapter = chapters.get(chapters.size() - 1);
        documentCreator.addChapterName(chapter.getChapterName());
        getChapter(chapter);
    }

    private static void getChapter(Chapter chapter) {
        log.info("Load: " + chapter.getChapterName());
        try {
            document = Jsoup.connect(chapter.getChapterLink())
                    .userAgent("Mozilla/5.0")
                    .get();
            Elements data = document.select(CHAPTER_CONTAINER).get(0).children();
            data.forEach(element -> parseData(element));
        } catch (IOException e) {
            log.error(chapter.getChapterName() + e.getLocalizedMessage());
        }
    }

    private static void parseData(Element element) {
        switch (element.tagName()) {
            case "p":
                documentCreator.addTextParagraph(checkSpelling(element.text()));
                break;
            case "div":
                Optional<String> source = Optional.of(element.firstElementChild().attr("data-src"));
                source.ifPresentOrElse(url -> documentCreator.addImgParagraph(url),
                                        () -> log.error("Image not found! " + element.outerHtml()));
                break;
            default:
                log.warn(element.tagName() + " cannot be parsed");
        }
    }

    private static String checkSpelling(String text) {
        String trimmedText = text.strip();

        if (trimmedText.contains("http")) {
            return trimmedText;
        }

        for (String[] pattern : PATTERNS) {
            trimmedText = trimmedText.replaceAll(pattern[0], pattern[1]); //TODO всё же попытаться на стрингбилдер переписать
        }

        return trimmedText;
    }

    private static String getTitleName(String url) {
        Pattern p = Pattern.compile("ranobelib.me/([A-Za-z0-9-]+)/");
        Matcher m = p.matcher(url);
        if (m.find()) {
            String capitalized = m.group(1).substring(0, 1).toUpperCase() + m.group(1).substring(1);
            if (capitalized.matches("^[A-Za-z0-9-]+novel$")) {
                capitalized = capitalized.substring(0, capitalized.length() - 6);
            }
            return capitalized.replaceAll("-", " ");
        } else {
            log.error("Title name not found in url: " + url);
            return "Unknown";
        }
    }

    //TODO: поиск ссылки на следующую главу или всё -
    //TODO: наполнение главы +
    //TODO: переход на следующую интернет-страницу и повтор +
    //TODO: возможно вордовские объекты-документы делать новые (возможно через 5-10 глав) и потом склеивать их.
    //TODO: или даже после определённого количества глав временно их выгружать, а то поломается на 700+ главе и кирдык
    //TODO: сделать кнопочку+поле для записи только ограниченного числа глав, а не до победы

    //TODO: метод с созданием вордовского документа +
    //TODO: метод и переменная, содержащие стандартный абзац для текста с настроенным стилем (или стиль можно применить глобально?) + глобально нельзя
    //TODO: метод и переменная, содержащие стандартный заголовок главы с настроенным стилем +
    //TODO: метод с проверкой ошибок в этом абзаце +
    //TODO: метод с добавлением текста в стандартный абзац и (сразу или нет?) добавлением в документ +
    //TODO: метод с добавлением разрыва страницы в конце каждой главы + но добавляет и к последней главе, что тупо
    //TODO: сохранение документа +


}
