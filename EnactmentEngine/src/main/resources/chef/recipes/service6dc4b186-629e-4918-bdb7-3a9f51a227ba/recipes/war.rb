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

if not node['CHOReOSData']['serviceData']['6dc4b186-629e-4918-bdb7-3a9f51a227ba']['deactivate']
	remote_file "war_file" do
  		source "#{node['CHOReOSData']['serviceData']['6dc4b186-629e-4918-bdb7-3a9f51a227ba']['PackageURL']}"
  		path "#{node['tomcat']['webapp_dir']}/6dc4b186-629e-4918-bdb7-3a9f51a227ba.war"
  		mode "0755"
  		action :create_if_missing
	end
end

if node['CHOReOSData']['serviceData']['6dc4b186-629e-4918-bdb7-3a9f51a227ba']['deactivate']
	file "#{node['tomcat']['webapp_dir']}/6dc4b186-629e-4918-bdb7-3a9f51a227ba.war" do
		action :delete
	end
end
