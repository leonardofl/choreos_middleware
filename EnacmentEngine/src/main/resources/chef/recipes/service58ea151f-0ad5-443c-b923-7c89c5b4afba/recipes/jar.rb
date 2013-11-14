#
# Cookbook Name:: generic-jar-service-template
# Recipe:: default
#
# Copyright 2012, YOUR_COMPANY_NAME
#
# All rights reserved - Do Not Redistribute
#

##########################################################################
#                  #
#                IMPORTANT DEVELOPMENT NOTICE:                           #
#                  #
# All ocurrences of $ NAME must be replaced with the actual service name #
#            before uploading the recipe to the chef-server              #
#                  #
##########################################################################

include_recipe "apt" # java recipe is failing without recipe apt
include_recipe "java"


service "service_58ea151f-0ad5-443c-b923-7c89c5b4afba_jar" do
	# Dirty trick to save the java pid to a file
 	start_command "start-stop-daemon -b --start --quiet --oknodo --pidfile /var/run/service-58ea151f-0ad5-443c-b923-7c89c5b4afba.pid --exec /bin/bash -- -c \"echo \\$\\$ > /var/run/service-58ea151f-0ad5-443c-b923-7c89c5b4afba.pid ; exec java -jar #{node['CHOReOSData']['serviceData']['58ea151f-0ad5-443c-b923-7c89c5b4afba']['InstallationDir']}/service-58ea151f-0ad5-443c-b923-7c89c5b4afba.jar\""
 	stop_command "start-stop-daemon --stop --signal 15 --quiet --oknodo --pidfile /var/run/service-58ea151f-0ad5-443c-b923-7c89c5b4afba.pid"
 	action :nothing
	status_command "if ps -p `cat /var/run/service-58ea151f-0ad5-443c-b923-7c89c5b4afba.pid` > /dev/null  ; then exit 0; else exit 1; fi"
 	supports :start => true, :stop => true, :status => true
end

remote_file "#{node['CHOReOSData']['serviceData']['58ea151f-0ad5-443c-b923-7c89c5b4afba']['InstallationDir']}/service-58ea151f-0ad5-443c-b923-7c89c5b4afba.jar" do
    source "#{node['CHOReOSData']['serviceData']['58ea151f-0ad5-443c-b923-7c89c5b4afba']['PackageURL']}"
    action :nothing
    #notifies :enable, "service[service_58ea151f-0ad5-443c-b923-7c89c5b4afba_jar]"
    notifies :start, "service[service_58ea151f-0ad5-443c-b923-7c89c5b4afba_jar]"
end

if not node['CHOReOSData']['serviceData']['58ea151f-0ad5-443c-b923-7c89c5b4afba']['deactivate']
	ruby_block "install-file-58ea151f-0ad5-443c-b923-7c89c5b4afba" do
	    block do
	    	# do nothing!
	    	i = 0
	    end
		notifies :create_if_missing, "remote_file[#{node['CHOReOSData']['serviceData']['58ea151f-0ad5-443c-b923-7c89c5b4afba']['InstallationDir']}/service-58ea151f-0ad5-443c-b923-7c89c5b4afba.jar]"
	end
end

file "#{node['CHOReOSData']['serviceData']['58ea151f-0ad5-443c-b923-7c89c5b4afba']['InstallationDir']}/service-58ea151f-0ad5-443c-b923-7c89c5b4afba.jar" do
    notifies :stop, "service[service_58ea151f-0ad5-443c-b923-7c89c5b4afba_jar]", :immediately
    #notifies :disable, "service[service_58ea151f-0ad5-443c-b923-7c89c5b4afba_jar]", :immediately
    action :nothing
end

if node['CHOReOSData']['serviceData']['58ea151f-0ad5-443c-b923-7c89c5b4afba']['deactivate']
	ruby_block "remove-file-58ea151f-0ad5-443c-b923-7c89c5b4afba" do
	    block do
	    	# do nothing!
	    	i = 0
	    end
		notifies :delete, "remote_file[#{node['CHOReOSData']['serviceData']['58ea151f-0ad5-443c-b923-7c89c5b4afba']['InstallationDir']}/service-58ea151f-0ad5-443c-b923-7c89c5b4afba.jar]"
	end
end