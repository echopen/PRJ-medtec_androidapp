In this folder one can find file for emulating the echopen kit on a RedPitaya with two differents images. The first image is a simple plate, and the second is a hand. Both images raw data (data with the phase, not the envelope of the image) are written in the files hand.txt and plate.txt. "raw" images and scan-converted images that must be obtained are shown on the png files. 

The sources of the codes are in srcbin folder.

Protocol:
- turn on the RedPitaya the RedPitaya and connect to RedPitaya wifi (pass:redpitaya)
- copy files (plate.txt, plate_emulator, hand.txt, hand_emulator) in a folder of the RedPitaya (command line: scp ./name root@192.168.128.3:/root/)
- connect to the RedPitaya (command line: ssh root@192.168.128.3, pass: root);
- if you don't have the right to execute plate_emulator or hand emulator do a chmod 777 on the file then execute it
- enjoy
