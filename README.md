# FamilyTreeJava
Show a family tree / learning project. The data is read from a [GEDCOM](https://en.wikipedia.org/wiki/GEDCOM) file.

# Initial setup
* The application expects a MySql database, as specified in the application.properties file (spring.datasource.*)
  * The database ("familytree") must contain (empty) tables, see the "sql" folder for create-scripts.
  * Execute the scripts in order, note that the scripts are not idempotent.
* In the application.properties, possibly switch the commenting on the "gedcom.source" lines, so that "sampleFamily.ged" is the selected one. The other is not included in git.
* Start the application and go to /localhost:8081/admin/init to load the configured GEDCOM file.
  * The "load" command does not give much feedback as yet.
  * There is also a "clear" command.
  * Note that you will need to log on.
