#!/bin/bash

javac -cp ".:mysql-connector-j-8.0.xx.jar" *.java
java -cp ".:mysql-connector-j-8.0.xx.jar" Main
