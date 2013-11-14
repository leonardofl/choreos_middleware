##########################################################################
#                                                                        #
#                IMPORTANT DEVELOPMENT NOTICE:                           #
#                                                                        #
# All ocurrences of 27e72255-678f-46a5-a975-a541f15a2d07 must be replaced with the actual service name  #
#            before uploading the recipe to the chef-server              #
#                                                                        #
##########################################################################


# Name of the deployed service 
default['CHOReOSData']['serviceData']['27e72255-678f-46a5-a975-a541f15a2d07']['WarFile'] = "travelagency.war"
default['CHOReOSData']['serviceData']['27e72255-678f-46a5-a975-a541f15a2d07']['PackageURL'] = "http://valinhos.ime.usp.br:54080/enact_test/middleware/travelagency.war"

# Set the default log filewar_depl
default['CHOReOSData']['serviceData']['27e72255-678f-46a5-a975-a541f15a2d07']['logFile'] = "/dev/stdout"

# Set the destination folder for JAR files
default['CHOReOSData']['serviceData']['27e72255-678f-46a5-a975-a541f15a2d07']['InstallationDir'] = ENV["HOME"]

