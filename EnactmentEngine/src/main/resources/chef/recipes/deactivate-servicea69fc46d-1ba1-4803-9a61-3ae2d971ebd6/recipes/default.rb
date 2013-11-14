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

ruby_block "remove-servicea69fc46d-1ba1-4803-9a61-3ae2d971ebd6" do
  block do
  	# do nothing!
  	i = 0
  end
  only_if { node.run_list.include?("recipe[servicea69fc46d-1ba1-4803-9a61-3ae2d971ebd6::jar]") }
  notifies :delete, "file[#{node['tomcat']['webapp_dir']}/a69fc46d-1ba1-4803-9a61-3ae2d971ebd6.war]", :immediately
end

ruby_block "remove-servicea69fc46d-1ba1-4803-9a61-3ae2d971ebd6" do
  block do
  	node.run_list.remove("recipe[servicea69fc46d-1ba1-4803-9a61-3ae2d971ebd6::war]")
  end
  only_if { node.run_list.include?("recipe[servicea69fc46d-1ba1-4803-9a61-3ae2d971ebd6::war]") }
end

ruby_block "remove-deactivate-servicea69fc46d-1ba1-4803-9a61-3ae2d971ebd6" do
  block do
    node.run_list.remove("recipe[deactivate-servicea69fc46d-1ba1-4803-9a61-3ae2d971ebd6]")
  end
  only_if { node.run_list.include?("recipe[deactivate-servicea69fc46d-1ba1-4803-9a61-3ae2d971ebd6]") }
end
