#ifndef STEPPER_H
#define STEPPER_H

#include<unistd.h>

typedef struct stepper_motor stepper_motor; //so we can call stepper_motor and not struct stepper_motor
typedef enum mode mode;
typedef enum sens sens;

//functions to adapte depending on the hardware
void init_gpio(int pin, int value); //define gpio as input or output
void set_gpio(int pin, int value); //set value high or low to gpio pin
void wait(int time); //wait time in us

void init_stepper(stepper_motor* stepper); //initialyse the structure stepper_motor
void enable_stepper(stepper_motor* stepper);
void disable_stepper(stepper_motor* stepper);
void set_mode(stepper_motor* stepper, mode step_size); //set mode of the stepper : full, 1/2, 1/4, 1/8, 1/16 step
int half_step_time(stepper_motor* stepper, double* speed); //give time of a half step depending on the mode and speed in tour per second, note that this time is in microseconds and must be greater than 1 us. It change the speed to its real value
int step_number(stepper_motor* stepper, double* angle); //give the number of step to do in order to move to a given angle, if angle is not a multiple of the angle of a step, angle is change to its correct value
void move(stepper_motor* stepper, double* angle, double* speed, sens dir); //move the stepper of a given angle with a given speed on sens dir. It returns the real mooved angle and speed
void init_position(stepper_motor* stepper, double angle); //initialize the stepper position by going to the mecanic stop (sens2) and then move to the given angle (sens1)

enum mode
{
	full,full_2,full_4,full_8,full_16
};

enum sens
{
	sens1,sens2
};

struct stepper_motor
{
	int pin_en;
	int pin_ms1;
	int pin_ms2;
	int pin_ms3;
	int pin_step;
	int pin_dir;
	mode step_size;
};

void init_gpio(int pin, int value)
{
	rp_DpinSetDirection(pin, value);
}


void set_gpio(int pin, int value)
{
	rp_DpinSetState(pin,value);
}

void wait(int time)
{
	usleep((unsigned int)time);
}


void init_stepper(stepper_motor* stepper)
{
	stepper->pin_en=RP_DIO5_N;
	stepper->pin_ms1=RP_DIO4_N;
	stepper->pin_ms2=RP_DIO3_N;
	stepper->pin_ms3=RP_DIO2_N;
	stepper->pin_step=RP_DIO1_N;
	stepper->pin_dir=RP_DIO0_N;
	stepper->step_size=full;

	init_gpio(stepper->pin_en,1);
	init_gpio(stepper->pin_ms1,1);
	init_gpio(stepper->pin_ms2,1);
	init_gpio(stepper->pin_ms3,1);
	init_gpio(stepper->pin_step,1);
	init_gpio(stepper->pin_dir,1);


	set_gpio(stepper->pin_en,1);
	set_gpio(stepper->pin_ms1,0);
	set_gpio(stepper->pin_ms2,0);
	set_gpio(stepper->pin_ms3,0);
	set_gpio(stepper->pin_step,0);
	set_gpio(stepper->pin_dir,0);
}

void enable_stepper(stepper_motor* stepper)
{
	set_gpio(stepper->pin_en,0);
}

void disable_stepper(stepper_motor* stepper)
{
	set_gpio(stepper->pin_en,1);
}

void set_mode(stepper_motor* stepper, mode step_size)
{
	if (step_size!=stepper->step_size)
	{
		stepper->step_size=step_size;

		if (step_size==full)
		{
			set_gpio(stepper->pin_ms1,0);
			set_gpio(stepper->pin_ms2,0);
			set_gpio(stepper->pin_ms3,0);
		}
		if (step_size==full_2)
		{
			set_gpio(stepper->pin_ms1,1);
			set_gpio(stepper->pin_ms2,0);
			set_gpio(stepper->pin_ms3,0);
		}
		if (step_size==full_4)
		{
			set_gpio(stepper->pin_ms1,0);
			set_gpio(stepper->pin_ms2,1);
			set_gpio(stepper->pin_ms3,0);
		}
		if (step_size==full_8)
		{
			set_gpio(stepper->pin_ms1,1);
			set_gpio(stepper->pin_ms2,1);
			set_gpio(stepper->pin_ms3,0);
		}
		if (step_size==full_16)
		{
			set_gpio(stepper->pin_ms1,1);
			set_gpio(stepper->pin_ms2,1);
			set_gpio(stepper->pin_ms3,1);
		}
	}
}

int half_step_time(stepper_motor* stepper, double* speed)
{
	int Nstep=0;
	double time;

	switch (stepper->step_size)
	{
		case full :
			Nstep=400;
			break;
		case full_2 :
			Nstep=400*2;
			break;
		case full_4 :
			Nstep=400*4;
			break;
		case full_8 :
			Nstep=400*8;
			break;
		case full_16 :
			Nstep=400*16;
			break;
	}

	time=(double)(1000000/Nstep)/(*speed)/2.0; //time in us 
	if (time<1.0){time=1.0;}
	(*speed)=(double)(500000/Nstep)/time;
	return (int)time;
}

int step_number(stepper_motor* stepper, double* angle)
{
	double minimum_angle=0;
	int  Nstep;

	switch (stepper->step_size)
	{
		case full :
			minimum_angle=360.0/400.0;
			break;
		case full_2 :
			minimum_angle=360.0/(400.0*2.0);
			break;
		case full_4 :
			minimum_angle=360.0/(400.0*4.0);
			break;
		case full_8 :
			minimum_angle=360.0/(400.0*8.0);
			break;
		case full_16 :
			minimum_angle=360.0/(400.0*16.0);
			break;
	}

	if ((*angle)<minimum_angle){(*angle)=minimum_angle;}	
	Nstep=(int)((*angle)/minimum_angle);
	(*angle)=(double)(Nstep)*minimum_angle;

	return Nstep;
}

void move(stepper_motor* stepper, double* angle, double* speed, sens dir)
{
	int Nstep, half_time;
	int i;

	set_gpio(stepper->pin_dir,dir);
	half_time=half_step_time(stepper, speed);
	Nstep=step_number(stepper, angle);

	for (i=0 ; i<Nstep ; i++)
	{
		set_gpio(stepper->pin_step,1);
		wait(half_time);
		set_gpio(stepper->pin_step,0);
		wait(half_time);
	}
}

void init_position(stepper_motor* stepper, double angle)
{
	double tour=360.0, speed=3.0;

	enable_stepper(stepper);
	wait(100);
	move(stepper, &tour, &speed, sens2);
	wait(100);
	move(stepper, &angle, &speed, sens1);

}

#endif
