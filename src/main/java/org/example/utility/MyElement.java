
package org.example.utility;

/**
 * @author dogukan
 */
public class MyElement {
    private String property;
    private String htmlCode;
    private String defaultValue;
    private final String[] index;
    private final String[] endIndex;

    public MyElement(String property, String htmlCode, String defaultValue, String[] index, String[] endIndex) {
        this.property = property;
        this.htmlCode = htmlCode;
        this.defaultValue = defaultValue;
        this.index = index;
        this.endIndex = endIndex;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getHtmlCode() {
        return htmlCode;
    }

    public void setHtmlCode(String htmlCode) {
        this.htmlCode = htmlCode;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String[] getIndex() {
        return index;
    }

    public String[] getEndIndex() {
        return endIndex;
    }


}
