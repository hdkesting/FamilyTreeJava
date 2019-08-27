# FamilyTreeJava
Show a family tree / learning project. The data is read from a [GEDCOM](https://en.wikipedia.org/wiki/GEDCOM) file.

# Initial setup
* The application expects a MySql database, as specified in the application.properties file (spring.datasource.*)
  * The database ("familytree") must contain (empty) tables, see the "sql" folder for create-scripts.
* In the application.properties, switch the commenting on the "gedcom.source" lines, so that "sampleFamily.ged" is the selected one. The other is not included in git.
* Start the application and go to /localhost:8081/admin/init/load to immediately load the configured GEDCOM file. This command does not give much feedback as yet.
  * /admin/init/clear will remove all data

# Limitations
* The application does not support marriages that do not consist of 1 male, 1 female (although either may not be known) and their possible children.
