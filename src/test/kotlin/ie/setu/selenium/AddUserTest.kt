import ie.setu.config.DbConfig
import ie.setu.config.JavalinConfig
import io.javalin.Javalin
import io.javalin.testtools.JavalinTest
import org.hamcrest.CoreMatchers
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.openqa.selenium.By

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.support.ui.WebDriverWait


class DefaultSuiteTest {
    private var wait: WebDriverWait? = null
    private lateinit var app: Javalin
    private lateinit var driver: WebDriver
    private var vars: Map<String, Any>? = null
    var js: JavascriptExecutor? = null

    @Before
    fun setUp() {
        //Connect to the remote database
        DbConfig().getDbConnection()
        driver = ChromeDriver()
        js = driver as JavascriptExecutor
        vars = HashMap()
    }


    @After
    fun tearDown() {
        driver.quit()
    }


    @Test
    fun addUsers() {

        app = JavalinConfig().getJavalinService()
        JavalinTest.test(app) { _, client ->
            //driver.get("http://localhost:7001/")
            driver.get(client.origin)

        driver.findElement(By.linkText("More Details...")).click()
        driver.findElement(By.cssSelector(".fa-plus")).click()
        driver.findElement(By.name("name")).click()
        driver.findElement(By.name("name")).click()
        driver.findElement(By.name("name")).sendKeys("Parma kumar")
        driver.findElement(By.name("email")).click()
        driver.findElement(By.name("email")).sendKeys("parmak@gm.com")
        driver.findElement(By.cssSelector(".card-body > .btn")).click()
        driver.findElement(By.cssSelector(".list-group-item:nth-child(17) .fas")).click()
        Assert.assertThat(
            driver.switchTo().alert().getText(),
            CoreMatchers.`is`<String>("Are you sure you want to delete this user? This action cannot be undone.")
        )
        driver.switchTo().alert().accept()
    }

        wait = WebDriverWait(driver, java.time.Duration.ofSeconds(30))
    }



    @Test
    fun activityadd() {
        driver.get("http://localhost:7001/")
        driver.findElement(By.linkText("More Details...")).click()
        driver.findElement(By.linkText("Harry doe (harrydoe@abc.com)")).click()
        driver.findElement(By.linkText("User Activities")).click()
        driver.findElement(By.cssSelector(".fa")).click()
        driver.findElement(By.id("activity")).click()
        run {
            val dropdown: WebElement = driver.findElement(By.id("activity"))
            dropdown.findElement(By.xpath("//option[. = 'Walking']")).click()
        }
        driver.findElement(By.id("inputTime")).click()
        driver.findElement(By.id("inputTime")).sendKeys("30")
        driver.findElement(By.id("inputCal")).sendKeys("110")
        driver.findElement(By.cssSelector(".btn-primary")).click()
        driver.findElement(By.cssSelector("div:nth-child(2) > .card .fas")).click()
        Assert.assertThat(
            driver.switchTo().alert().getText(),
            CoreMatchers.`is`<String>("Are you sure you want to delete?")
        )
        driver.switchTo().alert().accept()
    }

    @Test
    fun bMI() {
        driver.get("http://localhost:7001/")
        driver.findElement(By.linkText("More Details...")).click()
        driver.findElement(By.linkText("Marge Siapson (marge@siapson.com)")).click()
        driver.findElement(By.linkText("User BMI")).click()
        driver.findElement(By.cssSelector(".fa")).click()
        driver.findElement(By.name("weight")).click()
        driver.findElement(By.name("weight")).sendKeys("73")
        driver.findElement(By.name("height")).sendKeys("165")
        driver.findElement(By.cssSelector(".fa-calculator")).click()
        driver.findElement(By.cssSelector("tr:nth-child(2) .fas")).click()
        Assert.assertThat(
            driver.switchTo().alert().getText(),
            CoreMatchers.`is`<String>("Are you sure you want to delete this BMI record?")
        )
        driver.switchTo().alert().accept()
    }

