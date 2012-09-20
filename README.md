hadoop_training
===============

This is my own hadoop tests.

First Test with word count done.

jar包
mkdir FirstJar
javac -classpath ~/hadoop/hadoop-0.20.2-core.jar -d FirstJar WordCount.java
jar -cvf wordcount.jar -C FirstJar/ .
