//package com.gmail.alexejkrawez.libme_scrapper;
//
//public class Parser2 {

//https://overcoder.net/q/611114/%D0%BE%D1%87%D0%B8%D1%81%D1%82%D0%B8%D1%82%D1%8C-%D1%82%D0%B5%D0%BA%D1%81%D1%82-%D0%BF%D0%BE%D0%B4%D1%81%D0%BA%D0%B0%D0%B7%D0%BA%D0%B8-%D0%B2-javafx-textfield-%D1%82%D0%BE%D0%BB%D1%8C%D0%BA%D0%BE-%D1%82%D0%BE%D0%B3%D0%B4%D0%B0-%D0%BA%D0%BE%D0%B3%D0%B4%D0%B0-%D0%BF%D0%BE%D0%BB%D1%8C%D0%B7%D0%BE%D0%B2%D0%B0%D1%82%D0%B5%D0%BB%D1%8C
//https://stackoverflow.com/questions/32389447/javafx-tableview-row-selection
//https://www.jenkov.com/tutorials/javafx/tableview.html#hide-columns
//create a browser
//        EdgeOptions options = new EdgeOptions();
//        options.addArguments("window-size=1920x1080"); //not working?
//                options.addArguments("--headless=new");
//                options.addArguments("--start-maximized");
//        options.setExperimentalOption("useAutomationExtension", false);
//        options.addArguments("--disable-blink-features=AutomationControlled");
//                EdgeDriver driver = new EdgeDriver(options);
//        driver.manage().window().maximize();
//        EdgeDriver driver = new EdgeDriver();


//<!--        <dependency>-->
//<!--            <groupId>org.seleniumhq.selenium</groupId>-->
//<!--            <artifactId>selenium-http-jdk-client</artifactId>-->
//<!--            <version>4.8.3</version>-->
//<!--        </dependency>-->
//        System.setProperty("webdriver.http.factory", "jdk-http-client");
//        options.addArguments("--start-maximized");



                //get url from TextField
//                driver.navigate().to(url);

//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
//        WebElement element = wait
//                .until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(CHAPTER_TITLE))));

                //get table of contents
//                List<Chapter> tableOfContents = getTableOfContents(driver);
//        driver.quit();

//        driver.close();
//        driver.switchTo().newWindow(WindowType.TAB);
//        driver.navigate().to(tableOfContents.get(tableOfContents.size() - 1).getChapterLink());
//        List<WebElement> data = driver.findElements(By.xpath(CHAPTER_CONTAINER));
//        data.forEach(e -> log.info(e.getText() + "\n"));


//        List<WebElement> menu__item = wait
//                .until(ExpectedConditions.visibilityOfAllElements(driver.findElements(By.className("menu__item"))));

//        FirefoxOptions options = new FirefoxOptions();
//        options.addArguments("-headless");
//        FirefoxDriver driver = new FirefoxDriver(options);
//        driver.navigate().to(url);
//        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.className(CHAPTERS)));
////        WebDriverWait wait = new WebDriverWait(driver, 30)
//        List<WebElement> e = new ArrayList<>();
//        wait.until(webDriver -> {
//            List<WebElement> a = webDriver.findElements(By.className(CHAPTERS));
//            e.addAll(a);
//            return true;
//        });
//        List<WebElement> chaptersList = driver.findElements(By.className(CHAPTERS));
//        List<String> chaptersLinks = new ArrayList<>();
//        for (WebElement chapter : chaptersList) {
//            WebElement link = chapter.findElement(By.xpath(LINK));
//            chaptersLinks.add(link.getAttribute("href"));
//        }
//        System.out.println(chaptersLinks);



//
//    //BrowserVersion.FIREFOX
////        try (final WebClient webClient = new WebClient()) {
////            webClient.getOptions().setThrowExceptionOnScriptError(false);
////            final HtmlPage page = webClient.getPage(url);
////            final DomNodeList<DomNode> divs = page.querySelectorAll("div.vue-recycle-scroller__item-wrapper");
////
////
////        } catch (MalformedURLException e) {
////            throw new RuntimeException(e);
////        } catch (IOException e) {
////            throw new RuntimeException(e);
////        }
//
//
////        UserAgent userAgent = new UserAgent();
////        try {
////            userAgent.visit(url);
////        } catch (ResponseException e) {
//////            throw new RuntimeException(e);
////        }
////        Elements every = userAgent.doc.findEvery("<div class=vue-recycle-scroller__item-view>");
//
////        try {
////            Document document = Jsoup.connect(url)
////                    .userAgent("Mozilla/5.0")
////                    .get();
////            Elements elements = document.getElementsByClass(CHAPTER);
////            System.out.println(elements);
////        } catch (IOException e) {
//////            throw new RuntimeException(e);
////        }
//}

