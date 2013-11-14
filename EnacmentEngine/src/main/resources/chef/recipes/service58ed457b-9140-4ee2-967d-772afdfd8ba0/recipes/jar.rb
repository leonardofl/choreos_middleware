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


service "service_58ed457b-9140-4ee2-967d-772afdfd8ba0_jar" do
	# Dirty trick to save the java pid to a file
 	start_command "start-stop-daemon -b --start --quiet --oknodo --pidfile /var/run/service-58ed457b-9140-4ee2-967d-772afdfd8ba0.pid --exec /bin/bash -- -c \"echo \\$\\$ > /var/run/service-58ed457b-9140-4ee2-967d-772afdfd8ba0.pid ; exec java -jar #{node['CHOReOSData']['serviceData']['58ed457b-9140-4ee2-967d-772afdfd8ba0']['InstallationDir']}/service-58ed457b-9140-4ee2-967d-772afdfd8ba0.jar\""
 	stop_command "start-stop-daemon --stop --signal 15 --quiet --oknodo --pidfile /var/run/service-58ed457b-9140-4ee2-967d-772afdfd8ba0.pid"
 	action :nothing
	status_command "if ps -p `cat /var/run/service-58ed457b-9140-4ee2-967d-772afdfd8ba0.pid` > /dev/null  ; then exit 0; else exit 1; fi"
 	supports :start => true, :stop => true, :status => true
end

remote_file "#{node['CHOReOSData']['serviceData']['58ed457b-9140-4ee2-967d-772afdfd8ba0']['InstallationDir']}/service-58ed457b-9140-4ee2-967d-772afdfd8ba0.jar" do
    source "#{node['CHOReOSData']['serviceData']['58ed457b-9140-4ee2-967d-772afdfd8ba0']['PackageURL']}"
    action :nothing
    #notifies :enable, "service[service_58ed457b-9140-4ee2-967d-772afdfd8ba0_jar]"
    notifies :start, "service[service_58ed457b-9140-4ee2-967d-772afdfd8ba0_jar]"
end

if not node['CHOReOSData']['serviceData']['58ed457b-9140-4ee2-967d-772afdfd8ba0']['deactivate']
	ruby_block "install-file-58ed457b-9140-4ee2-967d-772afdfd8ba0" do
	    block do
	    	# do nothing!
	    	i = 0
	    end
		notifies :create_if_missing, "remote_file[#{node['CHOReOSData']['serviceData']['58ed457b-9140-4ee2-967d-772afdfd8ba0']['InstallationDir']}/service-58ed457b-9140-4ee2-967d-772afdfd8ba0.jar]"
	end
end

file "#{node['CHOReOSData']['serviceData']['58ed457b-9140-4ee2-967d-772afdfd8ba0']['InstallationDir']}/service-58ed457b-9140-4ee2-967d-772afdfd8ba0.jar" do
    notifies :stop, "service[service_58ed457b-9140-4ee2-967d-772afdfd8ba0_jar]", :immediately
    #notifies :disable, "service[service_58ed457b-9140-4ee2-967d-772afdfd8ba0_jar]", :immediately
    action :nothing
end

if node['CHOReOSData']['serviceData']['58ed457b-9140-4ee2-967d-772afdfd8ba0']['deactivate']
	ruby_block "remove-file-58ed457b-9140-4ee2-967d-772afdfd8ba0" do
	    block do
	    	# do nothing!
	    	i = 0
	    end
		notifies :delete, "remote_file[#{node['CHOReOSData']['serviceData']['58ed457b-9140-4ee2-967d-772afdfd8ba0']['InstallationDir']}/service-58ed457b-9140-4ee2-967d-772afdfd8ba0.jar]"
	end
end