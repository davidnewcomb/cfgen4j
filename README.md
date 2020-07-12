# Contentful Model Genenerator for Java

Java code generator for [Contentful](https://www.contentful.com/) that can cope with unions of types.
Replaces [Generator](https://github.com/contentful/generator.java) written by Contentful because it didn't work!

## Build

    - git clone https://github.com/davidnewcomb/cfgen4j.git
    - cd cfgen4j
    - mvn clean install

## Run

    - cd target
    - java -jar cfgen4j-0.0.1-SNAPSHOT-jar-with-dependencies.jar -h

```
usage: cfgen4j [-h] -s [SPACEID] -p [PACKAGE] -f [FOLDER] -t [TOKEN] [-ec [EP_CORE]] [-eu [EP_UPLOAD]]
               [-cf [CACHE_FILE]] [-v]

Inspects Contentful schema and generates Java classes ${version}

named arguments:
  -h, --help             show this help message and exit
  -s [SPACEID], --spaceid [SPACEID]
                         Contentful space id
  -p [PACKAGE], --package [PACKAGE]
                         Java package name
  -f [FOLDER], --folder [FOLDER]
                         Path to the package root
  -t [TOKEN], --token [TOKEN]
                         Contentful app access token
  -ec [EP_CORE], --ep-core [EP_CORE]
                         Change core endpoint
  -eu [EP_UPLOAD], --ep-upload [EP_UPLOAD]
                         Change upload endpoint
  -cf [CACHE_FILE], --cache-file [CACHE_FILE]
                         File to cache contentful net request
  -v, --version          Version
```

## More

See CHANGLOG.md for history and more.

## License

Copyright (c) MIT Licence 2018
David Newcomb http://www.bigsoft.co.uk
