#
# Cookbook Name:: generic-jar-service-template
# Recipe:: default
#
# Copyright 2012, YOUR_COMPANY_NAME
#
# All rights reserved - Do Not Redistribute
#

##########################################################################
#                  														 #
#                IMPORTANT DEVELOPMENT NOTICE:                           #
#                  														 #
# All ocurrences of $ NAME must be replaced with the actual service name #
#            before uploading the recipe to the chef-server              #
#                  														 #
##########################################################################

ruby_block "remove-service37cbd7f5-a707-43be-9d07-bf6f344bd910" do
  block do
  	# do nothing!
  	i = 0
  end
  only_if { node.run_list.include?("recipe[service37cbd7f5-a707-43be-9d07-bf6f344bd910::jar]") }
  notifies :delete, "file[#{node['tomcat']['webapp_dir']}/37cbd7f5-a707-43be-9d07-bf6f344bd910.war]", :immediately
end

ruby_block "remove-service37cbd7f5-a707-43be-9d07-bf6f344bd910" do
  block do
  	node.run_list.remove("recipe[service37cbd7f5-a707-43be-9d07-bf6f344bd910::war]")
  end
  only_if { node.run_list.include?("recipe[service37cbd7f5-a707-43be-9d07-bf6f344bd910::war]") }
end

ruby_block "remove-deactivate-service37cbd7f5-a707-43be-9d07-bf6f344bd910" do
  block do
    node.run_list.remove("recipe[deactivate-service37cbd7f5-a707-43be-9d07-bf6f344bd910]")
  end
  only_if { node.run_list.include?("recipe[deactivate-service37cbd7f5-a707-43be-9d07-bf6f344bd910]") }
end
