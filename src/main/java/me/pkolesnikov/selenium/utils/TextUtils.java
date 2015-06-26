package me.pkolesnikov.selenium.utils;

import ru.yandex.qatools.htmlelements.element.HtmlElement;

public interface TextUtils {
    /**
     * This method takes text from the HTML element then strip any non-digit chars and convert it to int.
     * Usefull for something like "2 000руб."
     */
    static int elementTextAsInteger(HtmlElement el) {
        final String preparedString = el.getText().replaceAll("[^0-9]+", "");
        return Integer.parseInt(preparedString);
    }
}
