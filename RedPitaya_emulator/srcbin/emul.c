#include<stdio.h>
#include<stdlib.h>
#include<stdint.h>
#include<time.h>
#include<signal.h>

#include "TCP_API.h"

#define PORT 7538

float x0=80.0;
float xf=160.0;
int dec=8;
int Nline=64;
double sector=80.0;
int mode_RP=0;
int step=1;

uint32_t buffer_length=0;
SOCKET sock=0;
client *client_list=NULL;
int16_t** data0=NULL;

void signal_callback_handler()
{
	printf("signal callback handler\n");
        close_TCP_server(&sock, client_list);
        free(client_list);
	free(data0);
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

	float scale=2.0*125.0/1.48/((float)dec); //factor 2.0 for back and forth
        buffer_length=(uint32_t)(scale*(xf-x0));
        if (buffer_length>16384){buffer_length=16384;}

	//TCP server
	unsigned int Nmax=5;
	client_list=(client *)malloc(sizeof(client));
        init_TCP_server(&sock, PORT, client_list, Nmax);
        launch_server(&sock, client_list);


	int i=0;
	data0=(int16_t**)malloc(Nline*sizeof(int16_t*));
	for (i=0 ; i<Nline ; i++)
	{
		data0[i]=(int16_t*)malloc((buffer_length+1)*sizeof(int16_t));
		data0[i][0]=(int16_t)(i+1);
	}

	load(buffer_length, Nline, data0, "plate.txt");


	int t=5000;
	while(1)
	{
		for (i=0 ; i<Nline ; i++)
		{
			//printf("line: %i\n",(int)data0[i][0]);
			send_int16_TCP_server(client_list, data0[i], (int)buffer_length+1, -1);
			usleep(t);
		}
		for (i=Nline ; i>0 ; i--)
		{
			//printf("line: %i\n",(int)data0[i-1][0]);
			send_int16_TCP_server(client_list, data0[i-1], (int)buffer_length+1, -1);
                        usleep(t);

		}
	}

	printf("close all\n");
	free(data0);
	close_TCP_server(&sock, client_list);
        free(client_list);
	return 0;
}
