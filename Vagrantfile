Vagrant.configure("2") do |config|
  # All Vagrant configuration is done here. The most common configuration
  # options are documented and commented below. For a complete reference,
  # please see the online documentation at vagrantup.com.

  config.vm.hostname = "nexus-plugins"

  config.vm.box = "Berkshelf-CentOS-6.3-x86_64-minimal"
  config.vm.box_url = "https://dl.dropbox.com/u/31081437/Berkshelf-CentOS-6.3-x86_64-minimal.box"
  
  config.vm.network :private_network, ip: "192.168.33.10"
  config.vm.network :forwarded_port, guest: 8081, host: 8081
  config.vm.network :forwarded_port, guest: 8443, host: 8443

  config.vm.synced_folder "/Users/kallan/src/nexus-digests-plugin/target", "/shared_folder"

  config.ssh.max_tries = 40
  config.ssh.timeout   = 120

  config.vm.provision :chef_solo do |chef|
    chef.json = {
      :mysql => {
        :server_root_password => 'rootpass',
        :server_debian_password => 'debpass',
        :server_repl_password => 'replpass'
      },
      :nexus => {
        :jetty => {
          :loopback => false
        },
        :ssl => {
          :verify => false
        }
      }
    }

    chef.run_list = [
      "recipe[nexus::default]"
    ]
  end

  script = <<SCRIPT
rm -rf /usr/local/nexus/current/nexus-2.3.1-01/nexus/WEB-INF/plugin-repository/nexus-digests-plugin-1.0.0-SNAPSHOT/
mv /shared_folder/nexus-digests-plugin-1.0.0-SNAPSHOT /usr/local/nexus/current/nexus-2.3.1-01/nexus/WEB-INF/plugin-repository/
chown -R nexus:nexus /usr/local/nexus/current/nexus-2.3.1-01/nexus/WEB-INF/plugin-repository/nexus-digests-plugin-1.0.0-SNAPSHOT/
sudo /etc/init.d/nexus restart
SCRIPT

  config.vm.provision :shell,
    :inline => script
end
