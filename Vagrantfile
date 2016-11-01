# -*- mode: ruby -*-
# vi: set ft=ruby :

unless Vagrant.has_plugin?("vagrant-docker-compose")
  system("vagrant plugin install vagrant-docker-compose")
  puts "Dependencies installed, please try the command again."
  exit
end

unless Vagrant.has_plugin?("vagrant-proxyconf")
  system("vagrant plugin install ./scripts/vagrant-proxyconf-1.5.2.gem")
  puts "Dependencies installed, please try the command again."
  exit
end

Vagrant.configure("2") do |config|
  config.vm.box = "ubuntu/trusty64"
  config.vm.network "private_network", ip: "192.168.56.102"
  config.vm.box_check_update = false
  
  config.vm.provider "virtualbox" do |v|
    v.memory = 5120
    v.cpus = 3
  end


  
  port1 = 8080
  port2 = 9200
  port3 = 80

  config.vm.network(:forwarded_port, guest: port1, host: port1)
  config.vm.network(:forwarded_port, guest: port2, host: port2)
  config.vm.network(:forwarded_port, guest: port3, host: port3)

#  config.vm.provision :shell, inline: "apt-get update"
  config.vm.provision :docker
  config.vm.provision :docker_compose, 
	yml: ["/vagrant/docker-compose.yml"], 
	rebuild: true, 
	project_name: "jacob_infra",
	run: "always"
end