To access the repository the following statement can be added to your Settings:

~~~ xml
    <repository>
        <id>rlinsdale</id>
        <name>Richard-Linsdale</name>
        <url>http://www.rlinsdale.org.uk/repository</url>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
~~~

and then add any required dependencies into the POM.  An example statement is:

~~~ xml
    ...
    <dependency>
        <artifactId>xxxxx</artifactId>
        <groupId>uk.org.rlinsdale.nbpcglibrary</groupId>
        <version>${project-version}</version>
    </dependency>
    ...
~~~

where xxxxx is one of:

  * api
  * annotations
  * common
  * data
  * form
  * icons
  * json
  * node
  * topcomponent
  * mysql
  * localjsonaccess
  * localdatabaseaccess
  * remoteclient

**Warning:** This repository is not currently indexed,
so search capabilities from
your IDE are extremely limited.  It is easier to manually add
dependencies directly into your POM.

Online Javadoc for this release can be found
[here](javadoc/index.html),
whilst a downloadable jar file containing the same Javadoc is available from
[here](http://www.rlinsdale.org.uk/repository/uk/org/rlinsdale/nbpcglibrary/${project-version}/nbpcglibrary-${project-version}-javadoc.jar)


