# CovidInfo
This project is build around MVVM design pattern with dependency injection using koin and android Room for storing data in Kotlin.
The project consist of 3 section:-
- Listing section
- Search section
- Details section

#### Listing section
Basically a landing page for the app that shows list of countries. The countries list, on first install it will get data from the provided api 
https://covid-api.mmediagroup.fr/v1/cases .
Then the data will be saved into room database, to be used as an offline data source through out the app cycle and next usage.
The data can be refresh by swipe down the page and it will start the cycle again by getting the data from api and stored it.
From this section, user able to sort the data as well such as:-

1. Number of active cases
2. Number of Deaths
3. Active cases for 100k hab
4. Deaths for 100k hab

#### Search section
By using the locally stored data, user able to do searching by country name. The live search will start to execute when search keywords is 2 characters in length.

#### Details section
From either Listing or Search section, user able to navigate to this section by clicking the country name.
The section will display 2 historical charts for both active and deaths cases also the following details for selected country.

1. Number of active cases
2. Number of Deaths
3. Date of last update
4. Active cases for 100k hab
5. Deaths for 100k hab

#### Cases per 100k calculation formula
(number of cases / population) x 100000

### Requirement 1
This application should be built without using a 3rd party library for network connection. Please refer to this class
https://github.com/Jengking/CovidInfo/blob/main/app/src/main/java/my/com/covid/info/utils/ApiRepository.kt

### Requirement 2
You should use SOLID principles, and at least 20% of unit testing code coverage. Please refer to this class
https://github.com/Jengking/CovidInfo/blob/main/app/src/androidTest/java/my/com/covid/info/dbs/AppDatabaseTest.kt

## Q & A
1. Describe all the decisions that you took during development and the reasoning behind them?
- Since requirement 1, to call api without using 3rd party library. I was left with 2 choices, either using Asynctask or Coroutines.
I choose to use coroutines for this project, for scalability and future proof as Asynctask was deprecated starting from Android 11.

- Due to limitation on api calls and search function implementation, I decided to use android Room as a local storage data provider
and Moshi as Json Serialization to easily convert between data to object and vice versa from api calls response and Room storing/retrieve data process.

- As the api response does not include data - cases per 100k, I ask for clarification and lookup for the correct calculation formula.

2. If you had more time, what other features would you add to your app and how would you build
them?
- I will look for a better graphs library as the historical charts don't work properly in tab fragments. Only the 2nd fragment data is loaded while the 1st data is available but is not loading.
The issue is mention in the links and not solution have been found yet. https://github.com/AnyChart/AnyChart-Android/issues/77 and https://github.com/AnyChart/AnyChart-Android/issues/102

- Add better error notification either in form of dialog box or snackbar.

# Thank you



