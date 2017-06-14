#include<stdio.h>
#include<stdlib.h>
#include<stdint.h>
#include<time.h>
#include<signal.h>
#include<string.h>
//#include<errno.h>

#include "TCP_API.h"

#define PORT 7538

float r0=80.0;
float rf=160.0;
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

void load_settings(char *directory)
{
	FILE *h=NULL;
	char name[50];
	int set[6];
	sprintf(name,"%s/settings.txt", directory);
	
	h=fopen(name,"r");
	if (h==NULL) {printf("file not found\n");}
	else
	{
		int i=0, err=0, tmp=0;
		for (i=0 ; i<6 ; i++)
		{
			err=fscanf(h, "%i", &tmp);
			if (err!=1) {printf("error while loading settings\n");}
			set[i]=tmp;
		}
	}

	float *pr0=&r0;
	(*pr0)=(float)set[0];
	float *prf=&rf;
	(*prf)=(float)set[1];
	int *pdec=&dec;
	(*pdec)=set[2];
	int *pNline=&Nline;
	(*pNline)=set[3];
	double *psector=&sector;
	(*psector)=(double)set[4];
	mode_RP=set[5];
}

void load_image(int line, int row, int16_t** data, char* name)
{
        FILE *h=NULL;
	
        char *linef = NULL;
        size_t len = 0;

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

	        for (i=0 ; i<8 ; i++)//skipping header of files
	        {
        	        if ((err = getline( &linef, &len, h)) != 1)
                	{
                        	//printf("line number %i\n",i);
                        	//printf("line red: %s\n",linef);
                	}
                	else {printf("error while getline\n");}
        	}

                for (i=0 ; i<row ; i++)
                {       
                        for (j=1 ; j<=line ; j++)
                        {       
                                if ((err=fscanf(h,"%i",&tmp))!=2) {data[i][j]=(int16_t)tmp;}
				else {printf("error while fscanf\n");}
				/*err=fscanf(h, "%i", &tmp);
				if (err==42){printf("err=1...\n");}
                        	data[i][j]=(int16_t)tmp;*/
                        }
                }       
                fclose(h);
        }
}

void init_data(void)
{
        float scale=2.0*125.0/1.48/((float)dec); //factor 2.0 for back and forth
        buffer_length=(uint32_t)(scale*(rf-r0));
        if (buffer_length>16384){buffer_length=16384;}

	int i=0;
        data0=(int16_t**)malloc(Nline*sizeof(int16_t*));
        for (i=0 ; i<Nline ; i++)
        {
                data0[i]=(int16_t*)malloc((buffer_length+1)*sizeof(int16_t));
                data0[i][0]=(int16_t)(i+1);
        }
}

void send_image(int16_t** data, int sens)
{
	//this funtion is used to send data such as the kit does, so one time it send from line 1 to Nline, second time it's from line Nline to 1
	//int sens is used to tell wich case we use
	int i=0, t=1000;
	if(sens==1)
	{
                for (i=0 ; i<Nline ; i++)
                {
                        send_int16_TCP_server(client_list, data0[i], (int)buffer_length+1, -1);
                        usleep(t);
                }
	}
	else
	{
	        for (i=Nline ; i>0 ; i--)
                {
                        send_int16_TCP_server(client_list, data0[i-1], (int)buffer_length+1, -1);
                        usleep(t);
                }

	}
}

int main (int argc, char **argv)
{
	if (argc==1||argc>2)
	{
		printf("error, probe emulator must be launch with an option (void, plate, hand or film\n");
		printf("try ./probe_emulator plate\n");
	        free(client_list);
	        free(data0);
		exit(1);
	}

	//close server and RedPitaya if CTRL+C
	signal(SIGINT, signal_callback_handler);


	//TCP server
	unsigned int Nmax=5;
	client_list=(client *)malloc(sizeof(client));
        init_TCP_server(&sock, PORT, client_list, Nmax);
        launch_server(&sock, client_list);

	if (strcmp(argv[1],"void")==0)
	{
		printf("void image\n");
		//empty image
		init_data();
		int i=0, j=0;
		for (i=0 ; i<Nline ; i++)
		{
			for (j=1 ; j<(int)buffer_length+1 ; j++) {data0[i][j]=8192;}
		}
		while(1)
		{
			send_image(data0, 1);
			send_image(data0, 2);
		}
	}
	else if(strcmp(argv[1],"plate")==0)
	{
		printf("plage image\n");
		//one fixed image
	        char *path;
	        path="./data/plate";
	        load_settings(path);
		init_data();

		char name[50];
		sprintf(name,"%s/plate.txt",path);
		load_image(buffer_length, Nline, data0, name);
		while(1)
	        {
	                send_image(data0, 1);
	                send_image(data0, 2);
	        }
	}
	else if(strcmp(argv[1],"hand")==0)
	{
		printf("hand image\n");
                //one fixed image
                char *path;
                path="./data/hand";
                load_settings(path);
                init_data();

                char name[50];
                sprintf(name,"%s/hand.txt",path);
                load_image(buffer_length, Nline, data0, name);
                while(1)
                {
                        send_image(data0, 1);
                        send_image(data0, 2);
		}
	}
	else if(strcmp(argv[1],"film")==0)
	{
		printf("film\n");		
		//one "film"
		int i=0, tmp=0;
		int Nimage=100;

		char *path;
	        path="./data/film";
	        load_settings(path);
	        init_data();

		char name[50];
			while(1)
		{
			for (i=0 ; i<=Nimage ; i++)
			{
				tmp=i/2;
				sprintf(name,"%s/int%i.txt",path,i);
			        load_image(buffer_length, Nline, data0, name);
				if (tmp*2==i) {send_image(data0,1);}
				else {send_image(data0,2);}
			}
		}
	}
	else 
	{
		printf("you write option: %s\n",argv[1]);
		printf("Error wrong argument for probe_emulator. Option must be in:\n");
		printf("void for a image with only 8192 value\n");
		printf("plate for a static image of a plate\n");
		printf("hand for a static image of hand\n");
		printf("film for an echography film of hand and arm\n");
	}



	printf("close all\n");
	free(data0);
	close_TCP_server(&sock, client_list);
        free(client_list);
	return 0;
}
