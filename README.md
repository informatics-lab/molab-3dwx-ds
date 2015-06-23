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

further information on API structure can be seen in the 'api-structure.txt' file.

#### Images
POST images to {root uri}/media with the following fields:  
'forecast_reference_time' : date time of model run eg. '2015-01-01T00:00:00.000Z'  
'forecast_time'           : date time of forecast eg. '2015-01-01T00:00:00.000Z'  
'phenomenon'              : phenomenon that image represents eg. 'cloud_fraction_in_a_layer'  
'data'                    : image file  
'mime_type'               : mime type of file eg. 'image/png'  
'model'                   : name of model eg. 'uk_v'  
'resolution_x'            : resolution of the image (x direction)  
'resolution_y'            : resolution of the image (y direction)  
'data_dimension_x'        : number of data values represented (x direction)  
'data_dimension_y'        : number of data values represented (y direction)  
'data_dimension_z'        : number of data values represented (z direction)  
'geographic_region'       : a JSON array containing 4 geographic points. Each point must specify a 'lat' and 'lng' double property eg. '[{"lat":1.0,"lng":1.0}, ...]'  
'processing_profile'      : description of the processing method used to generate this data  

For sample representation see the json file attached.

#### Videos
POST videos to {root uri}/media with the following fields:  
'forecast_reference_time' : date time of model run eg. '2015-01-01T00:00:00.000Z'  
'phenomenon'              : phenomenon that video represents eg. 'cloud_fraction_in_a_layer'  
'data'                    : image file  
'mime_type'               : mime type of file eg. 'video/ogg'  
'model'                   : name of model eg. 'uk_v'  
'resolution_x'            : resolution of the video (x direction)  
'resolution_y'            : resolution of the video (y direction)  
'data_dimension_x'        : number of data values represented (x direction)  
'data_dimension_y'        : number of data values represented (y direction)  
'data_dimension_z'        : number of data values represented (z direction)  
'geographic_region'       : a JSON array containing 4 geographic points. Each point must specify a 'lat' and 'lng' double property eg. '[{"lat":1.0,"lng":1.0}, ...]'  
'processing_profile'      : description of the processing method used to generate this data  

For sample representation see the json file attached.

successful POST requests will receive a 201 (created) response with a pointer to the newly stored data's location. 


### Development
Want to contribute? Great!  
Take a fork and start hacking!

### Todo's
 - Write Tests

### Contact Us
For more information on the Informatics Lab take a look on our website...  
http://www.informaticslab.co.uk

License
----
MIT

**Free Software, Hell Yeah!**