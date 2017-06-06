#ifndef ECHOPENRP_H
#define ECHOPENRP_H

#include<signal.h>

#include"rp.h"
#include"stepper.h"
#include"TCP_API.h"

extern float x0;
extern float xf;
extern int dec;
extern int Nline;
extern double sector;
extern int mode_RP; //0 for measuring raw data and sent int16_t, 1 for measuring envelope ans send char
extern int step; //0 no stepper plug to RedPitaya, 1 drive stepper

typedef struct data data;

void init_data(data *data_RP, unsigned int Nmax, int PORT, float level0, float levelf, mode stepper_mode);
void clear_data(data *data_RP);
void init_ramp(int trigger, float level0, float x0, float levelf, float xf); //if trigger==1 ramp is made on trigger else ramp is made with function ramp(), ramp is level0 high at x0 and levelf high at xf
void ramp();
void end_ramp();
void init_variable(float x0, float xf, int dec, uint32_t *bufferlength, int *delay);
void set_acquisition(int dec);
void trigg();
void on_trigger_acquisition(float *buffer_float, uint32_t buffer_length);
void on_trigger_acquisition_int16(float *buffer_float, uint32_t buffer_length);
void init_RP();
void close_RP();
void send_via_tcp(int line, char *buffer_char, float *buffer_float, uint32_t buffer_length, client *client_list);
void send_int16_via_tcp(int line, int16_t *buffer_int16, float *buffer_float, uint32_t buffer_length, client *client_list);
void send_to_server(int line, char *buffer_char, float *buffer_float, uint32_t buffer_length, SOCKET *sock);
void send_int16_to_server(int line, int16_t *buffer_int16, float *buffer_float, uint32_t buffer_length, SOCKET *sock);
void init_pulse();
void pulse();
void external_trigger_acquisition_TCP(data *data_RP, int line); //acquire and send data on external trigger
void internal_trigger_acquisition_TCP(data *data_RP, int line); //acquire and send data on "internal" trigger
void internal_trigger_acquisition_server(data *data_RP);

struct data
{
	uint32_t buffer_length;
	int delay;
	SOCKET sock;
	client *client_list;
	stepper_motor *stepper;
	mode stepper_mode;
	double angle;
	char *buffer_char;
	float *buffer_float;
	int16_t *buffer_int16;
};

void init_data(data *data_RP, unsigned int Nmax, int PORT, float level0, float levelf, mode stepper_mode)
{
	//init RedPitaya and variables (buffer_length, delay)
	init_RP();
	init_variable(x0, xf, dec, &(data_RP->buffer_length), &(data_RP->delay));
	init_ramp(1, level0, x0, levelf, xf);
	set_acquisition(dec);
	data_RP->buffer_float=(float *)malloc((int)data_RP->buffer_length*sizeof(float));
	if (mode_RP==0)
	{
		data_RP->buffer_int16=(int16_t *)malloc((int)data_RP->buffer_length*sizeof(int16_t));
	}
	else
	{
		data_RP->buffer_char=(char *)malloc((int)data_RP->buffer_length*sizeof(char));
	}

	//init server
	data_RP->client_list=(client *)malloc(sizeof(client));
	init_TCP_server(&(data_RP->sock), PORT, data_RP->client_list, Nmax);
	launch_server(&data_RP->sock, data_RP->client_list);

	//init stepper
	if (step==1)
	{
		data_RP->stepper=(stepper_motor *)malloc(sizeof(stepper_motor));
		init_stepper(data_RP->stepper);
		set_mode(data_RP->stepper, data_RP->stepper_mode);
		init_position(data_RP->stepper, 150.0-sector/2.0);
	}
}

void clear_data(data *data_RP)
{
	//clear stepper
	if (step==1)
	{
		disable_stepper(data_RP->stepper);
		free(data_RP->stepper);
	}

	//close clear server
	close_TCP_server(&data_RP->sock, data_RP->client_list);
	free(data_RP->client_list);

	//close clear RP
	free(data_RP->buffer_float);
	if (mode_RP==0)	{free(data_RP->buffer_int16);}
	else {free(data_RP->buffer_char);}
	end_ramp();
	close_RP();
}

