This document refers to release v${project.version}

To access the repository the following statement can be added to your POM:

    <repository>
        <id>rlinsdale</id>
        <name>Richard-Linsdale</name>
        <url>http://repository.rlinsdale.org.uk</url>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>

and then add any required dependencies into the POM.  An example statement is:

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>uk.org.rlinsdale</groupId>
                <artifactId>nbpcglibrary</artifactId>
                <version>${project.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    ...
    <dependency>
        <artifactId>xxxxx</artifactId>
        <groupId>uk.org.rlinsdale.nbpcglibrary</groupId>
    </dependency>
    ...

    where xxxxx is one of:
    common, data, form, icons, mysql, node or topcomponent

**Warning:** This repository is not currently indexed,
so search capabilities from
your IDE are extremely limited.  It is easier to manually add
dependencies directly into your POM.

Online Javadoc for this release can be found
[here](http://javadoc.rlinsdale.org.uk/nbpcglibrary/v${release}/index.html),
whilst a downloadable jar file containing the same Javadoc is available from
[here](http://repository.rlinsdale.org.uk/uk/org/rlinsdale/nbpcglibrary/${project.version}/nbpcglibrary-${project.version}-javadoc.jar)


