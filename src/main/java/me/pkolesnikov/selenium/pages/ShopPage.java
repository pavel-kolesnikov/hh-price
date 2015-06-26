package me.pkolesnikov.selenium.pages;

import me.pkolesnikov.selenium.elements.CartElement;
import me.pkolesnikov.selenium.elements.SpecialOffersTab;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import ru.yandex.qatools.htmlelements.element.HtmlElement;
import ru.yandex.qatools.htmlelements.loader.decorator.HtmlElementDecorator;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static ru.yandex.qatools.htmlelements.matchers.decorators.MatcherDecoratorsBuilder.should;
import static ru.yandex.qatools.htmlelements.matchers.decorators.TimeoutWaiter.timeoutHasExpired;
import static ru.yandex.qatools.matchers.webdriver.DisplayedMatcher.displayed;

public final class ShopPage {
    private WebDriver driver;

    @FindBy(className = "HH-PriceCart")
    private CartElement cart;

    @FindBy(className = "HH-PriceCart-Empty")
    private HtmlElement emptyCart;

    @FindBy(className = "price-spoffers")
    private SpecialOffersTab specialOffersTab;

    public ShopPage switchTo(Tabs tab) {
        tab.demandFrom(driver).click();
        return this;
    }

    public ShopPage(WebDriver driver) {
        PageFactory.initElements(new HtmlElementDecorator(driver), this);
        this.driver = driver;
    }

    public CartElement cart() {
        return cart;
    }

    public SpecialOffersTab specialOffersTab() {
        return specialOffersTab;
    }

    public boolean cartIsEmpty() {
        return emptyCart.isDisplayed() && !cart.isDisplayed();
    }

    public void assertCartEmpty() {
        assertThat(emptyCart, should(displayed()).whileWaitingUntil(timeoutHasExpired(TimeUnit.SECONDS.toMillis(5))));
    }

    public enum Tabs {
        SpecialOffers("#recommended"),
        DbAccess("#dbaccess"),
        Publications("#publications"),
        Additional("#additional");

        private String href;

        Tabs(String href) {
            this.href = href;
        }

        private WebElement demandFrom(WebDriver driver) {
            return driver.findElement(By.cssSelector("a[href='" + href + "']"));
        }
    }
}
