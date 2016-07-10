#!/bin/bash

RED='\033[0;31m'
NC='\033[0m' # No Color

echo "Why you so lazy >.<"
sleep 1
echo "Good thing I was bored and wrote this script for ya."
sleep 1
echo "OK Let's get started."
sleep 1
echo -e "What is your GitHub repo path? (ie. dev foundation is '${RED}Fadyazmy/developersfoundation${NC}')"
echo "Make sure to include the username and slash"

read REPO

while true; do
    read -p "Using ${RED}https://github.com/$REPO.git${NC} Are you sure this is correct?" yn
    case $yn in
        [Yy]* ) break;;
        [Nn]* ) echo "I give up, run da script to try again."; exit;;
        * ) echo "Please answer yes or no.";;
    esac
done

# LOL FINALLY ACTUALLY RUNNING THE COMMANDS
git remote rm origin
git remote add origin https://github.com/$REPO.git
git push -u origin master