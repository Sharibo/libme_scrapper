//package com.gmail.alexejkrawez.site_scrapper;
//
//public class Parser2 {

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
