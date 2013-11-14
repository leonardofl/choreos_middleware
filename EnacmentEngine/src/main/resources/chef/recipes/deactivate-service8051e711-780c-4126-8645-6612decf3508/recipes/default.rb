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

ruby_block "remove-activate-service8051e711-780c-4126-8645-6612decf3508" do
  block do
    node.run_list.remove("recipe[activate-service8051e711-780c-4126-8645-6612decf3508]")
  end
  only_if { node.run_list.include?("recipe[activate-service8051e711-780c-4126-8645-6612decf3508]") }
end


ruby_block "remove-deactivate-service8051e711-780c-4126-8645-6612decf3508" do
  block do
    node.run_list.remove("recipe[deactivate-service8051e711-780c-4126-8645-6612decf3508]")
  end
  only_if { node.run_list.include?("recipe[deactivate-service8051e711-780c-4126-8645-6612decf3508]") }
  notifies :stop, "service[service_8051e711-780c-4126-8645-6612decf3508_jar]", :immediately
end
