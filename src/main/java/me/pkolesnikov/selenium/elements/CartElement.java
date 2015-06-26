package me.pkolesnikov.selenium.elements;

import me.pkolesnikov.selenium.utils.TextUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

import java.util.List;

public final class CartElement extends HtmlElement {
    @FindBy(className = "price-cart__button")
    private Button checkout;

    @FindBy(className = "HH-PriceCart-TotalCost-Actual")
    private HtmlElement totalSpan;

    public int totalPrice() {
        return TextUtils.elementTextAsInteger(totalSpan);
    }

    public void clean() {
        final List<WebElement> items = findElements(By.className("HH-PriceCart-ItemRemover"));
        items.forEach(WebElement::click);
    }

    public void checkout() {
        checkout.click();
    }

    public static final class CartItem {
        @FindBy(className = "HH-PriceCart-ItemRemover")
        private HtmlElement remove;

        public void remove() {
            remove.click();
        }
    }
}
