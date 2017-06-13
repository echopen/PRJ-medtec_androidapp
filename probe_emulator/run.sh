#!/bin/bash

usage()
{
	echo "test de fichier bash pour compiler avec option"
	echo "toto"
	exit 0 #exit after diplaying help
}

if [ $# -eq 0 ]
then
        echo "You haven't write any option, run $0 -h for help"
        exit 0
fi

machine="PC"

while getopts "h m:" opt 
do
	case $opt in
		h)
			usage
			;;
		m)
			machine="$OPTARG"
			;;
		\?)
			echo "Unknowed option, run $0 -h for help"
			exit 1
			;;
	esac
done

case $machine in
	RP)

		if [ -e probe_emulator ]; then
			rm probe_emulator
		fi
		if sshpass -p root ssh root@192.168.128.3 '[ -d /root/data ]'; then
		printf "data folder alredy exist, not copying\n"
		else
			echo "sending data folder, please wait"
			sshpass -p root scp -rp ./data root@192.168.128.3:/root/
		fi
		arm-linux-gnueabi-gcc -g -O2 -std=gnu99 -Wall -Werror ./srcbin/probe_emulator.c -o probe_emulator -lpthread
		sshpass -p root scp ./probe_emulator root@192.168.128.3:/root/
		;;
	PC)
		if [ -e probe_emulator ];then
			rm probe_emulator
		fi
		gcc -g  ./srcbin/probe_emulator.c -o probe_emulator -lpthread
		;;
	*)
		echo "wrong argument for option -t"
		exit 1
		;;
esac

exit 0 
