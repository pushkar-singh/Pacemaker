# Pacemaker
------------------------------------------------------------------------------------------------------------------------


***************  TO BUILD & RUN THE PROJECT ****************


Execute the below shell command that runs the script (runScript.sh).
>>  sh runScript.sh

This script will run mvn commands to COMPILE, TEST and EXECUTE the project.

>> Note:::
>> For activities, Distance and Duration should be integer (no decimal)
and
>> Time format should be like "2015-08-04T10:11:30" (It's implemented like this to make sorting work properly)

------------------------------------------------------------------------------------------------------------------------



******************** BUILD SYSTEM ********************


Maven has been used to resolve dependencies, package the project into jar and run the jar.

The project is divided into four maven modules:

 -pacemaker-console - contains the business logic of the project.
 
 -pacemaker-serializer - contains serializers for different file formats.
 
 -pacemaker-models - contains all the common models and beans.
 
 -pacemaker-coverage - responsible for generating one aggregate test coverage report for all the modules.
 
Module specific dependencies are kept int the respective pom.xml of the modules. While the common dependencies used
across all the four modules are included in parent pom.xml present at the root directory of the project.

"pacemaker-console" is the main executable which should be run.
Also this is module is using other modules as maven dependency.

Below is shown the dependency hierarchy between the modules

                        pacemaker(root)
                        /      |      \
                       /       |       \
                      /        |        \
                     /         |         \
                pacemaker  pacemaker  pacemaker
              -serializer   -console  -coverage
                  |          /     \
                  |         /       \
                  |        /         \
                  |       /           \
           pacemaker  pacemaker    pacemaker
             -models    -models  -serializer  


------------------------------------------------------------------------------------------------------------------------



********************* PERSISTENCE APPROACH *********************


Data is stored in flat files. The supported file formats are JSON, XML and YAML. Default file format is YAML.
For YAML, "yamlbeans" maven dependency has been added in the pom.xml of pacemaker-serializer module.

The datasource file will be saved in "pacemaker-console" module.

If a user changes a file format using cff command, then there will be a new file created and saved automatically
at the time of exit and old file will be deleted.

>> Note: no need to run "load" or "store" command separately after cff. 
      Just a single "cff" command will do all the data persistence. 


------------------------------------------------------------------------------------------------------------------------



**************  PRESENTATION APPROACH: *******************


Formatting and coloring have been done for table and texts using ASCII values.
For enhanced view, bar chart is added which represents different types of activities.
This will show up with below command
>> la <user-id>


------------------------------------------------------------------------------------------------------------------------



****** TESTING APPROACH **********


JACOCO plugin has been used for coverage.

For UI tests 'newusers.script', 'newactivities.script', 'newroutes.script' and 'activitiessorting.script' have been created.
We need to change UUID in each file as per the generated UUID except for 'newusers.script' file.

Each module has it's own test cases majorly covering PacemakerAPI, Serializers and Models.

There's a separate maven module (pacemaker-coverage) created for generating test coverage report.
Also, have tried to test Main class using "mockito" to mock dependency on other classes.

>> Note:: Consolidated test coverage report will be created at the location
  "pacemaker-coverage/target/coverage-report/index.html".


------------------------------------------------------------------------------------------------------------------------



 
 