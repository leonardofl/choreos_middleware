##########################################################################
#                                                                        #
#                IMPORTANT DEVELOPMENT NOTICE:                           #
#                                                                        #
# All ocurrences of 741a1885-f99a-4e67-b1ce-ca6a206705a3 must be replaced with the actual service name  #
#            before uploading the recipe to the chef-server              #
#                                                                        #
##########################################################################


# Name of the deployed service 
default['CHOReOSData']['serviceData']['741a1885-f99a-4e67-b1ce-ca6a206705a3']['WarFile'] = "airline.war"
default['CHOReOSData']['serviceData']['741a1885-f99a-4e67-b1ce-ca6a206705a3']['PackageURL'] = "http://valinhos.ime.usp.br:54080/enact_test/middleware/airline.war"

# Set the default log filewar_depl
default['CHOReOSData']['serviceData']['741a1885-f99a-4e67-b1ce-ca6a206705a3']['logFile'] = "/dev/stdout"

# Set the destination folder for JAR files
default['CHOReOSData']['serviceData']['741a1885-f99a-4e67-b1ce-ca6a206705a3']['InstallationDir'] = ENV["HOME"]

