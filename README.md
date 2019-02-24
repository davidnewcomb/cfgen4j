# CFGen4J

Not added to Maven Central yet. (Coming soon!)

## Build

    1. git clone https://github.com/davidnewcomb/cfgen4j.git
    1. cd cfgen4j
    1. mvn clean install

## Run

    1. cd target
    1. java -jar cfgen4j-0.0.1-SNAPSHOT-jar-with-dependencies.jar -h

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

See [CHANGLOG.md] for history and more.

## License

Copyright (c) MIT Licence 2018
David Newcomb http://www.bigsoft.co.uk