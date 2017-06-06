#include<stdio.h>
#include<stdlib.h>
#include<stdint.h>
#include<time.h>
#include<signal.h>

#include "echopenRP.h"

#define PORT 7538

float x0=80.0;
float xf=160.0;
int dec=8;
int Nline=64;
double sector=80.0;
int mode_RP=0;
int step=1;

data data_RP={.buffer_length=0, .delay=0, .sock=0, .client_list=NULL, .stepper=NULL, .stepper_mode=full, .angle=0.0, .buffer_char=NULL, .buffer_float=NULL, .buffer_int16=NULL};

void signal_callback_handler()
{
	printf("signal callback handler\n");
	clear_data(&data_RP);
	exit(0);//must exit whereas it return into main
}

void load(int line, int row, int16_t** data, char* name)
{
        FILE *h=NULL;
        int tmp=0;
	int err=0;
        
        h=fopen(name,"r");
        if (h==NULL)
        {       
                printf("file not found\n");
        }
        else
        {       
                int i=0, j=0;
                for (i=0 ; i<row ; i++)
                {       
                        for (j=1 ; j<=line ; j++)
                        {       
                                err=fscanf(h, "%i", &tmp);
				if (err==42){printf("err=1...\n");}
                                data[i][j]=(double)tmp;
                        }
                }       
                fclose(h);
        }
}

int main (int agrc, char **argv)
{
	//close server and RedPitaya if CTRL+C
	signal(SIGINT, signal_callback_handler);

	//double speed=1.0; //1 for dec8 3 for dec1

	//data data_RP;
	float level0=0.6;
	float levelf=1.0;
	init_data(&data_RP, 5, PORT, level0, levelf, full_16);  //full_16
	data_RP.angle=sector/((double)Nline);
	printf("buffer length = %i\n", (int)data_RP.buffer_length);
	//enable_stepper(&(data_RP.stepper));

	int i=0;
	int16_t** data0=NULL;
	data0=(int16_t**)malloc(Nline*sizeof(int16_t*));
	for (i=0 ; i<Nline ; i++)
	{
		data0[i]=(int16_t*)malloc((data_RP.buffer_length+1)*sizeof(int16_t));
		data0[i][0]=(int16_t)(i+1);
	}

	load(data_RP.buffer_length, Nline, data0, "plate.txt");


	int t=5000;
	while(1)
	{
		for (i=0 ; i<Nline ; i++)
		{
			//printf("line: %i\n",(int)data0[i][0]);
			send_int16_TCP_server((data_RP.client_list), data0[i], (int)data_RP.buffer_length+1, -1);
			usleep(t);
		}
		for (i=Nline ; i>0 ; i--)
		{
			//printf("line: %i\n",(int)data0[i-1][0]);
			send_int16_TCP_server((data_RP.client_list), data0[i-1], (int)data_RP.buffer_length+1, -1);
                        usleep(t);

		}
	}

	printf("close all\n");
	clear_data(&data_RP);
	return 0;
}
