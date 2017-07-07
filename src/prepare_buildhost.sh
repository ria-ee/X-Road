#!/bin/bash
test "$(lsb_release -si)" == "Ubuntu" || { echo "This script supports only Ubuntu"; exit 1; }
set -e

sudo apt-get update
sudo apt-get install -y curl software-properties-common

REL=$(lsb_release -sr | cut -d'.' -f1)
JRUBY_VERSION=$(cat .jruby-version || echo "1.7.27")

if [ $REL -lt 16 ]; then
    sudo apt-add-repository -y ppa:openjdk-r/ppa
    sudo apt-get update
fi

sudo apt-get install -y openjdk-8-jdk-headless build-essential git unzip debhelper
sudo update-ca-certificates -f

cd ~

if ! command -v rvm &>/dev/null; then
    gpg --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 409B6B1796C275462A1703113804BB82D39DC0E3
    curl -L https://get.rvm.io | bash -s stable
    source ~/.rvm/scripts/rvm
fi

rvm install jruby-$JRUBY_VERSION --binary
rvm jruby-$JRUBY_VERSION do jgem install jruby-jars:$JRUBY_VERSION bundler:1.14.6 warbler:1.4.9 bundler-audit

mkdir -p /var/tmp/xroad

if [[ $REL -ge 16 && ! -e /.dockerenv ]]; then
    if ! command -v docker &>/dev/null; then
        echo "Install docker"
        sudo apt-get install -y docker.io
        sudo addgroup $(whoami) docker
        newgrp docker
    fi
fi

