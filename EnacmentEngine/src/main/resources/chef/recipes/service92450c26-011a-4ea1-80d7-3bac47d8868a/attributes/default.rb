##########################################################################
#                                                                        #
#                IMPORTANT DEVELOPMENT NOTICE:                           #
#                                                                        #
# All ocurrences of 92450c26-011a-4ea1-80d7-3bac47d8868a must be replaced with the actual service name  #
#            before uploading the recipe to the chef-server              #
#                                                                        #
##########################################################################


# Name of the deployed service 
default['CHOReOSData']['serviceData']['92450c26-011a-4ea1-80d7-3bac47d8868a']['WarFile'] = ""
default['CHOReOSData']['serviceData']['92450c26-011a-4ea1-80d7-3bac47d8868a']['PackageURL'] = "http://valinhos.ime.usp.br:54080/enact_test/v2-2/airline-service.jar"

# Set the default log filewar_depl
default['CHOReOSData']['serviceData']['92450c26-011a-4ea1-80d7-3bac47d8868a']['logFile'] = "/dev/stdout"

# Set the destination folder for JAR files
default['CHOReOSData']['serviceData']['92450c26-011a-4ea1-80d7-3bac47d8868a']['InstallationDir'] = ENV["HOME"]

