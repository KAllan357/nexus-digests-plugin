# When a SHA1 isn't enough.

A Nexus Plugin that records more types of digests for artifacts that are uploaded to the server.

## Requirements

* Maven
* Java 6
* Sonatype Public Grid to resolve artifacts - [link](http://repository.sonatype.org/content/groups/sonatype-public-grid/).

## Installation

Execute a `mvn clean install` on the repository to build the plugin. Unzip the built "bundle.zip" (ex: nexus-digests-plugin-1.0.0-SNAPSHOT-bundle.zip) and copy the output folder to your Nexus installation at `$nexus_installation_path/nexus/WEB-INF/plugin-repository/`. Restart your Nexus server.

## License and Author

Author:: Kyle Allan (KAllan357@gmail.com)
