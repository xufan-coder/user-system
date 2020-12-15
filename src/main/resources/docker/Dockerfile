FROM centos-jdk8:3.0
MAINTAINER zerody
ADD @project.artifactId@-@project.version@.jar @project.artifactId@-@project.version@.jar
ENTRYPOINT ["java","-jar","-Duser.timezone=GMT+8","-Dfile.encoding=UTF-8","-Xmx@java.memory.limit@","/@project.artifactId@-@project.version@.jar"]

