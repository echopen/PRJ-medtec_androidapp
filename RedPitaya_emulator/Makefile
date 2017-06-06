#
# Red Pitaya SDK Makefile
#

#Includes
RP_PATH_INCLUDE = -I./inc

#Library
RP_LIB_INCLUDE = -L./inc -lm -lpthread -lrp

#Cross compiler 
CROSS_COMPILE	?=	$(GNUEABI)
#Cross compiler options
GNUEABI	?= arm-linux-gnueabi-
GNUEABIHF ?= arm-linux-gnueabihf-
CC = $(CROSS_COMPILE)gcc

#Flags
CFLAGS = -g -O2 -std=gnu99 -Wall -Werror

#Objects
OBJECTS	= #control.o common.o processing.o tcp.o implementer.o
TARGET ?= implementation

#Build the executable
all: $(TARGET)

$(TARGET): $(TARGET).o $(OBJECTS)
	$(CC) -o $@ $^ $(CFLAGS) $(RP_LIB_INCLUDE)

$(TARGET).o: srcbin/$(TARGET).c
	$(CC) -c $(CFLAGS) $(RP_PATH_INCLUDE) $< -o $@

%.o: srclib/%.c
	$(CC) -c $(CFLAGS) $(RP_PATH_INCLUDE) $< -o $@

clean:
	-$(RM) $(TARGET) *.o ~*
