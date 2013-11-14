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

ruby_block "remove-service41ce4e57-cf31-4115-9503-467a70be1e02" do
  block do
  	# do nothing!
  	i = 0
  end
  only_if { node.run_list.include?("recipe[service41ce4e57-cf31-4115-9503-467a70be1e02::jar]") }
  notifies :delete, "file[#{node['tomcat']['webapp_dir']}/41ce4e57-cf31-4115-9503-467a70be1e02.war]", :immediately
end

ruby_block "remove-service41ce4e57-cf31-4115-9503-467a70be1e02" do
  block do
  	node.run_list.remove("recipe[service41ce4e57-cf31-4115-9503-467a70be1e02::war]")
  end
  only_if { node.run_list.include?("recipe[service41ce4e57-cf31-4115-9503-467a70be1e02::war]") }
end

ruby_block "remove-deactivate-service41ce4e57-cf31-4115-9503-467a70be1e02" do
  block do
    node.run_list.remove("recipe[deactivate-service41ce4e57-cf31-4115-9503-467a70be1e02]")
  end
  only_if { node.run_list.include?("recipe[deactivate-service41ce4e57-cf31-4115-9503-467a70be1e02]") }
end
