package org.example.driver;

import org.example.driver.chromedriver.MyChromeDriver;
import org.openqa.selenium.WebDriver;


public class DriverFactory {
    private static final MyChromeDriver myChromeDriver = MyChromeDriver.getInstance();

    public static WebDriver getDriver(Type type) {
        if (type == Type.CHROME) {
            return myChromeDriver.getDriver();
        }
        return null;
    }
}
