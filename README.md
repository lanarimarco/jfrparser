# jfrparser

## Abstract
Allows to create a tsv file from jfr file by including only packages prefixed by `com.smeup` and `it.smea`

## Requirement
java11 both to build project and to execute the main class

## Usage
```
git clone https://github.com/lanarimarco/jfrparser.git
cd jfrparser
mvn clean package
java -jar target/jfrparser-jar-with-dependencies.jar path_to_file.jfr
```
