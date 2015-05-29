# molab-3dwx-ds
## Met Office informatics lab 3D weather data service
The data service for our 3D weather visualisation client

### Version
1.0.0

### Configuration
If you wish to build and run the application you will need to create your own 'application.yml' file.
Provided is a standard logback configuration (src/config/logback.xml) file which you can point your properties file at.
Once created your 'application.yml' file should be placed into the 'src/config' dir to be picked up by the maven build.

I have included the development 'application.yml' file for local testing and dev work.
You can use this file as a template when creating your 'application.yml' file.
When running locally from within an IDE the default spring boot logging config will be used.

### Installation
You will need java & maven installed.

```sh
$ git clone [git-repo-url] molab-3dwx-ds  
$ cd molab-3dwx-ds  
$ mvn clean install
$ cd target
$ unzip molab-3dwx-ds...distribution.zip -d molab-3dwx-ds
$ cd molab-3dwx-ds
```

### Logging
Once built and installed application will log to to {application root dir}/log 

### Use
Ensure you have an instance of mongod running with the db 'molab' with an 'images' & 'videos' collection.
```sh
$ molab-3dwx-ds.sh start 
```

### Endpoints
Everything should be discoverable from the root uri at:
http://<YOUR BOX NAME>:<YOUR PORT>/molab-3dwx-ds/

#### Images
POST images to {root uri}/images with the following fields:  
'model_run_dt' : date time of model run eg. '2015-01-01T00:00:00.000Z'  
'forecast_dt'  : date time of forecast eg. '2015-01-01T00:00:00.000Z'  
'phenomenon'   : phenomenon that image represents eg. 'cloud_fraction'  
'data'         : image file  

For sample representation see the json file attached.

#### Videos
POST videos to {root uri}/videos with the following fields:  
'model_run_dt' : date time of model run eg. '2015-01-01T00:00:00.000Z'  
'phenomenon'   : phenomenon that image represents eg. 'cloud_fraction'  
'data'         : video file  

For sample representation see the json file attached.

successful POST requests will receive a 201 (created) response with a pointer to the newly stored data's location. 


### Development
Want to contribute? Great!  
Take a fork and start hacking!

### Todo's
 - Write Tests
 - Restructure API 

### Contact Us
For more information on the Informatics Lab take a look on our website...  
http://www.informaticslab.co.uk

License
----
MIT

**Free Software, Hell Yeah!**