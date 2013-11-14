#
# Cookbook Name:: generic-jar-service-template
# Recipe:: default
#
# Copyright 2012, YOUR_COMPANY_NAME
#
# All rights reserved - Do Not Redistribute
#

##########################################################################
#									 #
#                IMPORTANT DEVELOPMENT NOTICE:                           #
#									 #
# All ocurrences of $ NAME must be replaced with the actual service name #
#            before uploading the recipe to the chef-server              #
#									 #
##########################################################################

include_recipe "apt" # java recipe is failing without recipe apt (and tomcat depends on java)
include_recipe "tomcat::choreos"

remote_file "war_file" do
	source "#{node['CHOReOSData']['serviceData']['d7c473f2-4b47-408a-9820-1a1b19450865']['PackageURL']}"
	path "#{node['tomcat']['webapp_dir']}/d7c473f2-4b47-408a-9820-1a1b19450865.war"
	mode "0755"
	action :nothing
end

if not node['CHOReOSData']['serviceData']['d7c473f2-4b47-408a-9820-1a1b19450865']['deactivate']
	ruby_block "install-file-d7c473f2-4b47-408a-9820-1a1b19450865" do
		block do
			# do nothing!
			i = 0
		end
		notifies :create_if_missing, "remote_file[war_file]" 
	end
end

file "#{node['tomcat']['webapp_dir']}/d7c473f2-4b47-408a-9820-1a1b19450865.war" do
	action :nothing
end

if node['CHOReOSData']['serviceData']['d7c473f2-4b47-408a-9820-1a1b19450865']['deactivate']
	ruby_block "uninstall-file-d7c473f2-4b47-408a-9820-1a1b19450865" do
		block do
			# do nothing!
			i = 0
		end
		notifies :delete, "file[#{node['tomcat']['webapp_dir']}/d7c473f2-4b47-408a-9820-1a1b19450865.war]"
	end
end
