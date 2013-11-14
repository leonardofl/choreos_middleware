maintainer       "CHOReOS"
maintainer_email "felipepg@ime.usp.br"
license          "bla"
description      "Deploys a WAR file into a cloud node"
long_description IO.read(File.join(File.dirname(__FILE__), 'README.md'))
version          "0.0.1"

depends "apt"
depends "tomcat"

attribute "service/b394f47e-dce4-4a76-a78e-91a5830d288d/WarFile",
  :display_NAME => "WAR file name to be deployed",
  :default => "test"

attribute "service/b394f47e-dce4-4a76-a78e-91a5830d288d/URL",
  :display_NAME => "Location (URL) of the WAR file to be deployed",
  :default => "test"


attribute "service/b394f47e-dce4-4a76-a78e-91a5830d288d/logfile",
  :display_NAME => "The default log file for errors",
  :default => "/dev/null"