//private static void parseData(Element element) {
//        switch (element.tagName()) {
//        case "p":
//        log.info(element.text());
//        break;
//        case "div":
//        log.info(element.firstElementChild().attr("data-src"));
//        break;
//default:
//        log.warn(element.tagName());
//        }
//        }

//    private static void parseData(Element element) {
//        log.info(element.getTagName());
//        switch (element.getTagName()) {
//            case "p":
//                log.info(element.getText());
//                break;
//            case "a":
//                log.info("data-src: " + element.getAttribute("data-src"));
//                log.info("href: " + element.getAttribute("href"));
//                log.info("href: " + element.getText());
//                break;
//            default:
//                log.warn(element.getTagName());
//        }
//    }

//        Pattern[] patterns = {
//                Pattern.compile("\\s\\s"),              // doubleSpace
//                Pattern.compile("\\.\\S"),              // dot
//                Pattern.compile("\\S[-–—]\\S"),         // dash1
//                Pattern.compile("\\S[-–—]\\s"),         // dash2
//                Pattern.compile("[-–—]\\S"),            // dash3
//                Pattern.compile("[-–]"),                // dash4
//                Pattern.compile("\\.[\"”»]"),           // quotes1
//                Pattern.compile("\\.\\.[\"”»]\\.")      // quotes2
//        };


/*      DOCUMENT CREATOR      */
//DocumentSettingsPart documentSettings = document.getDocumentSettingsPart();
//        try {
//        documentSettings.getContents().setThemeFontLang(language);
//        documentSettings.getContents().setHideGrammaticalErrors(new BooleanDefaultTrue());
//        documentSettings.getContents().setHideSpellingErrors(new BooleanDefaultTrue());
//
//        DocDefaults.PPrDefault pPrDefault = new DocDefaults.PPrDefault();
//        pPrDefault.setPPr(paragraphProperties);
//
//        DocDefaults.RPrDefault rPrDefault = new DocDefaults.RPrDefault();
//        rPrDefault.setRPr(propertiesText);
//        styles.getContents().getDocDefaults().setRPrDefault(rPrDefault);
//        } catch (Docx4JException e) {
//        log.error(e.getLocalizedMessage());
//        }


//        PPrBase.Ind firstLine = new PPrBase.Ind();
//        firstLine.setFirstLine(BigInteger.valueOf(284));
//        paragraphProperties.setInd(firstLine);


//        wrapper = factory.createR();
//        text = factory.createText();
//        drawing = factory.createDrawing();
//        propertiesText = factory.createRPr();
//        propertiesText.setLang(language);
//        color = factory.createColor();


//        parts = word.getParts();
//        parts.put(fonts);
//        parts.put(styles);



//    private void style() {
//        text.setValue("Welcome To Baeldung");
//        wrapper.getContent().add(text);
//        paragraph.getContent().add(wrapper);
//
//        BooleanDefaultTrue b = new BooleanDefaultTrue();
//        propertiesText.setB(b);
//        propertiesText.setI(b);
//        propertiesText.setCaps(b);
//        color.setVal("green");
//        propertiesText.setColor(color);
//
//        wrapper.setRPr(propertiesText);
//        document.getContent().add(paragraph);
//    }



//    public FontTablePart getFontTablePart();
//    public StyleDefinitionsPart getStyleDefinitionsPart();
//    public EndnotesPart getEndNotesPart();
//    public DocumentSettingsPart getDocumentSettingsPart();
//    RelationshipsPart rp = part.getRelationshipsPart();
//    factory.createBr() //Page break


// Populate it with default styles
//    stylesPart.unmarshalDefaultStyles();

// Add the styles part to the main document part relationships
//    wordDocumentPart.addTargetPart(stylesPart);