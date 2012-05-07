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

include_recipe "petals"

remote_file "sa_file" do
  source "#{node['service']['$NAME']['URL']}"
  path "/tmp/$NAME.zip"
  mode "0755"
  action :create
  not_if do
    File.exists?("#{node['petals']['install_dir']}/../installed/$NAME.zip")
  end
end

execute "install sa" do
  command "cp /tmp/$NAME.zip #{node['petals']['install_dir']}/"
  creates "#{node['petals']['install_dir']}/../installed/$NAME.zip"
  action :run
end
