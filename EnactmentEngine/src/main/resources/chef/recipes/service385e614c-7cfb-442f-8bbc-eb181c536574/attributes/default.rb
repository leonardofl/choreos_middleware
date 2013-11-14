##########################################################################
#                                                                        #
#                IMPORTANT DEVELOPMENT NOTICE:                           #
#                                                                        #
# All ocurrences of 385e614c-7cfb-442f-8bbc-eb181c536574 must be replaced with the actual service name  #
#            before uploading the recipe to the chef-server              #
#                                                                        #
##########################################################################


# Name of the deployed service 
default['CHOReOSData']['serviceData']['385e614c-7cfb-442f-8bbc-eb181c536574']['WarFile'] = "travel-agency-service.jar"
default['CHOReOSData']['serviceData']['385e614c-7cfb-442f-8bbc-eb181c536574']['PackageURL'] = "http://valinhos.ime.usp.br:54080/enact_test/v2-2/travel-agency-service.jar"

# Set the default log filewar_depl
default['CHOReOSData']['serviceData']['385e614c-7cfb-442f-8bbc-eb181c536574']['logFile'] = "/dev/stdout"

# Set the destination folder for JAR files
default['CHOReOSData']['serviceData']['385e614c-7cfb-442f-8bbc-eb181c536574']['InstallationDir'] = ENV["HOME"]

