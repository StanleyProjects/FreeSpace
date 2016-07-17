# FreeSpace
FreeSpace game on JavaFX

    applicationId "stan.free.space"
    versionCode 1607180138
    versionName "0.08"

# Windows build information
## build Java8
> "%JAVA_HOME%/bin/javac" -sourcepath ./src/main/java -d bin -classpath lib/* ./src/main/java/stan/free/space/Main.java

## build_css
> "%JAVA_HOME%/bin/javapackager" -createbss -srcdir ./src/main/css -outdir bin/css -srcfiles theme.css -v

## run Java8
> "%JAVA_HOME%/bin/java" -Dprism.order=sw -classpath lib/*;bin stan.free.space.Main
