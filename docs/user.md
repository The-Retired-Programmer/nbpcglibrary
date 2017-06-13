[**NBPCG Library**](index.md)&nbsp;&nbsp;
[User Documentation](user.html)&nbsp;&nbsp;
[**Developer Documentation**](developer.html)&nbsp;&nbsp;
[**Release Notes**](release.html)

The library exists on Maven Central so no explicit repository information is required to be included 
in user's POM. Add any required dependencies into the POM.  An example statement is:

~~~ xml
    ...
    <dependency>
        <artifactId>xxxxx</artifactId>
        <groupId>uk.the retiredprogrammer.nbpcglibrary</groupId>
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
  * email
  * expressionparserandevaluate
  * lifecycle
  * simpleexpressionlanguage

Online Javadoc for this release can be found [here](http://www.javadoc.io/doc/uk.theretiredprogrammer/nbpcglibrary),
whilst a downloadable jar file containing the same Javadoc is available
[from Maven Central](http://central.maven.org/maven2/uk/theretiredprogrammer/nbpcglibrary/${project-version}/nbpcglibrary-${project-version}-javadoc.jar).

