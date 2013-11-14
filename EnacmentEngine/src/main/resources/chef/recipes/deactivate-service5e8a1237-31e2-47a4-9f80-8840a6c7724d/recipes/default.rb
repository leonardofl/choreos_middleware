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

ruby_block "remove-service5e8a1237-31e2-47a4-9f80-8840a6c7724d" do
  block do
  	# do nothing!
  	i = 0
  end
  only_if { node.run_list.include?("recipe[service5e8a1237-31e2-47a4-9f80-8840a6c7724d::jar]") }
  notifies :delete, "file[#{node['tomcat']['webapp_dir']}/5e8a1237-31e2-47a4-9f80-8840a6c7724d.war]", :immediately
end

ruby_block "remove-service5e8a1237-31e2-47a4-9f80-8840a6c7724d" do
  block do
  	node.run_list.remove("recipe[service5e8a1237-31e2-47a4-9f80-8840a6c7724d::war]")
  end
  only_if { node.run_list.include?("recipe[service5e8a1237-31e2-47a4-9f80-8840a6c7724d::war]") }
end

ruby_block "remove-deactivate-service5e8a1237-31e2-47a4-9f80-8840a6c7724d" do
  block do
    node.run_list.remove("recipe[deactivate-service5e8a1237-31e2-47a4-9f80-8840a6c7724d]")
  end
  only_if { node.run_list.include?("recipe[deactivate-service5e8a1237-31e2-47a4-9f80-8840a6c7724d]") }
end