    @Test
    fun healthTip() {
        driver.get("http://localhost:7001/")
        driver.findElement(By.linkText("More Details...")).click()
        driver.findElement(By.linkText("Marge Siapson (marge@siapson.com)")).click()
        driver.findElement(By.linkText("User Health Tip")).click()
        driver.findElement(By.cssSelector(".btn")).click()
        driver.findElement(By.cssSelector(".btn")).click()
    }

    @Test
    fun sleepadd() {
        driver.get("http://localhost:7001/")
        driver.findElement(By.linkText("More Details...")).click()
        driver.findElement(By.linkText("Marge Siapson (marge@siapson.com)")).click()
        driver.findElement(By.linkText("User Sleep")).click()
        driver.findElement(By.cssSelector(".shadow-none")).click()
        driver.findElement(By.id("inpSleep")).click()
        driver.findElement(By.id("inpSleep")).sendKeys("8.2")
        driver.findElement(By.cssSelector("#myModal .btn-primary")).click()
        driver.findElement(By.cssSelector(".card-body:nth-child(4) .fas")).click()
        Assert.assertThat(
            driver.switchTo().alert().getText(),
            CoreMatchers.`is`<String>("Are you sure you want to delete?")
        )
        driver.switchTo().alert().accept()
    }

    @Test
    fun sleepupdate() {
        driver.get("http://localhost:7001/")
        driver.findElement(By.linkText("More Details...")).click()
        driver.findElement(By.linkText("Marge Siapson (marge@siapson.com)")).click()
        driver.findElement(By.linkText("User Sleep")).click()
        driver.findElement(By.cssSelector(".far")).click()
        driver.findElement(By.cssSelector("#updateModal #inpSleep")).click()
        driver.findElement(By.cssSelector("#updateModal #inpSleep")).sendKeys("8.6")
        driver.findElement(By.cssSelector("#updateModal .btn-primary")).click()
        driver.findElement(By.cssSelector(".far")).click()
        driver.findElement(By.cssSelector("#updateModal #inpSleep")).click()
        driver.findElement(By.cssSelector("#updateModal #inpSleep")).sendKeys("8.2")
        driver.findElement(By.cssSelector("#updateModal .btn-primary")).click()
    }

    @Test
    fun wateradd() {
        driver.get("http://localhost:7001/")
        driver.findElement(By.linkText("More Details...")).click()
        driver.findElement(By.linkText("Marge Siapson (marge@siapson.com)")).click()
        driver.findElement(By.linkText("User Water Intake")).click()
        driver.findElement(By.cssSelector(".shadow-none")).click()
        driver.findElement(By.id("inpWater")).click()
        driver.findElement(By.id("inpWater")).sendKeys("3.6")
        driver.findElement(By.cssSelector("#myModal .btn-primary")).click()
        driver.findElement(By.cssSelector(".card-body:nth-child(7) .fas")).click()
        Assert.assertThat(
            driver.switchTo().alert().getText(),
            CoreMatchers.`is`<String>("Are you sure you want to delete this record?")
        )
        driver.switchTo().alert().accept()
    }

    @Test
    fun waterupdate() {
        driver.get("http://localhost:7001/")
        driver.findElement(By.linkText("More Details...")).click()
        driver.findElement(By.linkText("Marge Siapson (marge@siapson.com)")).click()
        driver.findElement(By.linkText("User Water Intake")).click()
        driver.findElement(By.cssSelector(".card-body:nth-child(4) .far")).click()
        driver.findElement(By.cssSelector("#updateModal #inpWater")).click()
        driver.findElement(By.cssSelector("#updateModal #inpWater")).sendKeys("3.5")
        driver.findElement(By.cssSelector("#updateModal .btn-primary")).click()
        driver.findElement(By.cssSelector(".card-body:nth-child(7) .far")).click()
        driver.findElement(By.cssSelector("#updateModal #inpWater")).click()
        driver.findElement(By.cssSelector("#updateModal #inpWater")).sendKeys("3")
        driver.findElement(By.cssSelector("#updateModal .btn-primary")).click()
    }
    
}