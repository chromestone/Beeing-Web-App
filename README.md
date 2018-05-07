This is a half hardcoded quick local web app I made to speed up data collection for a research project.

Basically, you connect the app through the browser and scroll through YouTube videos. You can record data on the videos as you go.

Instructions are as follows. The id number file is pretty important if you want a unique id for each video. This gets incremented as you go. Note the server does not save the updated id if you forcefully kill it. Run Main.java. Choose a directory, you must have the files listed in the example directory already created. Note that the links.txt file will be consumed. Links are taken out as you go. (And there is no back button lol, highly recommend backup links). Tracker contains a mapping from id to video and data contains the descriptions.

**Dependencies:** nanohttpd @ https://github.com/NanoHttpd/nanohttpd

**Oh yeah, this is single threaded as it was meant for a simple local app. Don't actually try to run this publicly anywhere. Even in a LAN is not recommended without some modifications**