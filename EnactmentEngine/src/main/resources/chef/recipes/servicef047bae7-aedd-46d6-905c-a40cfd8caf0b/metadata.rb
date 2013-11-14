maintainer       "CHOReOS"
maintainer_email "felipepg@ime.usp.br"
license          "bla"
description      "Deploys a WAR file into a cloud node"
long_description IO.read(File.join(File.dirname(__FILE__), 'README.md'))
version          "0.0.1"

depends "apt"
depends "tomcat"

attribute "service/f047bae7-aedd-46d6-905c-a40cfd8caf0b/WarFile",
  :display_NAME => "WAR file name to be deployed",
  :default => "test"

attribute "service/f047bae7-aedd-46d6-905c-a40cfd8caf0b/URL",
  :display_NAME => "Location (URL) of the WAR file to be deployed",
  :default => "test"


attribute "service/f047bae7-aedd-46d6-905c-a40cfd8caf0b/logfile",
  :display_NAME => "The default log file for errors",
  :default => "/dev/null"

