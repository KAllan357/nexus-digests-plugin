# When a SHA1 isn't enough.

A Nexus Plugin that records more types of digests for artifacts that are uploaded to the server.

## Requirements

* Maven
* Java 6
* Sonatype Public Grid to resolve artifacts - [link](http://repository.sonatype.org/content/groups/sonatype-public-grid/).

## Installation

Execute a `mvn clean install` on the repository to build the plugin. Unzip the built "bundle.zip" (ex: nexus-digests-plugin-1.0.0-SNAPSHOT-bundle.zip) and copy the output folder to your Nexus installation at `$nexus_installation_path/nexus/WEB-INF/plugin-repository/`. Restart your Nexus server.

## Usage

Execute a REST GET to your Nexus at `/service/local/artifact/digests` with params:

* g - the group id
* a - the artifact id
* v - the version
* e - the extension
* r - the repository
 
Example: `http://localhost:8081/nexus/service/local/artifact/digests?g=com.kyle&a=foo&v=1.0.0&e=jar&r=releases`

Example Output:

```
{
  "SHA1":"1cf53aab6cc10940ed07a0378346f65c4d4c79d4",
  "SHA256":"d1bbadf9f3965ec1aaaa5482d6e0a6313a488e3189d97c8bf5be8c097960814a"
}
```

## License and Author

Author:: Kyle Allan (KAllan357@gmail.com)
