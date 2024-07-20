package org.example.driver.chromedriver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class MyChromeDriver {

    private static MyChromeDriver instance = new MyChromeDriver();

    private final WebDriver driver = new ChromeDriver();

    public static MyChromeDriver getInstance() {
        return instance;
    }

    private MyChromeDriver() {
    }

    public WebDriver getDriver() {
        return driver;
    }
}
