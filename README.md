# Being
This is a half hardcoded ad-hoc local web app I made to speed up data collection for a research project.

Basically, you connect the app through the browser and scroll through YouTube videos. You can record data on the videos as you go.

**Instructions are as follows:**

* The id number file is pretty important if you want a unique id for each video. This gets incremented as you go. _Note the server does not save the updated id if you forcefully kill it._

* Run Main.java.

* Choose a directory, you must have the files listed like in the example directory provided. _Note that the links.txt file will be consumed. Links are taken out as you go._ (And there is no back button! So I highly recommend backing up links.)

* Tracker contains a mapping from id to video and data contains the descriptions.

**Dependencies:** NanoHttpd @ https://github.com/NanoHttpd/nanohttpd (I used v2.2.0)

**Oh yeah, this is single threaded as it was meant for a simple local app. Don't actually try to run this publicly anywhere. Even in a LAN is not recommended without some modifications**
