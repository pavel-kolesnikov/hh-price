package me.pkolesnikov.selenium;

import me.pkolesnikov.selenium.elements.SpecialOffersTab;
import me.pkolesnikov.selenium.pages.ShopPage;
import org.junit.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class PriceCartTests {
    private static final long IMPLICIT_TIMEOUT_SECONDS = 5;

    private static ChromeDriverService service;
    private WebDriver driver;

    @BeforeClass
    public static void createAndStartService() throws IOException {
        service = new ChromeDriverService.Builder()
                .usingDriverExecutable(new File("resources/chromedriver.exe"))
                .usingAnyFreePort()
                .build();
        service.start();
    }

    @AfterClass
    public static void createAndStopService() {
        service.stop();
    }

    @Before
    public void setupDriver() throws InterruptedException {
        driver = new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());
        driver.manage().timeouts().implicitlyWait(IMPLICIT_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        driver.get("http://hh.ru/price");
    }

    @Test
    public void tabsOk() {
        ShopPage shopPage = new ShopPage(driver);
        Stream.of(ShopPage.Tabs.values()).forEach(shopPage::switchTo);
    }

    @Test
    public void specialOffersCheckoutOk() throws InterruptedException {
        ShopPage shopPage = new ShopPage(driver);
        shopPage.switchTo(ShopPage.Tabs.SpecialOffers);

        final SpecialOffersTab.SpecialOfferItem item = shopPage.specialOffersTab().purchase(1);
        assertEquals(shopPage.cart().totalPrice(), item.actualPrice());

        shopPage.cart().checkout();
        assertTrue(driver.getCurrentUrl().contains("/auth/employer"));
    }

    @Test
    public void specialOffersCheckoutSumOk() throws InterruptedException {
        ShopPage shopPage = new ShopPage(driver);
        shopPage.switchTo(ShopPage.Tabs.SpecialOffers);

        final int expectedTotal = shopPage.specialOffersTab().availableItems().stream()
                .mapToInt(SpecialOffersTab.SpecialOfferItem::actualPrice)
                .sum();

        shopPage.specialOffersTab().availableItems().stream()
                .forEach(SpecialOffersTab.SpecialOfferItem::purchase);

        assertEquals(expectedTotal, shopPage.cart().totalPrice());
    }

    @Test
    public void specialOffersRemoveFromCartOk() throws InterruptedException {
        ShopPage shopPage = new ShopPage(driver);
        shopPage.switchTo(ShopPage.Tabs.SpecialOffers);

        shopPage.specialOffersTab().purcaseSome();
        //TODO: purchase-remove several items

        shopPage.cart().clean();
        shopPage.assertCartEmpty();
    }

    @After
    public void killWebDriver() {
        driver.quit();
    }
}
