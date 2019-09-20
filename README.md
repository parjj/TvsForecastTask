# Top View Sightseeing Mobile Application Coding Test

## Table Of Contents
*  Title
*  General Features
*  Frameworks and APIs
*  Task Time

### Title - TVSForecast App
The TVSForecast app displays the weather details depending on the user's location. 

### General Features
* Dark sky API is used to fetch weather details on providing the user location details. 
* The Data to be displayed are fetched from the url using retrofit. 
* The datas are stored in the local database - ROOM . 

#### The files contained are :

1. *MainActivity - The UI of the mobile app. The user weather details are defined here by providing the current location of the user by
                   LocationManager and retrieving it by using Dark Sky API,retrofit and storing it in the local room database.
2. *IconViewClass -This class carries the properties of the weather icons. Used Skycons which was specifically built for the Dark Sky API.
3. *RetrofitData -  This folder contains the model entity class, API (*GetDataService) for the network interaction. 
                    Retrieving the required data's to be shown on the UI currently,daily data and excluding the other say hourly,minutely 
4. *RoomData - This folder contains the Room database files and a live data observer is provided to fetch all the data's.
5. *ViewModel - This folder holds the data for the livedata check. 
6. *res/ - This resource folder provides the components to be used by the application.
    **  /drawable - contains the images and border definitions to be added to the screen
    **  / layout folder : - daily_forecast -  layouts for the recycler view.
                             

### Frameworks and APIs 
* Dark Sky API
* Skycon associated with Dark Sky API
* Retrofit
* AsyncTask
* Room Database
* MVVM



### Task Time

Time spent on this project
* Tuesday - 7 hours
* Wednesday - 6 hours
* Thursday - 5 hours

** A total of 18 hours . 