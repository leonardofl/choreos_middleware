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

ruby_block "remove-service69990fab-a5c2-40b3-97e4-1b0105f95046" do
  block do
  	# do nothing!
  	i = 0
  end
  only_if { node.run_list.include?("recipe[service69990fab-a5c2-40b3-97e4-1b0105f95046::jar]") }
  notifies :delete, "file[#{node['tomcat']['webapp_dir']}/69990fab-a5c2-40b3-97e4-1b0105f95046.war]", :immediately
end

ruby_block "remove-service69990fab-a5c2-40b3-97e4-1b0105f95046" do
  block do
  	node.run_list.remove("recipe[service69990fab-a5c2-40b3-97e4-1b0105f95046::war]")
  end
  only_if { node.run_list.include?("recipe[service69990fab-a5c2-40b3-97e4-1b0105f95046::war]") }
end

ruby_block "remove-deactivate-service69990fab-a5c2-40b3-97e4-1b0105f95046" do
  block do
    node.run_list.remove("recipe[deactivate-service69990fab-a5c2-40b3-97e4-1b0105f95046]")
  end
  only_if { node.run_list.include?("recipe[deactivate-service69990fab-a5c2-40b3-97e4-1b0105f95046]") }
end
