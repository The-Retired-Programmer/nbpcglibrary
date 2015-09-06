{::comment define doc.title=NBPCG Library User Documentation /}
{::comment define doc.header= User Documentation /}
{::comment define doc.name = NBPCG Library /}
{::comment define doc.menu.menu = [NBPCG Library Home](index.html) /}

This document refers to release v${project.version}

To access the repository the following statement can be added to your Settings:

    <repository>
        <id>rlinsdale</id>
        <name>Richard-Linsdale</name>
        <url>http://repository.rlinsdale.org.uk</url>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>

and then add any required dependencies into the POM.  An example statement is:

    ...
    <dependency>
        <artifactId>xxxxx</artifactId>
        <groupId>uk.org.rlinsdale.nbpcglibrary</groupId>
        <version>${project.version}</version>
    </dependency>
    ...

    where xxxxx is one of:
    api, annotations, common, data, form, icons, json, node, topcomponent
    mysql, localjsonaccess, localdatabaseaccess, remoteclient or remotedb

**Warning:** This repository is not currently indexed,
so search capabilities from
your IDE are extremely limited.  It is easier to manually add
dependencies directly into your POM.

Online Javadoc for this release can be found
[here](http://javadoc.rlinsdale.org.uk/nbpcglibrary/v${release}/index.html),
whilst a downloadable jar file containing the same Javadoc is available from
[here](http://repository.rlinsdale.org.uk/uk/org/rlinsdale/nbpcglibrary/${project.version}/nbpcglibrary-${project.version}-javadoc.jar)


