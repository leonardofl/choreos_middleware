##########################################################################
#                                                                        #
#                IMPORTANT DEVELOPMENT NOTICE:                           #
#                                                                        #
# All ocurrences of 769a18ce-e458-4909-b5f8-2feb80731e67 must be replaced with the actual service name  #
#            before uploading the recipe to the chef-server              #
#                                                                        #
##########################################################################


# Name of the deployed service 
default['CHOReOSData']['serviceData']['769a18ce-e458-4909-b5f8-2feb80731e67']['WarFile'] = "travelagency.war"
default['CHOReOSData']['serviceData']['769a18ce-e458-4909-b5f8-2feb80731e67']['PackageURL'] = "http://valinhos.ime.usp.br:54080/enact_test/v2-2/travelagency.war"

# Set the default log filewar_depl
default['CHOReOSData']['serviceData']['769a18ce-e458-4909-b5f8-2feb80731e67']['logFile'] = "/dev/stdout"

# Set the destination folder for JAR files
default['CHOReOSData']['serviceData']['769a18ce-e458-4909-b5f8-2feb80731e67']['InstallationDir'] = ENV["HOME"]

