# Change Log
Noteworthy new features and fixes.
This project adheres to [Semantic Versioning](http://semver.org/).

## Version [1.0.0] - 2019-02-24
- Initial release.
- Contentful's Java generator only allowed one field in the content model to be a primitive type or a list of other content models. Contentful allow a field to be a collection of *different* content model types but this is not supported by there Java code generator. The program solves that problem.
- I didn't want to hit the network for every build so I have added the ability to cache contentful responses. This allows developers to work seperately because they are no longer restrincted to the structure of a particular live environment.
- There is an acompanying maven plugin to generate the during a continueous environment.

