#!/bin/bash

# This is a simple run and compile script. It copies the file
# argv[0] to a specific Red Pitaya with IP argv[1]. It uses putty-tools,
# installed with install.sh.

#Global variables definition
IP=$1
REDIRECT='/var/log/sdk_log/debug'
EXECUTABLE=$(echo $2)

usage(){
  echo    "Usage: - Input argument 1: Red Pitaya IP address in form 192.168.128.3."
  echo -e "       - Input argument 2: File to be copied, compiled and ran on a redpitaya system.\n"
  exit 1
}

#TODO: Add timestamp to redirect output
timeStamp(){
  exit 1
}

if [ $# -eq 0 ] || [ $# -gt 2 ]
  then
     echo -e "\nWrong argument number!"
     usage
fi

echo -e "\nEXECUTING RED PITAYA RUN SCRIPT..."
echo -e "\nCOPYING RED PITAYA INCLUDES IF NECESSARY..."
if [ -f "./inc/librp.so" ];then
	echo "./inc/librp.so does exists. Not copying."
else
	echo "./inc/librp.so does exists. Copying..."
	sshpass -p root scp -r root@$IP:/opt/redpitaya/lib/librp.so inc
fi
if [ -f "./inc/rp.h" ];then
	echo "./inc/rp.h does exists. Not copying."
else
	echo "./inc/rp.h does not exists. Copying..."
	sshpass -p root scp -r root@$IP:/opt/redpitaya/include/redpitaya/rp.h inc;
fi

echo -e "\nCOMPILING SOURCE FILE..."

make all OBJECT=$2 TARGET=$EXECUTABLE

#Remount red pitaya file system
#sshpass -p root ssh root@$IP 'mount -o rw, remount /opt/redpitaya'

#Creating log directories
sshpass -p root ssh root@$IP 'mkdir -p /var/log/sdk_log'
sshpass -p root ssh root@$IP 'touch /var/log/sdk_log/debug'

#Copying executable file to red pitaya
echo -e "\nEXECUTING REMOTE FILE..."
echo -e "\nOUTPUT: \n----------"
sshpass -p root scp $PWD/$EXECUTABLE root@$IP:/tmp
sshpass -p root ssh root@$IP '/tmp/'$EXECUTABLE' | tee '$REDIRECT

#make clean
echo -e "\n----------\nREMOVING ARTIFACTS..."
#make clean TARGET=$EXECUTABLE
