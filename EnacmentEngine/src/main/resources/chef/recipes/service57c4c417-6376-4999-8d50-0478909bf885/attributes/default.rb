##########################################################################
#                                                                        #
#                IMPORTANT DEVELOPMENT NOTICE:                           #
#                                                                        #
# All ocurrences of 57c4c417-6376-4999-8d50-0478909bf885 must be replaced with the actual service name  #
#            before uploading the recipe to the chef-server              #
#                                                                        #
##########################################################################


# Name of the deployed service 
default['CHOReOSData']['serviceData']['57c4c417-6376-4999-8d50-0478909bf885']['WarFile'] = "airline.war"
default['CHOReOSData']['serviceData']['57c4c417-6376-4999-8d50-0478909bf885']['PackageURL'] = "http://valinhos.ime.usp.br:54080/enact_test/v2-2/airline.war"

# Set the default log filewar_depl
default['CHOReOSData']['serviceData']['57c4c417-6376-4999-8d50-0478909bf885']['logFile'] = "/dev/stdout"

# Set the destination folder for JAR files
default['CHOReOSData']['serviceData']['57c4c417-6376-4999-8d50-0478909bf885']['InstallationDir'] = ENV["HOME"]

