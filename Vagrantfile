# -*- mode: ruby -*-
# vi: set ft=ruby :

unless Vagrant.has_plugin?("vagrant-docker-compose")
  system("vagrant plugin install vagrant-docker-compose")
  puts "Dependencies installed, please try the command again."
  exit
end

Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/trusty64"
  config.vm.network "private_network", ip: "192.168.56.102"
  
  config.vm.provider "virtualbox" do |v|
    v.memory = 1024
    v.cpus = 2
  end
  
  port1 = 80
  port2 = 9200

  config.vm.network(:forwarded_port, guest: port1, host: port1)
  config.vm.network(:forwarded_port, guest: port2, host: port2)

#  config.vm.provision :shell, inline: "apt-get update"
  config.vm.provision :docker
  config.vm.provision :docker_compose, 
	yml: ["/vagrant/docker-compose.yml"], 
	rebuild: true, 
	project_name: "jacob_infra",
	run: "always"
end