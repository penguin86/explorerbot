#!/bin/sh
# Starts the explorerbot's client. 
# Usage: start_server [ipaddress]
# Example: start_server 192.168.0.35

java -Djava.library.path="./explorerbot_lib/" -jar explorerbot.jar client $1
