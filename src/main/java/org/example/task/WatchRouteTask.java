package org.example.task;

import org.apache.poi.ss.usermodel.Sheet;
import org.example.excel.Excel;
import org.example.utility.MyElement;
import org.example.utility.Utility;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WatchRouteTask extends Task {
    private static final Logger LOGGER = Logger.getLogger(WatchRouteTask.class.getName());

    private final String url;
    private final Excel excel;
    private final Sheet sheet;
    private final int delayTime;
    private final String css;
    private final boolean href;
    private final String hrefCss;
    private final List<MyElement> myElements;

    public WatchRouteTask(String url, Excel excel, Sheet sheet, int delayTime, String css, boolean href, List<MyElement> elements, String hrefCss) {
        this.url = url;
        this.excel = excel;
        this.sheet = sheet;
        this.delayTime = delayTime;
        this.css = css;
        this.href = href;
        this.myElements = elements;
        this.hrefCss = hrefCss;
    }

    @Override
    public void doJob(WebDriver driver) {
        driver.navigate().to(url);
        LOGGER.log(Level.INFO, "Navigated to URL: {0}", url);
        scrollPage(driver);

        Document doc = Jsoup.parse(driver.getPageSource());
        Elements newsHeadlines = doc.select(css);

        LOGGER.log(Level.INFO, "CSS Selector: {0}", css);
        LOGGER.log(Level.INFO, "Extracted headlines text: {0}", newsHeadlines.text());
        LOGGER.log(Level.INFO, "Href flag: {0}", href);

        if (href) {
            processHrefElements(driver, doc, newsHeadlines);
        } else {
            processNonHrefElements(newsHeadlines);
        }

        excel.writeExcel();
        LOGGER.log(Level.INFO, "Excel written successfully.");
    }

    private void scrollPage(WebDriver driver) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        for (int i = 0; i < delayTime * 1000; i++) {
            delay(1);
            jse.executeScript("window.scrollTo(0," + i * 5 + ");");
        }
        LOGGER.log(Level.INFO, "Page scrolled for delay time: {0} seconds", delayTime);
    }

    private void processHrefElements(WebDriver driver, Document doc, Elements newsHeadlines) {
        for (Element headline : newsHeadlines) {
            try {
                String attr = headline.getElementsByAttribute("href").get(0).attr("href");
                driver.navigate().to(Utility.BASE_URL + attr);
                LOGGER.log(Level.INFO, "Navigated to HREF URL: {0}", Utility.BASE_URL + attr);

                doc = Jsoup.parse(driver.getPageSource());
                Elements hrefElements = doc.getElementsByClass(hrefCss);

                for (Element hrefElement : hrefElements) {
                    List<String> elements = extractElementsFromHref(hrefElement);
                    if (!elements.isEmpty()) {
                        LOGGER.log(Level.INFO, "Extracted elements: {0}", elements);
                        excel.writeData(sheet, elements);
                    }
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Exception while processing HREF elements: {0}", e.getMessage());
            }
        }
    }

    private void processNonHrefElements(Elements newsHeadlines) {
        for (Element headline : newsHeadlines) {
            List<String> elements = extractElementsFromHeadline(headline);
            if (!elements.isEmpty()) {
                LOGGER.log(Level.INFO, "Extracted elements: {0}", elements);
                excel.writeData(sheet, elements);
            }
        }
    }

    private List<String> extractElementsFromHref(Element hrefElement) {
        List<String> elements = new ArrayList<>();
        for (MyElement myElement : myElements) {
            elements.addAll(extractElementData(hrefElement, myElement));
        }
        return elements;
    }

    private List<String> extractElementsFromHeadline(Element headline) {
        List<String> elements = new ArrayList<>();
        for (MyElement myElement : myElements) {
            elements.addAll(extractElementData(headline, myElement));
        }
        return elements;
    }

    private List<String> extractElementData(Element element, MyElement myElement) {
        List<String> elements = new ArrayList<>();
        String htmlCode = myElement.getHtmlCode();
        String property = myElement.getProperty();
        String[] beginIndex = myElement.getIndex();
        String[] endIndex = myElement.getEndIndex();

        try {
            if (htmlCode.contains("1...")) {
                elements.add(extractDataUsingClass(element, htmlCode, property, beginIndex, endIndex));
            } else if (htmlCode.contains("2---")) {
                elements.add(extractDataUsingAttribute(element, htmlCode, property, beginIndex, endIndex));
            } else if (htmlCode.contains("3///")) {
                elements.add(extractDataUsingTag(element, htmlCode, property, beginIndex, endIndex));
            } else if (htmlCode.contains("4***")) {
                elements.add(extractDataUsingId(element, htmlCode));
            }
        } catch (Exception ex) {
            elements.add(myElement.getDefaultValue());
            LOGGER.log(Level.WARNING, "Exception while extracting data: {0}", ex.getMessage());
        }

        return elements;
    }

    private String extractDataUsingClass(Element element, String htmlCode, String property, String[] beginIndex, String[] endIndex) {
        int i = 0;
        for (String bI : beginIndex) {
            String text = element.getElementsByClass(htmlCode.substring(4)).get(Integer.parseInt(bI)).text();
            if (Objects.equals(endIndex[i], bI) || text.equalsIgnoreCase(property)) {
                return element.getElementsByClass(htmlCode.substring(4)).get(Integer.parseInt(endIndex[i])).text().replace(property, "").trim();
            }
        }
        return myElements.get(i).getDefaultValue();
    }

    private String extractDataUsingAttribute(Element element, String htmlCode, String property, String[] beginIndex, String[] endIndex) {
        int i = 0;
        for (String bI : beginIndex) {
            String text = element.getElementsByAttribute("alt").get(Integer.parseInt(bI)).attr(htmlCode.substring(4));
            if (Objects.equals(endIndex[i], bI) || text.equalsIgnoreCase(property)) {
                return element.getElementsByAttribute("alt").get(Integer.parseInt(endIndex[i])).attr(htmlCode.substring(4)).replace(property, "");
            }
        }
        return myElements.get(i).getDefaultValue();
    }

    private String extractDataUsingTag(Element element, String htmlCode, String property, String[] beginIndex, String[] endIndex) {
        int i = 0;
        for (String bI : beginIndex) {
            String text = element.getElementsByTag(htmlCode.substring(4)).get(Integer.parseInt(bI)).text();
            if (Objects.equals(endIndex[i], bI) || text.equalsIgnoreCase(property)) {
                return element.getElementsByTag(htmlCode.substring(4)).get(Integer.parseInt(endIndex[i])).text().replace(property, "");
            }
        }
        return myElements.get(i).getDefaultValue();
    }

    private String extractDataUsingId(Element element, String htmlCode) {
        String text = element.getElementById(htmlCode.substring(4)).val();
        if (text.equalsIgnoreCase("")) {
            text = element.getElementById(htmlCode.substring(4)).text();
        }
        return text;
    }
}
