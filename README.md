<p align="center">
  <img src="./app/src/main/res/drawable/header.png" alt="Sublime's custom image"/>
</p>

***
## Project Overview - CS205 Operating System Concepts with Android

The PortfolioTracker app is a simple single screen Android application informing the user of the health of various stocks.

The user can input up to five stock tickers, and the relevant data between *July 1 2021, 00:00:01 to Dec 31 2021, 23:59:59* is gathered from [Finnhub](https://finnhub.io).

The recommended configuration of the emulator are as follows:
- Device: Nexus 6P
- API: Level 30
- Resolution: 1440 x 2560 (560 dpi)
- Multi-Core CPU 4
- RAM: 1536 MB
- SD card: 512 MB

## Functionalities

Upon running the application in Android Studio, the main screen displays 5 text boxes on the left to input stock tickers, and buttons on the right of each row indicating `Download` and `Calculate` respectively. 

Upon inputting valid stock tickers and clicking the `Download` and `Calculate` buttons in order, the application displays the annualised return and volatility of the respective stocks within the preset period.

Other than the above basic functionalities, the following are the extra quality of life features: 

- **Input Validation:** 

When an input ticker is invalid or empty, an error popup shows up on the corresponding text box.

- **Flexibility and Efficiency of Use:**

If the user inputs more than one ticker, the `DOWNLOAD ALL` button can be used to synchronously download data for each of the tickers. Clicking on `CALCULATE ALL` will then calculate and display the metrics for each ticker currently on the screen.

- **Data Persistence:**

For power and computation efficiency, data for previously fetched tickers will not be redownloaded.

- **Error Prevention:**

The `Calculate` button will be inactive until the data for the ticker has been downloaded and persisted into the database.

## Design Choices

In the main activity, we initialise all the `Views` and register all relevant `BroadcastReceivers`. A single array is used to hold the list of stocks chosen.
***
We utilise the following three `BroadcastReceivers`:

- **MyBroadcastReceiver**

The main `BroadcastReceiver` for handling the `Calculation` intent and initialising a `Cursor` to query the data for the ticker, followed by the calculation.

- **ErrorBroadcastReceiver**

As mentioned earlier, the app has the functionality to display an error when the ticker is invalid (i.e GOOGLE instead of GOOGL). This `BroadcastReceiver` enables the display of those errors.

- **DownloadBroadcastReceiver** 

This `BroadcastReceiver` enables the `Calculate` button once the intent is received after the completion of a download for the ticker.
***

The `HistoricalDataProvider` contains the methods that allow persistence and querying functionality, acting as the Data Layer of the app.

Finally, the `FinnhubService` does most of the heavy lifting, where it fetches data from the API for the tickers, in case they don't already exist in the database. It also sends broadcasts various intents that we are able to see the result of on the screen.
