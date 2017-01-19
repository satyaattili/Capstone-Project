# Capstone-Project
 Udacity Nanodegree final project
  
 This is News Application that provides headlines, top news from top 500 sources.
 
 This application using https://newsapi.org/ API to get the news from top 500 sources. Needed API key to get news articles.
 API key can be generated from the website https://newsapi.org/
 
 App contains two versions
 1. free (contains ads)
 2. premium (ad-free)
 
 Gradle Tasks :
 1. installFreeRelease
 2. installPremiumRelease
 

 Firebase & Google Playservices:
 Remote Config : Configured for login needed or not, currently login feature enabled using Remote Config.
 App Indexing : News Articles will be indexed in app google search (Indexed visited articles only)
 Dynamic Links : To share news articles, User will navigate to Playstore if app not istalled.
 Notifications : App will recieve notifications for top news
 Ad-Mob : Banner ad will apppear in News detail screen
 
 Content Provider and Loader :
 NewsProvider extends ContenProvider and helps to save notifications and used Cursorloader to get notifications from Db.
 
 MVP Pattren :
 SourcesActivity and Articles Activity contains only UI related logic and business logic will be in their respective Presenters.
 
 
 
