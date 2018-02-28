#!/bin/bash

# argment 1 : image to send
# argument 2 : baterie level

if [ -e probe_emulator ] & [ -e ./srcbin/Main.class ];then 
	gnome-terminal -e "./probe_emulator $1" &
	cd srcbin
	gnome-terminal -e "java Main $2" &
fi
