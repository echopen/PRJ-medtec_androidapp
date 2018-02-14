In this folder, one can find tool for compiling a program that will emul an echopen probe. This emulator can run on a PC or on a RedPitaya (to be close to a real kit).

For compiling the C and the java code (./srcbin/probe_emulator.c), use the bash file run.sh provide here. Option for choosing the device on which the program will run use -m option, RP for RedPitaya and PC for your PC such as:

	bash run.sh RP

If you chose PC option, the executable file will be located in this folder. If you chose RP option, the executable will be send in the RedPitaya. To run the emulator, launch the bash file start.sh with the two options : type of image send (void, plate, hand or film) and the batery level (from 0 to 100) :

	bash start.sh hand 75

* void option will send on a loop an image fill with only value 8192
* plate option will send on a loop an image of a plate
* hand option will send on a loop an image of the cross-section of a hand
* film will send on a loop an echographic film of the cross-sections of a hand an harm

To execute the program on the RedPitaya you first need to connect to the RedPitaya *via* ssh:

	ssh root@192.168.128.3

pass is root. At connection you will be situated in /root folder where is located the program. Now you can just lanch it (with correct option).