void init_ramp(int trigger, float level0, float x0, float levelf, float xf) 
{
	int i=0;
	uint32_t length=16384;
	float tmp=0.0, xmax=0.2;
	float *homeramp;
	homeramp=(float *)malloc(length*sizeof(float));
	x0=x0/1000;
	xf=xf/1000;

	if(level0<0.0){level0=0.0;}
	if(levelf>1.0){levelf=1.0;}
	if(x0<0.0){x0=0.0;}
	if(xf>xmax){xf=xmax;}

	float frequency=1480.0/xmax/2.0; //factor 2 for back and forth
	float lsamp=xmax/((double)length); //"length" sampling
	int N0=(int)(x0/lsamp); //x0 position
	int Nf=(int)(xf/lsamp); //xf position
	float slope=(levelf-level0)/((double)Nf-(double)N0); //slope of the ramp
	float initlevel=level0-slope*((double)N0); //level at first point

	//definition of the ramp
	for (i=0 ; i<length ; i++)
	{
		if (i<Nf)
		{
			tmp=slope*((double)i)+initlevel;
			if (tmp<0.0){tmp=0.0;}
			homeramp[i]=tmp;
		}
		else {homeramp[i]=levelf;}
	}

	rp_GenWaveform(RP_CH_1, RP_WAVEFORM_ARBITRARY);
	rp_GenArbWaveform(RP_CH_1, homeramp, length);
	rp_GenMode(RP_CH_1, RP_GEN_MODE_BURST);
	rp_GenBurstCount(RP_CH_1, 1);
	rp_GenAmp(RP_CH_1, 1.0);
	rp_GenFreq(RP_CH_1, frequency);

	if (trigger==1)
	{	
		rp_GenTriggerSource(RP_CH_1,RP_TRIG_SRC_CHA_PE); //to trigger on external trigger must put RP_TRIG_SRC_CHA_PE not RP_TRIG_SRC_EXT_PE...
		rp_GenOutEnable(RP_CH_1);
	}

	free(homeramp);
}

void ramp()
{
	rp_GenOutEnable(RP_CH_1);
}

void end_ramp() {rp_GenOutDisable(RP_CH_1);}

void init_variable(float x0, float xf, int dec, uint32_t *bufferlength, int *delay)
{
	float scale=2.0*125.0/1.48/((float)dec); //factor 2.0 for back and forth
	*bufferlength=(uint32_t)(scale*(xf-x0));
	if (*bufferlength>16384){*bufferlength=16384;}
	int Nf=0;
	Nf=(int)(scale*xf);
	*delay=Nf-8192;
}

void set_acquisition(int dec) 
{
	if (dec==1)
	{
		rp_AcqSetDecimation(RP_DEC_1);
	}
	else if (dec==8)
	{
		rp_AcqSetDecimation(RP_DEC_8);
		rp_AcqSetAveraging(1);
	}
	else
	{
		rp_AcqSetDecimation(RP_DEC_64);
		rp_AcqSetAveraging(1);
	}
	
	rp_AcqSetGain(RP_CH_1,RP_HIGH); 	
	rp_AcqSetGain(RP_CH_2,RP_HIGH);
	//rp_AcqSetGain(RP_CH_1,RP_LOW);
        //rp_AcqSetGain(RP_CH_2,RP_LOW);

}

void trigg(int delay) 
{
	rp_AcqStart();
	rp_AcqSetTriggerSrc(RP_TRIG_SRC_EXT_PE);
	rp_AcqSetTriggerDelay(delay); 
}

void on_trigger_acquisition(float *buffer_float, uint32_t buffer_length)
{
	rp_acq_trig_state_t state = RP_TRIG_STATE_WAITING;
	while(1){
		rp_AcqGetTriggerState(&state);
		if(state == RP_TRIG_STATE_TRIGGERED){
			break;
		}
	}
	rp_AcqGetLatestDataV(RP_CH_1, &buffer_length, buffer_float);
}

void on_trigger_acquisition_int16(float *buffer_float, uint32_t buffer_length)
{
	rp_acq_trig_state_t state = RP_TRIG_STATE_WAITING;
	while(1){
		rp_AcqGetTriggerState(&state);
		if(state == RP_TRIG_STATE_TRIGGERED){
			break;
		}
	}
	rp_AcqGetLatestDataV(RP_CH_2, &buffer_length, buffer_float);
}

void init_RP()
{
	if(rp_Init() != RP_OK)
	{
	printf("Rp api init failed!\n");
	}
	init_pulse();
}

void close_RP()
{
	rp_Release();
}

void send_via_tcp(int line, char *buffer_char, float *buffer_float, uint32_t buffer_length, client *client_list)
{
	int i=0;
	float tmpf, scale=0.9, offset=71.0; //depend on maximum value of input, for test scale=1
	int tmp;
	buffer_char[0]=(char)line;
	for (i=0 ; i<buffer_length ; i++) 
	{
		tmpf=255.0/scale*buffer_float[i]+offset;
		tmp=(int)tmpf;
		if (tmp<0) {tmp=-tmp;}
		if (tmp==0) {tmp=1;}
		if (tmp>255) {tmp=255;}
		buffer_char[i+1]=(char)tmp;
	}
	send_TCP_server(client_list, buffer_char, (int)buffer_length+1, -1);
}

