#!/bin/bash

usage()
{
	echo "Bash file for compiling an emulator of the echopen probe. The program can run on RedPitaya or on computer with linux OS. Run run.sh -m OPTION to compile the code, where OPTION=RP for a program running on RedPitaya and OPTION=PC for running on a PC."
	echo "On a PC just enter ./probe_emulator image_type to launch the emulator, image_type=void or plate or hand or film."
	echo "On a RedPitaya you must first connect to the wifi network Red Pitaya (pass:redpitaya) then execute this bash file. After connect to RedPitaya via ssh: ssh root@192.168.128.3 (pass:root). In the folder where you are located (/root) just launch the emulator such as for a PC."
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
			echo "data sent"
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

echo "process done"

exit 0 
