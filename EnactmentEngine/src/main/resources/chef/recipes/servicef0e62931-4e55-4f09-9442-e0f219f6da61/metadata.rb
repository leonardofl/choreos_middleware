maintainer       "CHOReOS"
maintainer_email "felipepg@ime.usp.br"
license          "bla"
description      "Deploys a WAR file into a cloud node"
long_description IO.read(File.join(File.dirname(__FILE__), 'README.md'))
version          "0.0.1"

depends "apt"
depends "tomcat"

attribute "service/f0e62931-4e55-4f09-9442-e0f219f6da61/WarFile",
  :display_NAME => "WAR file name to be deployed",
  :default => "test"

attribute "service/f0e62931-4e55-4f09-9442-e0f219f6da61/URL",
  :display_NAME => "Location (URL) of the WAR file to be deployed",
  :default => "test"


attribute "service/f0e62931-4e55-4f09-9442-e0f219f6da61/logfile",
  :display_NAME => "The default log file for errors",
  :default => "/dev/null"