void send_int16_via_tcp(int line, int16_t *buffer_int16, float *buffer_float, uint32_t buffer_length, client *client_list)
{
	int i=0;

	buffer_int16[0]=(int16_t)line;
	for (i=0 ; i<(int)buffer_length ; i++)
	{
		buffer_int16[i+1]=(int16_t)(buffer_float[i]/20.0*8192.0);
	}

	send_int16_TCP_server(client_list, buffer_int16, (int)buffer_length+1, -1);
}

void send_to_server(int line, char *buffer_char, float *buffer_float, uint32_t buffer_length, SOCKET *sock)
{
	int i=0;
	float tmpf, scale=0.9; //depend on maximum value of input, for test scale=1
	int tmp;
	buffer_char[0]=(char)line;
	char tmp_buffer[(int)buffer_length];
	for (i=0 ; i<buffer_length ; i++) 
	{
		tmpf=255.0/scale*buffer_float[i]+75.0;
		tmp=(int)tmpf;
		if (tmp<0) {tmp=-tmp;}
		if (tmp==0) {tmp=1;}
		if (tmp>255) {tmp=255;}
		buffer_char[i+1]=(char)tmp;
		tmp_buffer[i]=(char)tmp;
	}
	//send_TCP_client(sock, buffer_char, (int)buffer_length+1);
	send_TCP_client(sock, tmp_buffer, (int)buffer_length);
}

void send_int16_to_server(int line, int16_t *buffer_int16, float *buffer_float, uint32_t buffer_length, SOCKET *sock)
{
	int i=0;
	int16_t tmp_buffer[(int)buffer_length*2];
	for (i=0 ; i<(int)buffer_length ; i++)
	{
		tmp_buffer[i]=(int16_t)(buffer_float[i]/20.0)*8192;
	}

	send_int16_TCP_client(sock, tmp_buffer, (int)buffer_length);
}

void init_pulse()
{
	rp_DpinSetDirection(RP_DIO7_N,1);
	rp_DpinSetState(RP_DIO7_N,0);
}

void pulse()
{
	rp_pinState_t state=0;
	rp_DpinGetState(RP_DIO7_N, &state);
	rp_DpinSetState(RP_DIO7_N, !state);
}

void external_trigger_acquisition_TCP(data *data_RP, int line)
{
	trigg(data_RP->delay);
	if (mode_RP==0)
	{
		on_trigger_acquisition_int16(data_RP->buffer_float, data_RP->buffer_length);
		send_int16_via_tcp(line, data_RP->buffer_int16, data_RP->buffer_float, data_RP->buffer_length, data_RP->client_list);
	}
	else 
	{
		on_trigger_acquisition(data_RP->buffer_float, data_RP->buffer_length);
		send_via_tcp(line, data_RP->buffer_char, data_RP->buffer_float, data_RP->buffer_length, data_RP->client_list);
	}
}

void internal_trigger_acquisition_TCP(data *data_RP, int line)
{
        trigg(data_RP->delay);
	pulse();
        on_trigger_acquisition(data_RP->buffer_float, data_RP->buffer_length);
        if (mode_RP==0)
	{	
		on_trigger_acquisition_int16(data_RP->buffer_float, data_RP->buffer_length);
		send_int16_via_tcp(line, data_RP->buffer_int16, data_RP->buffer_float, data_RP->buffer_length, data_RP->client_list);
	}
        else 
	{
		on_trigger_acquisition(data_RP->buffer_float, data_RP->buffer_length);
		send_via_tcp(line, data_RP->buffer_char, data_RP->buffer_float, data_RP->buffer_length, data_RP->client_list);
	}
	pulse();
}

void internal_trigger_acquisition_server(data *data_RP)
{
        trigg(data_RP->delay);
	pulse();
        on_trigger_acquisition(data_RP->buffer_float, data_RP->buffer_length);
        if (mode_RP==0)
        {	
		on_trigger_acquisition_int16(data_RP->buffer_float, data_RP->buffer_length);
		send_int16_to_server(0, data_RP->buffer_int16, data_RP->buffer_float, data_RP->buffer_length, &data_RP->sock);
	}
        else 
	{
		on_trigger_acquisition(data_RP->buffer_float, data_RP->buffer_length);
		send_to_server(0, data_RP->buffer_char, data_RP->buffer_float, data_RP->buffer_length, &data_RP->sock);
	}
	pulse();
}

#endif
