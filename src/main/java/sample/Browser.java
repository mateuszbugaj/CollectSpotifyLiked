package sample;


import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Browser {
    private ArrayList<String > consoleInfo = new ArrayList<>();
    private WebDriver driver;
    private FileManager fileManager;
    private TextArea console;
    private String userName;
    private String password;
    private Label progressInfo;

    public void updateConsoleLog(String newLine){
        consoleInfo.add(newLine);
        System.out.println(newLine);
        ArrayList<Object> reversedConsoleInfo = (ArrayList<Object>) consoleInfo.clone();
        Collections.reverse(reversedConsoleInfo);

        String consoleString = reversedConsoleInfo
                .stream()
                .map(line -> line + "\n")
                .collect(Collectors.joining());

        console.setText(consoleString);
    }

    public void setConsole(TextArea console) {
        this.console = console;
    }

    public void setLoginInfo(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public void setProgressInfo(Label progressInfo) {
        this.progressInfo = progressInfo;
    }

    public void runBrowser(){
        Thread logicThread = new Thread(() -> {
            try {
                collectData();
            } catch (UnableToLogInException | InterruptedException | UnableToCreateFileException e) {
                e.printStackTrace();
            }
        });
        logicThread.start();
    }

    private void collectData() throws UnableToLogInException, InterruptedException, UnableToCreateFileException {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--lang=en-ca","headless"); // to enable english version, to enable mode without window
        driver = new ChromeDriver(options);
        WebDriverWait driverWait = new WebDriverWait(driver, 6);
        updateConsoleLog("Browser driver initialized");

        driver.get("https://accounts.spotify.com/en/login/?_locale=en-US&continue=https:%2F%2Fwww.spotify.com%2Fus%2Faccount%2Foverview%2F"); // Spotify logging page
        updateConsoleLog("Website accessed");
        WebElement nameInput = driver.findElement(By.xpath("//input[@ng-model='form.username']"));
        WebElement passwordInput = driver.findElement(By.xpath("//input[@ng-model='form.password']"));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        driverWait.until(ExpectedConditions.elementToBeClickable(loginButton));

        nameInput.sendKeys(userName);
        passwordInput.sendKeys(password);
        loginButton.click();
        updateConsoleLog("Logging in");
        Thread.sleep(1000);

        try{
            driver.get("https://open.spotify.com/collection/tracks"); // Spotify page with liked songs
            driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//ol[@class='tracklist']")));
            WebElement tracklist = driver.findElement(By.xpath("//ol[@class='tracklist']"));
            driverWait.until(ExpectedConditions.elementToBeClickable(tracklist));
        } catch (org.openqa.selenium.TimeoutException e){
            updateConsoleLog("Unable to log in!");
            progressInfo.setVisible(false);
            driver.quit();
            throw  new UnableToLogInException();
        }

        updateConsoleLog("Logged in successfully");

        int numberOfSongs = Integer.parseInt(driver.findElement(By.xpath("//div[contains(@class, 'TrackListHeader__description-wrapper')]")).getText().replace(" SONGS",""));
        updateConsoleLog("You have " + numberOfSongs + ((numberOfSongs>1)?" songs":" song") +" liked");

        List<WebElement> songsWebElements = driver.findElements(By.xpath("//div[contains(@class, 'track-name-wrapper')]"));
        while (songsWebElements.size()<numberOfSongs){

            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", songsWebElements.get(songsWebElements.size()-1)); // scroll down until all songs are accessible
            Thread.sleep(500);

            songsWebElements = driver.findElements(By.xpath("//div[contains(@class, 'track-name-wrapper')]"));
        }


        updateConsoleLog("Found elements: " + songsWebElements.size());
        List<String > songNames = new ArrayList<>();

        for(WebElement element:songsWebElements){
            String text = element.getText();
            int dot = text.indexOf('•');
            text = text.substring(0,dot-1);
            text = text.replace("\nEXPLICIT", "");
            songNames.add(text);
            updateConsoleLog(text);
        }

        driver.quit();
        updateConsoleLog("Closing browser driver");

        try {
            fileManager = new FileManager();

        } catch (IOException e) {
            updateConsoleLog("Unable to create file!");
            progressInfo.setVisible(false);
            throw new UnableToCreateFileException();
        }

        songNames.forEach(entry -> fileManager.save(entry));

        fileManager.closeWriter();
        updateConsoleLog("Saved to file:\"Spotify liked\" ");
        updateConsoleLog("Done!");

    }
}


