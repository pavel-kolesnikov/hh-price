package me.pkolesnikov.selenium.elements;

import org.openqa.selenium.support.FindBy;
import ru.yandex.qatools.htmlelements.element.Button;
import ru.yandex.qatools.htmlelements.element.HtmlElement;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static me.pkolesnikov.selenium.utils.TextUtils.elementTextAsInteger;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static ru.yandex.qatools.htmlelements.matchers.decorators.MatcherDecoratorsBuilder.should;
import static ru.yandex.qatools.htmlelements.matchers.decorators.TimeoutWaiter.timeoutHasExpired;
import static ru.yandex.qatools.matchers.webdriver.EnabledMatcher.enabled;

public final class SpecialOffersTab extends HtmlElement {

    //TODO: inject proper css class here
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @FindBy(css = "div.price-spoffers > div.m-colspan2")
    private List<SpecialOfferItem> availableItems;

    public List<SpecialOfferItem> availableItems() {
        return availableItems;
    }

    public SpecialOfferItem purchase(int i) {
        final SpecialOfferItem item = availableItems.get(i);
        item.purchase();

        return item;
    }

    public SpecialOfferItem purcaseSome() {
        final SpecialOfferItem specialOfferItem = availableItems.stream()
                .filter(SpecialOfferItem::isEnabled)
                .findAny()
                .get();

        specialOfferItem.purchase();

        return specialOfferItem;
    }

    public static final class SpecialOfferItem extends HtmlElement {
        @FindBy(className = "price-spoffers__actual-price")
        private HtmlElement actualPriceSpan;

        @FindBy(className = "HH-Price-SpecialOffer-AddToCartButton")
        private Button purchase;

        public int actualPrice() {
            return elementTextAsInteger(actualPriceSpan);
        }

        public boolean isEnabled() {
            return purchase.isEnabled();
        }

        public void purchase() {
            purchase.click();

            // make sure button os disabled
            assertThat(purchase.getWrappedElement(),
                    should(not(enabled())).whileWaitingUntil(timeoutHasExpired(TimeUnit.SECONDS.toMillis(5))));
        }
    }
}
