#GoAber!

This is the README file for the GoAber site.

## Building and Running the project

### In .NET
To run the .NET project navigate to the ASP.NET folder and open the project using the .csproj file with visual studio. Before the project can be built and deployed the database must be generated. To do this open up the package manager console. First a new migration must be made using the add migration command.

Add-Migration Init -ProjectName GoAber

This will create a new migration file called "Init". Then the database needs to be updated. This can be performed by running the following command:

Update-Database -ProjectName GoAber

When this succeeds the project should be rebuilt from scratch and then deployed. By default the system will run on localhost.

### In JavaEE
To run the JavaEE project navigate to the JavaEE folder and open the project with Netbeans. In order to make sure the WSDL is available on the front end you must copy the 'WSDL' folder from:

GoAber\src\JavaEE\GoAber\GoAber-ejb\build\classes\META-INF

To:

GoAber\src\JavaEE\GoAber\GoAber-war\build\web\META-INF

You should then clean and build both the EJB and the WAR projects. Finally, right click on the WAR project and click deploy. By default the system will run on localhost.
