/**
 * @file scan_conversion_float.cpp
 * @author  GG23800 <jerome@echopen.org>
 * @version 1.0
 *
 *
 * @section LICENSE
 *
 * Copyright (c) 2014, echOpen All rights reserved.
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * Neither the name of echopen nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *
 * @section DESCRIPTION
 *
 *
 */

#include<math.h>
#include<stdio.h>
#include<stdlib.h>

#include"scan_conversion_float.hpp"
#include"pointer_management.hpp"
//x_y_tensor
void create_x_y_tensor (x_y_tensor *xyt, int Nxt, int Nyt)
{
	xyt->Nx=Nxt;
	xyt->Ny=Nyt;

	create_vector_float(&xyt->x_vector,Nxt);
	create_vector_float(&xyt->y_vector,Nyt);
	create_matrix_int(&xyt->indicial_x_y_tmp,2,Nxt*Nyt);
	create_matrix_int(&xyt->indicial_x_y_out_tmp,2,Nxt*Nyt);
}

void fill_x_y_vector (scan_conv *scan_conv_struct)
{
	int i=0;
	float half_sec=scan_conv_struct->sector/2.0;
	float x0=-scan_conv_struct->polar_tensor->Rf*sin(half_sec);
	float xf= -x0;
	float delta_x=(2.0*xf)/(((float)scan_conv_struct->Nx)-1.0);
	float y0=scan_conv_struct->polar_tensor->R0*cos(half_sec);
	float yf=scan_conv_struct->polar_tensor->Rf;
	float delta_y=(yf-y0)/(((float)scan_conv_struct->Ny)-1.0);

	scan_conv_struct->cartesian_tensor->x_vector[0]=x0;
	scan_conv_struct->cartesian_tensor->y_vector[0]=y0;
	if (scan_conv_struct->Nx<=scan_conv_struct->Ny)
	{
		for (i=1 ; i<scan_conv_struct->Nx-1 ; i++)
		{
			scan_conv_struct->cartesian_tensor->x_vector[i]=scan_conv_struct->cartesian_tensor->x_vector[i-1]+delta_x;
			scan_conv_struct->cartesian_tensor->y_vector[i]=scan_conv_struct->cartesian_tensor->y_vector[i-1]+delta_y;
		}
		for (i=scan_conv_struct->Nx-1 ; i<scan_conv_struct->Ny-1 ; i++)
		{
			scan_conv_struct->cartesian_tensor->y_vector[i]=scan_conv_struct->cartesian_tensor->y_vector[i-1]+delta_y;
		}
	}
	else
	{
		for (i=1 ; i<scan_conv_struct->Ny-1 ; i++)
		{
			scan_conv_struct->cartesian_tensor->x_vector[i]=scan_conv_struct->cartesian_tensor->x_vector[i-1]+delta_x;
			scan_conv_struct->cartesian_tensor->y_vector[i]=scan_conv_struct->cartesian_tensor->y_vector[i-1]+delta_y;
		}
		for (i=scan_conv_struct->Ny-1 ; i<scan_conv_struct->Nx-1 ; i++)
		{
			scan_conv_struct->cartesian_tensor->x_vector[i]=scan_conv_struct->cartesian_tensor->x_vector[i-1]+delta_x;
		}
	}
	scan_conv_struct->cartesian_tensor->x_vector[scan_conv_struct->Nx-1]=xf;
	scan_conv_struct->cartesian_tensor->y_vector[scan_conv_struct->Ny-1]=yf;
}

void resize_x_y_tensor (x_y_tensor *xyt, int Nxt, int Nyt)
{
	int Nyold=xyt->Nx*xyt->Ny;
	xyt->Nx=Nxt;
	xyt->Ny=Nyt;

	resize_vector_float(&xyt->x_vector,Nxt);
	resize_vector_float(&xyt->y_vector,Nyt);
	resize_matrix_int(&xyt->indicial_x_y_tmp,2,Nxt*Nyt,2,Nyold);
	resize_matrix_int(&xyt->indicial_x_y_out_tmp,2,Nxt*Nyt,2,Nyold);
}

void clear_x_y_tensor (x_y_tensor *xyt)
{
	delete_vector_float(&xyt->x_vector);
	delete_vector_float(&xyt->y_vector);
	delete_matrix_int(&xyt->indicial_x_y_tmp,2);
	delete_matrix_int(&xyt->indicial_x_y_out_tmp,2);
}

//r_theta_tensor
void create_r_theta_tensor (r_theta_tensor *rtt, sc_settings *settings, float R0t, float Rft, int Nrt, int Nlt, int Nxt, int Nyt)
{
	rtt->R0=R0t;
	rtt->Rf=Rft;
	rtt->Nr=Nrt;
	rtt->Nl=Nlt;
	rtt->Nx=Nxt;
	rtt->Ny=Nyt;
    rtt->option_sc=settings->option_sc;

	//prevoir un option pour "classique" pour r_vector un vecteur
	//"linear simplify" pour r_vector une matice Nline*Nr
	//"real" pour r_vector une matrice Nline*Nr et theta_vector une matrice Nline*Nr

	switch (settings->option_sc)
	{
		case 0 :
			create_matrix_float(&rtt->r_vector, 1, rtt->Nr);
			create_matrix_float(&rtt->theta_vector, rtt->Nl, 1);
			break;
		case 1 :
			create_matrix_float(&rtt->r_vector, rtt->Nl, rtt->Nr);
			create_matrix_float(&rtt->theta_vector, rtt->Nl, 1);
			break;
		case 2 : // identical to case 1
			create_matrix_float(&rtt->r_vector, rtt->Nl, rtt->Nr);
			create_matrix_float(&rtt->theta_vector, rtt->Nl, 1);
			break;
		case 3 :
			create_matrix_float(&rtt->r_vector, rtt->Nl, rtt->Nr);
			create_matrix_float(&rtt->theta_vector, rtt->Nl, rtt->Nr);
			break;
	}

	create_matrix_float(&rtt->r_matrix,rtt->Nx,rtt->Ny);
	create_matrix_float(&rtt->theta_matrix,rtt->Nx,rtt->Ny);
}

void fill_r_theta_vectors (scan_conv *scan_conv_struct)
{
	int i=0;
	float delta_r=scan_conv_struct->settings->speed*scan_conv_struct->settings->decimation/2.f/scan_conv_struct->settings->sampling_rate/1000.f; //with /1000 delta_r is in mm
	scan_conv_struct->settings->dr=delta_r;
	float rstart=2.f*scan_conv_struct->settings->delay*scan_conv_struct->settings->speed/1000.f; //1000 to have rmin in mm
	float delta_theta=scan_conv_struct->sector/(((float)scan_conv_struct->Nline)-1.f);
	float half_sec=scan_conv_struct->sector/2.f;

	switch (scan_conv_struct->settings->option_sc)
	{
		case 0 :
			scan_conv_struct->polar_tensor->r_vector[0][0]=rstart-scan_conv_struct->settings->hp-scan_conv_struct->settings->h0;
			if (scan_conv_struct->polar_tensor->r_vector[0][0]>scan_conv_struct->polar_tensor->R0) {printf("Warning R0 out of bound, too small\n");}
			for (i=1 ; i<scan_conv_struct->Nr ; i++) {scan_conv_struct->polar_tensor->r_vector[0][i]=scan_conv_struct->polar_tensor->r_vector[0][i-1]+delta_r;}
			if (scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->Nr-1]<scan_conv_struct->polar_tensor->Rf) {printf("Warning Rf out of bound, too high\n");}

			for (i=0 ; i<scan_conv_struct->Nline ; i++) {scan_conv_struct->polar_tensor->theta_vector[i][0]=scan_conv_struct->settings->ti[i];}
			break;

		case 1 :
		for (int j=0 ; j<scan_conv_struct->settings->Nl ; j++)
		{
				scan_conv_struct->polar_tensor->r_vector[j][0]=rstart-scan_conv_struct->settings->hp-scan_conv_struct->settings->ri[j];
				if (scan_conv_struct->polar_tensor->r_vector[j][0]>scan_conv_struct->polar_tensor->R0) {printf("Warning R0 out of bound, too small for line %i\n",j);}
				for (i=1 ; i<scan_conv_struct->Nr ; i++) {scan_conv_struct->polar_tensor->r_vector[j][i]=scan_conv_struct->polar_tensor->r_vector[j][i-1]+delta_r;}
				if (scan_conv_struct->polar_tensor->r_vector[j][scan_conv_struct->Nr-1]<scan_conv_struct->polar_tensor->Rf) {printf("Warning Rf out of bound, too high for line %i\n", j);}

				for (i=0 ; i<scan_conv_struct->Nline ; i++) {scan_conv_struct->polar_tensor->theta_vector[i][0]=scan_conv_struct->settings->ti[i];}
		}
			break;

		case 2 : //identical to case 1
		for (int j=0 ; j<scan_conv_struct->settings->Nl ; j++)
		{
				scan_conv_struct->polar_tensor->r_vector[j][0]=rstart-scan_conv_struct->settings->hp-scan_conv_struct->settings->ri[j];
				if (scan_conv_struct->polar_tensor->r_vector[j][0]>scan_conv_struct->polar_tensor->R0) {printf("Warning R0 out of bound, too small for line %i\n",j);}
				for (i=1 ; i<scan_conv_struct->Nr ; i++) {scan_conv_struct->polar_tensor->r_vector[j][i]=scan_conv_struct->polar_tensor->r_vector[j][i-1]+delta_r;}
				if (scan_conv_struct->polar_tensor->r_vector[j][scan_conv_struct->Nr-1]<scan_conv_struct->polar_tensor->Rf) {printf("Warning Rf out of bound, too high for line %i\n", j);}

				for (i=0 ; i<scan_conv_struct->Nline ; i++) {scan_conv_struct->polar_tensor->theta_vector[i][0]=scan_conv_struct->settings->ti[i];}
		}
			break;

		case 3 :
			break;
	}
}

void fill_r_theta_matrices (scan_conv *scan_conv_struct)
{
	int i=0, j=0, N_tmp=0, N_out_tmp=0;
	float half_sec=scan_conv_struct->sector/2;


    if (scan_conv_struct->settings->option_sc==2) //special test for mecanic version 3, due to local deformation, may be delete after tests because we won't image at this position where local effects appears, change of r and theta must not be cleared!!!!
    {
        float x=0.f, y=0.f;
        for (j=0 ; j<scan_conv_struct->Ny ; j++)
        {
            for (i=0 ; i<scan_conv_struct->Nx ; i++)
            {
                scan_conv_struct->polar_tensor->r_matrix[i][j]=sqrt(scan_conv_struct->cartesian_tensor->x_vector[i]*scan_conv_struct->cartesian_tensor->x_vector[i]+scan_conv_struct->cartesian_tensor->y_vector[j]*scan_conv_struct->cartesian_tensor->y_vector[j]);
                scan_conv_struct->polar_tensor->theta_matrix[i][j]=-atan(scan_conv_struct->cartesian_tensor->x_vector[i]/scan_conv_struct->cartesian_tensor->y_vector[j]);
                if (scan_conv_struct->polar_tensor->r_matrix[i][j]>scan_conv_struct->polar_tensor->R0 && scan_conv_struct->polar_tensor->r_matrix[i][j]<scan_conv_struct->polar_tensor->Rf && scan_conv_struct->polar_tensor->theta_matrix[i][j]>-half_sec && scan_conv_struct->polar_tensor->theta_matrix[i][j]<half_sec)
                {
                    scan_conv_struct->cartesian_tensor->indicial_x_y_tmp[0][N_tmp]=i;
                    scan_conv_struct->cartesian_tensor->indicial_x_y_tmp[1][N_tmp]=j;
                    N_tmp++;
                }
                else
                {
                    x=scan_conv_struct->polar_tensor->r_matrix[i][j]*cos(scan_conv_struct->polar_tensor->theta_matrix[i][j]);
                    y=scan_conv_struct->polar_tensor->r_matrix[i][j]*cos(scan_conv_struct->polar_tensor->theta_matrix[i][j]);
                    if (((x+scan_conv_struct->settings->x0)*(x+scan_conv_struct->settings->x0)+y*y > scan_conv_struct->settings->rtang*scan_conv_struct->settings->rtang*1.01) && (fabsf(y) < 2) && (scan_conv_struct->settings->l1/fabsf(scan_conv_struct->settings->l1)*x <= 0.f) && (fabsf(x) < fabs(scan_conv_struct->settings->x0))) // useful only if we look close to crossing point, very local exception points
                    {
                        scan_conv_struct->cartesian_tensor->indicial_x_y_tmp[0][N_tmp]=i;
                        scan_conv_struct->cartesian_tensor->indicial_x_y_tmp[1][N_tmp]=j;
                        N_tmp++;
                    }
                    else
                    {
                        scan_conv_struct->cartesian_tensor->indicial_x_y_out_tmp[0][N_out_tmp]=i;
                        scan_conv_struct->cartesian_tensor->indicial_x_y_out_tmp[1][N_out_tmp]=j;
                        N_out_tmp++;
                    }
                }
            }
        }

        float t1=0.f, t2=0.f, theta1=0.f, theta2=0.f, theta=0.f;
        float dr=scan_conv_struct->settings->rtang, x0l=scan_conv_struct->settings->x0;

        //change real grid for model grid
        for (j=0 ; j<scan_conv_struct->Ny ; j++)
        {
            for (i=0 ; i<scan_conv_struct->Nx ; i++)
            {
                y=scan_conv_struct->polar_tensor->r_matrix[i][j]*cos(scan_conv_struct->polar_tensor->theta_matrix[i][j]);
                x=scan_conv_struct->polar_tensor->r_matrix[i][j]*sin(scan_conv_struct->polar_tensor->theta_matrix[i][j]);
                t1=(y+sqrtf(x*x+y*y-dr*dr+x0l*x0l+2.f*x0l*x))/(-dr-x-x0l);
                t2=(y-sqrtf(x*x+y*y-dr*dr+x0l*x0l+2.f*x0l*x))/(-dr-x-x0l);
                theta1=2.f*atanf(t1);
                theta2=2.f*atanf(t2);
                if (fabsf(theta1)<=scan_conv_struct->settings->thetamax)
                {
                    if(fabsf(theta2)<=scan_conv_struct->settings->thetamax)
                    {
                        if (scan_conv_struct->settings->l1<0)
                        {
                            if (theta1<theta2) {theta=theta1;}
                            else {theta=theta2;}
                        }
                        else
                        {
                            if (theta1<theta2) {theta=theta2;}
                            else {theta=theta1;}
                        }
                    }
                    else {theta=theta1;}

                    x+=x0l-dr/cosf(theta);
                    scan_conv_struct->polar_tensor->r_matrix[i][j]=sqrtf(x*x+y*y);
                    scan_conv_struct->polar_tensor->theta_matrix[i][j]=atanf(x/y);
                }
                else if(fabsf(theta2)<=scan_conv_struct->settings->thetamax)
                {
                    theta=theta2;

                    x-=x0l-dr/cosf(theta);
                    scan_conv_struct->polar_tensor->r_matrix[i][j]=sqrtf(x*x+y*y);
                    scan_conv_struct->polar_tensor->theta_matrix[i][j]=atanf(x/y);
                }
                else
                {
                    scan_conv_struct->polar_tensor->r_matrix[i][j]=0.f;
                    scan_conv_struct->polar_tensor->theta_matrix[i][j]=0.f;
                }
            }
        }
    }
    else
    {
        for (j=0 ; j<scan_conv_struct->Ny ; j++)
        {
            for (i=0 ; i<scan_conv_struct->Nx ; i++)
            {
                scan_conv_struct->polar_tensor->r_matrix[i][j]=sqrt(scan_conv_struct->cartesian_tensor->x_vector[i]*scan_conv_struct->cartesian_tensor->x_vector[i]+scan_conv_struct->cartesian_tensor->y_vector[j]*scan_conv_struct->cartesian_tensor->y_vector[j]);
                scan_conv_struct->polar_tensor->theta_matrix[i][j]=atan(scan_conv_struct->cartesian_tensor->x_vector[i]/scan_conv_struct->cartesian_tensor->y_vector[j]);
                if (scan_conv_struct->polar_tensor->r_matrix[i][j]<scan_conv_struct->polar_tensor->R0 || scan_conv_struct->polar_tensor->r_matrix[i][j]>scan_conv_struct->polar_tensor->Rf || scan_conv_struct->polar_tensor->theta_matrix[i][j]<-half_sec || scan_conv_struct->polar_tensor->theta_matrix[i][j]>half_sec)
                {
                    scan_conv_struct->cartesian_tensor->indicial_x_y_out_tmp[0][N_out_tmp]=i;
                    scan_conv_struct->cartesian_tensor->indicial_x_y_out_tmp[1][N_out_tmp]=j;
                    N_out_tmp++;
                }
                else
                {
                    scan_conv_struct->cartesian_tensor->indicial_x_y_tmp[0][N_tmp]=i;
                    scan_conv_struct->cartesian_tensor->indicial_x_y_tmp[1][N_tmp]=j;
                    N_tmp++;
                }
            }
        }
    }
	scan_conv_struct->N_point_to_change=N_tmp;
	scan_conv_struct->N_out=scan_conv_struct->Nx*scan_conv_struct->Ny-N_tmp;
}

void resize_r_theta_tensor (r_theta_tensor *rtt, sc_settings *settings, float R0t, float Rft, int Nrt, int Nlt, int Nxt, int Nyt)
{
	int Nxold=rtt->Nx, Nyold=rtt->Ny;
	int Nrold=rtt->Nr, Nlold=rtt->Nl;

	rtt->R0=R0t;
	rtt->Rf=Rft;
	rtt->Nr=Nrt;
	rtt->Nl=Nlt;
	rtt->Nx=Nxt;
	rtt->Ny=Nyt;

	int nxr=0, nyt=0;
	switch (rtt->option_sc)
	{
		case 0:
			nxr=1;
			nyt=1;
			break;
		case 1:
			nxr=Nrold;
			nyt=1;
			break;
		case 2:
			nxr=Nrold;
			nyt=1;
			break;
		case 3:
			nxr=Nrold;
			nyt=Nlold;
			break;
	}

	rtt->option_sc=settings->option_sc;

	switch (settings->option_sc)
	{
		case 0 :
			resize_matrix_float(&rtt->r_vector, 1, rtt->Nr, nxr, Nrold);
			resize_matrix_float(&rtt->theta_vector,rtt->Nl, 1, Nlold, nyt);
			break;
		case 1 :
			resize_matrix_float(&rtt->r_vector, rtt->Nl, rtt->Nr, nxr, Nrold);
			resize_matrix_float(&rtt->theta_vector,rtt->Nl, 1, Nlold, nyt);
			break;
		case 2 :
			resize_matrix_float(&rtt->r_vector, rtt->Nl, rtt->Nr, nxr, Nrold);
			resize_matrix_float(&rtt->theta_vector,rtt->Nl, 1, Nlold, nyt);
			break;
		case 3 :
			resize_matrix_float(&rtt->r_vector, rtt->Nl, rtt->Nr, nxr, Nrold);
			resize_matrix_float(&rtt->theta_vector,rtt->Nl, rtt->Nr, Nlold, nyt);
			break;
	}

	resize_matrix_float(&rtt->r_matrix,rtt->Nx,rtt->Ny, Nxold, Nyold);
	resize_matrix_float(&rtt->theta_matrix,rtt->Nx,rtt->Ny, Nxold, Nyold);
}

void clear_r_theta_tensor(r_theta_tensor *rtt)
{
	switch (rtt->option_sc)
	{
		case 0 :
			delete_matrix_float(&rtt->r_vector, 1);
			delete_matrix_float(&rtt->theta_vector, rtt->Nl);
			break;
		default :
			delete_matrix_float(&rtt->r_vector, rtt->Nl);
			delete_matrix_float(&rtt->theta_vector, rtt->Nl);
			break;
	}

	delete_matrix_float(&rtt->r_matrix,rtt->Nx);
	delete_matrix_float(&rtt->theta_matrix,rtt->Nx);
}

void create_struct_sc_settings (sc_settings *set, int Nl)
{
	create_vector_float(&set->xi, Nl);
    create_vector_float(&set->yi, Nl);
	create_vector_float(&set->ri, Nl);
	create_vector_float(&set->ti, Nl);
	set->Nl=Nl;
	if (Nl%2 != 0) {printf("Warning, the number of line per image must be even\n");}
}

void init_struct_sc_settings (sc_settings *set, float nh0, float nhp, float nsector, float nsr, int ndec, float ndelay, float ndx, float nspeed, int nosc, int not2, float nRb, float nl1, float nt1)
{
	set->h0=nh0;
	set->hp=nhp;
	set->sector=nsector;
  set->thetamax=nsector/2.f;
	set->sampling_rate=nsr;
	set->decimation=ndec;
	set->delay=ndelay;
	set->Dx=ndx;
	set->speed=nspeed;
	set->option_sc=nosc;
	set->option_theta=not2;
    set->Rb=nRb;
    set->l1=nl1;
    set->t1=nt1;

	int i=0;
	float dxi=0.f, psi=0.f, dti=0.f;

	float lDx=10.f, lh0=10.f; //local value of DC and h0 for option_sc=0

	switch (set->option_theta)
	{
		case 0: //dxi constant
            if (set->option_sc==0)
			{
				dxi=lDx/((float)(set->Nl-1));
				psi=PIf/2.f-set->sector/2.f;
				lh0=lDx/2.f*tan(psi);
				for (i=0 ; i<set->Nl/2 ; i++)
				{
					set->xi[i]=lDx/2.f - ((float)i)*dxi;
					set->ri[i]=0.f;
					psi=atan(lh0/set->xi[i]);
					set->ti[i]=-(PIf/2.f-psi);
					set->xi[i]=0.f;
				}
				for (i=set->Nl/2 ; i<set->Nl ; i++)
				{
					set->xi[i]=lDx/2.f - ((float)i)*dxi;
					set->ri[i]=0.f;
					psi=atan(-lh0/set->xi[i]);
					set->ti[i]=PIf/2.f-psi;
					set->xi[i]=0.f;
				}
			}
			else if(set->option_sc==1)
			{
				if (set->Dx==0 || set->h0==0) {printf("Warning, can't initiate scan conversion linear displacement with Dx=0 or h0=0\n");}
				dxi=set->Dx/((float)(set->Nl-1));
				for (i=0 ; i<set->Nl/2 ; i++)
				{
					set->xi[i]=set->Dx/2.f - ((float)i)*dxi;
					set->ri[i]=sqrt(set->xi[i]*set->xi[i]+set->h0*set->h0);
					psi=atan(set->h0/set->xi[i]);
					set->ti[i]=-(PIf/2.f-psi);
				}
				for (i=set->Nl/2 ; i<set->Nl ; i++)
				{
					set->xi[i]=set->Dx/2.f - ((float)i)*dxi;
					set->ri[i]=sqrt(set->xi[i]*set->xi[i]+set->h0*set->h0);
					psi=atan(-set->h0/set->xi[i]);
					set->ti[i]=PIf/2.f-psi;
				}
				if (set->thetamax>set->ti[set->Nl-1])
                {
                    set->thetamax=set->ti[set->Nl-1];
                    printf("warning angle of scan conversion is higher than phisical maximal angle\n");
                }
			}
			else if (set->option_sc==2) //actually, we take all the lines even the ones >theta_max
            {
                if (set->Dx==0 || set->h0==0) {printf("Warning, can't initiate scan conversion lounger with Dx=0 or h0=0\n");}
				dxi=set->Dx/((float)(set->Nl-1));
                //position basse de la balancelle
				for (i=0 ; i<set->Nl ; i++)
				{
					set->xi[i]=-set->Dx/2.f + ((float)i)*dxi;
                    set->ti[i]=atanf(set->xi[i]/set->h0);
					set->ri[i]=set->Rb;
					set->xi[i]=set->Rb*sinf(set->ti[i]);
					set->yi[i]=-set->Rb*cosf(set->ti[i]);
				}
				// positition of transducer
				set->t1*=PIf/180.f;
				for (i=0 ; i<set->Nl ; i++)
				{
					set->xi[i]+=set->l1*cosf(set->ti[i]);
					set->yi[i]+=set->l1*sinf(set->ti[i]);
                    set->ti[i]+=set->t1;
				}
				//position is given with origin at the pivot point
				//change to origin at model crossing point
				if (set->l1<0) {set->thetamax=set->ti[set->Nl-1];} //l1 and t1 must have the same sign
				else {set->thetamax=-set->ti[0];}
                if (set->thetamax > set->sector/2.f) {set->thetamax=set->sector/2.f;}
                else if (set->sector/2.f > set->thetamax)
                {
                    printf("Warning, sector of scan conversion is higher than real sector\n");
                    set->sector = 2.f*set->thetamax;
                }
				set->rtang=set->l1*cosf(set->t1)-set->Rb*sinf(set->t1);
				set->x0=set->rtang/cosf(set->thetamax);

                //position is the real position of the transducer must change to model position
				for (i=0 ; i<set->Nl ; i++) {set->xi[i] -= set->rtang/cosf(set->ti[i]);}

				//determin true ri in model
				for (i=0 ; i<set->Nl ; i++) {set->ri[i]=sqrtf(set->xi[i]*set->xi[i]+set->yi[i]*set->yi[i]);}
            }
			else {printf("Warning option_sc = 0, 1 or 2\n");}
			break;
		case 1: //dtheta constant
			psi=set->sector/2.f;
			dti=set->sector/((float)(set->Nl-1));
			set->ti[0]=-set->sector/2.f;
			set->xi[0]=+set->Dx/2.f;
			set->ri[0]=sqrt(set->xi[i]*set->xi[i]+set->h0*set->h0);
			for (i=1 ; i<set->Nl/2 ; i++)
			{
				set->ti[i]=-set->sector/2.f+((float)i)*dti;
				psi=PIf/2.f+set->ti[i];
				set->xi[i]=set->h0/tan(psi);
				set->ri[i]=sqrt(set->xi[i]*set->xi[i]+set->h0*set->h0);
			}
			for (i=set->Nl/2 ; i<set->Nl ; i++)
			{
				set->ti[i]=-set->sector/2.f+((float)i)*dti;
				psi=PIf/2.f-set->ti[i];
				set->xi[i]=-set->h0/tan(psi);
				set->ri[i]=sqrt(set->xi[i]*set->xi[i]+set->h0*set->h0);
			}
			if (set->option_sc==0)
			{
				for (i=0 ; i<set->Nl ; i++)
				{
					set->ri[i]=0.f;
					set->xi[i]=0.f;
				}
			}
			else if (set->option_sc<0 || set->option_sc>1) {printf("Warning option_sc =0 or 1\n");}
			break;
		case 2:
			break;
	}
}

void resize_struct_sc_settings (sc_settings *set, int nnl)
{
	resize_vector_float(&set->xi, nnl);
    resize_vector_float(&set->yi, nnl);
	resize_vector_float(&set->ri, nnl);
	resize_vector_float(&set->ti, nnl);
	set->Nl=nnl;
	if ((set->Nl/2%2)!=0) {printf("Warning, the number of line per image must be even\n");}
}

void delete_struct_sc_settings (sc_settings *set)
{
	delete_vector_float(&set->xi);
    delete_vector_float(&set->yi);
	delete_vector_float(&set->ri);
	delete_vector_float(&set->ti);
}

//image management
void create_image (scan_conv *scan_conv_struct, int Nxt, int Nyt)
{
	create_matrix_float(&scan_conv_struct->image, Nxt, Nyt);
}

void resize_image (scan_conv *scan_conv_struct, int Nxnew, int Nynew, int Nxold, int Nyold)
{
	resize_matrix_float(&scan_conv_struct->image, Nxnew, Nynew, Nxold, Nyold);
}

void delete_image (scan_conv *scan_conv_struct)
{
	delete_matrix_float(&scan_conv_struct->image,scan_conv_struct->Nx);
}


void create_indicial_x_y_buffers (scan_conv *scan_conv_struct)
{
	create_matrix_int(&scan_conv_struct->indicial_x_y, 2, scan_conv_struct->N_point_to_change);
	create_matrix_int(&scan_conv_struct->indicial_x_y_out, 2, scan_conv_struct->N_out);
}

void fill_indicial_x_y_buffers (scan_conv *scan_conv_struct)
{
	int i=0;

	if (scan_conv_struct->N_point_to_change<=scan_conv_struct->N_out)
	{
		for (i=0 ; i<scan_conv_struct->N_point_to_change ; i++)
		{
			scan_conv_struct->indicial_x_y[0][i]=scan_conv_struct->cartesian_tensor->indicial_x_y_tmp[0][i];
			scan_conv_struct->indicial_x_y[1][i]=scan_conv_struct->cartesian_tensor->indicial_x_y_tmp[1][i];
			scan_conv_struct->indicial_x_y_out[0][i]=scan_conv_struct->cartesian_tensor->indicial_x_y_out_tmp[0][i];
			scan_conv_struct->indicial_x_y_out[1][i]=scan_conv_struct->cartesian_tensor->indicial_x_y_out_tmp[1][i];
		}
		for (i=scan_conv_struct->N_point_to_change ; i<scan_conv_struct->N_out ; i++)
		{
			scan_conv_struct->indicial_x_y_out[0][i]=scan_conv_struct->cartesian_tensor->indicial_x_y_out_tmp[0][i];
			scan_conv_struct->indicial_x_y_out[1][i]=scan_conv_struct->cartesian_tensor->indicial_x_y_out_tmp[1][i];
		}
	}
	else
	{
		for (i=0 ; i<scan_conv_struct->N_out ; i++)
		{
			scan_conv_struct->indicial_x_y[0][i]=scan_conv_struct->cartesian_tensor->indicial_x_y_tmp[0][i];
			scan_conv_struct->indicial_x_y[1][i]=scan_conv_struct->cartesian_tensor->indicial_x_y_tmp[1][i];
			scan_conv_struct->indicial_x_y_out[0][i]=scan_conv_struct->cartesian_tensor->indicial_x_y_out_tmp[0][i];
			scan_conv_struct->indicial_x_y_out[1][i]=scan_conv_struct->cartesian_tensor->indicial_x_y_out_tmp[1][i];
		}
		for (i=scan_conv_struct->N_out ; i<scan_conv_struct->N_point_to_change ; i++)
		{
			scan_conv_struct->indicial_x_y[0][i]=scan_conv_struct->cartesian_tensor->indicial_x_y_tmp[0][i];
			scan_conv_struct->indicial_x_y[1][i]=scan_conv_struct->cartesian_tensor->indicial_x_y_tmp[1][i];
		}
	}
}

void resize_indicial_x_y_buffers (scan_conv *scan_conv_struct, int Nin_old, int Nout_old)
{
	resize_matrix_int(&scan_conv_struct->indicial_x_y, 2, scan_conv_struct->N_point_to_change, 2, Nin_old);
	resize_matrix_int(&scan_conv_struct->indicial_x_y_out, 2, scan_conv_struct->N_out, 2, Nout_old);
}

void delete_indicial_x_y_buffers (scan_conv *scan_conv_struct)
{
	delete_matrix_int(&scan_conv_struct->indicial_x_y, 2);
	delete_matrix_int(&scan_conv_struct->indicial_x_y_out, 2);
}

void create_indicial_weight_matrix(scan_conv *scan_conv_struct)
{
	create_matrix_int(&scan_conv_struct->indicial_r_theta, 4, scan_conv_struct->N_point_to_change);
	create_matrix_float(&scan_conv_struct->weight, 4, scan_conv_struct->N_point_to_change);
}

void resize_indicial_weight_matrix(scan_conv *scan_conv_struct, int Nin_old)
{
	resize_matrix_int(&scan_conv_struct->indicial_r_theta, 4, scan_conv_struct->N_point_to_change, 4, Nin_old);
	resize_matrix_float(&scan_conv_struct->weight, 4, scan_conv_struct->N_point_to_change, 4, Nin_old);
}

void delete_indicial_weight_matrix(scan_conv *scan_conv_struct)
{
	delete_matrix_int(&scan_conv_struct->indicial_r_theta, 4);
	delete_matrix_float(&scan_conv_struct->weight, 4);
}

//if faudrait transposer l'image et le sens de scan pour plus de logique
void indicial_matrix_calculation(scan_conv *scan_conv_struct)
{
	int i=0, k=0, l=0;
	int k1=0, k2=0;
	float r_old=0.0, r_new=0.0, theta_old=0.0, theta_new=0.0;


	switch(scan_conv_struct->settings->option_sc)
	{
		case 0:
			for (i=0 ; i<scan_conv_struct->N_point_to_change ; i++)
			{
				r_new=scan_conv_struct->polar_tensor->r_matrix[scan_conv_struct->indicial_x_y[0][i]][scan_conv_struct->indicial_x_y[1][i]];
				theta_new=scan_conv_struct->polar_tensor->theta_matrix[scan_conv_struct->indicial_x_y[0][i]][scan_conv_struct->indicial_x_y[1][i]];
				if (r_new>r_old) //r increase and decrease continuously
				{
					while (scan_conv_struct->polar_tensor->r_vector[0][k+1]<r_new && k<scan_conv_struct->Nr-1) {k++;}
				}
				else if (r_new<r_old)
				{
					while (scan_conv_struct->polar_tensor->r_vector[0][k]>r_new && k>0) {k--;}
				}
				r_old=r_new;
				if (theta_new<theta_old) //theta increase continuously but decrease from theta to -theta
				{
					l=0;
					while (scan_conv_struct->polar_tensor->theta_vector[l+1][0]<theta_new && l<scan_conv_struct->Nline-1) {l++;}
				}
				else if (theta_new>theta_old)
				{
					while (scan_conv_struct->polar_tensor->theta_vector[l+1][0]<theta_new && l<scan_conv_struct->Nline-1) {l++;}
				}
				theta_old=theta_new;

				scan_conv_struct->indicial_r_theta[0][i]=k;
				scan_conv_struct->indicial_r_theta[1][i]=k+1;
				scan_conv_struct->indicial_r_theta[2][i]=l;
				scan_conv_struct->indicial_r_theta[3][i]=l+1;
			}
			break;
		case 1:
			for (i=0 ; i<scan_conv_struct->N_point_to_change ; i++)
			{
				r_new=scan_conv_struct->polar_tensor->r_matrix[scan_conv_struct->indicial_x_y[0][i]][scan_conv_struct->indicial_x_y[1][i]];
				theta_new=scan_conv_struct->polar_tensor->theta_matrix[scan_conv_struct->indicial_x_y[0][i]][scan_conv_struct->indicial_x_y[1][i]];

				if (theta_new<theta_old) //theta increase continuously but decrease from theta to -theta
				{
					l=0;
					while (scan_conv_struct->polar_tensor->theta_vector[l+1][0]<theta_new && l<scan_conv_struct->Nline-1) {l++;}
				}
				else if (theta_new>theta_old)
				{
					while (scan_conv_struct->polar_tensor->theta_vector[l+1][0]<theta_new && l<scan_conv_struct->Nline-1) {l++;}
				}

				//search of r[k] for line l
				if (scan_conv_struct->polar_tensor->r_vector[l][k1]<r_new)
				{
					while (scan_conv_struct->polar_tensor->r_vector[l][k1+1]<r_new && k1<scan_conv_struct->Nr-1) {k1++;}
				}
				else if (scan_conv_struct->polar_tensor->r_vector[l][k1]>r_new)
				{
					while (scan_conv_struct->polar_tensor->r_vector[l][k1]>r_new && k1>0) {k1--;}
				}
				//search of r[k] for line l+1
				if (scan_conv_struct->polar_tensor->r_vector[l+1][k2]<r_new)
				{
					while (scan_conv_struct->polar_tensor->r_vector[l+1][k2+1]<r_new && k2<scan_conv_struct->Nr-1) {k2++;}
				}
				else if (scan_conv_struct->polar_tensor->r_vector[l+1][k2]>r_new)
				{
					while (scan_conv_struct->polar_tensor->r_vector[l+1][k2]>r_new && k2>0) {k2--;}
				}

				theta_old=theta_new;

				scan_conv_struct->indicial_r_theta[0][i]=k1;
				scan_conv_struct->indicial_r_theta[1][i]=k2;
				scan_conv_struct->indicial_r_theta[2][i]=l;
				scan_conv_struct->indicial_r_theta[3][i]=l+1;
			}
			break;
        case 2: //identical to case 1
			for (i=0 ; i<scan_conv_struct->N_point_to_change ; i++)
			{
				r_new=scan_conv_struct->polar_tensor->r_matrix[scan_conv_struct->indicial_x_y[0][i]][scan_conv_struct->indicial_x_y[1][i]];
				theta_new=scan_conv_struct->polar_tensor->theta_matrix[scan_conv_struct->indicial_x_y[0][i]][scan_conv_struct->indicial_x_y[1][i]];

				if (theta_new<theta_old) //theta increase continuously but decrease from theta to -theta
				{
					l=0;
					while (scan_conv_struct->polar_tensor->theta_vector[l+1][0]<theta_new && l<scan_conv_struct->Nline-1) {l++;}
				}
				else if (theta_new>theta_old)
				{
					while (scan_conv_struct->polar_tensor->theta_vector[l+1][0]<theta_new && l<scan_conv_struct->Nline-1) {l++;}
				}

				//search of r[k] for line l
				if (scan_conv_struct->polar_tensor->r_vector[l][k1]<r_new)
				{
					while (scan_conv_struct->polar_tensor->r_vector[l][k1+1]<r_new && k1<scan_conv_struct->Nr-1) {k1++;}
				}
				else if (scan_conv_struct->polar_tensor->r_vector[l][k1]>r_new)
				{
					while (scan_conv_struct->polar_tensor->r_vector[l][k1]>r_new && k1>0) {k1--;}
				}
				//search of r[k] for line l+1
				if (scan_conv_struct->polar_tensor->r_vector[l+1][k2]<r_new)
				{
					while (scan_conv_struct->polar_tensor->r_vector[l+1][k2+1]<r_new && k2<scan_conv_struct->Nr-1) {k2++;}
				}
				else if (scan_conv_struct->polar_tensor->r_vector[l+1][k2]>r_new)
				{
					while (scan_conv_struct->polar_tensor->r_vector[l+1][k2]>r_new && k2>0) {k2--;}
				}

				theta_old=theta_new;

				scan_conv_struct->indicial_r_theta[0][i]=k1;
				scan_conv_struct->indicial_r_theta[1][i]=k2;
				scan_conv_struct->indicial_r_theta[2][i]=l;
				scan_conv_struct->indicial_r_theta[3][i]=l+1;
			}
			break;
		case 3:
			break;
	}
}

void weight_matrix_calculation(scan_conv *scan_conv_struct)
{
	int i=0;
	float ri=0.f, ti=0.f, r1=0.f, r2=0.f, t1=0.f, t2=0.f, P1=0.f, P2=0.f, P3=0.f, P4=0.f;
	float Rk1=0.f, Rk2=0.F, Psi=0.f;

	if (scan_conv_struct->option==0) //linear interpolation
	{
		for (i=0 ; i<scan_conv_struct->N_point_to_change ; i++)
		{
			switch (scan_conv_struct->settings->option_sc)
			{
				case 0:
					ri=scan_conv_struct->polar_tensor->r_matrix[scan_conv_struct->indicial_x_y[0][i]][scan_conv_struct->indicial_x_y[1][i]];
					ti=scan_conv_struct->polar_tensor->theta_matrix[scan_conv_struct->indicial_x_y[0][i]][scan_conv_struct->indicial_x_y[1][i]];
					r1=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[0][i]];
					r2=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[1][i]];
					t1=scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[2][i]][0];
					t2=scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[3][i]][0];
					P1=(r2-ri)/(r2-r1);
					P2=(ri-r1)/(r2-r1);
					P3=(t2-ti)/(t2-t1);
					P4=(ti-t1)/(t2-t1);
					scan_conv_struct->weight[0][i]=P1*P3;
					scan_conv_struct->weight[1][i]=P1*P4;
					scan_conv_struct->weight[2][i]=P2*P3;
					scan_conv_struct->weight[3][i]=P2*P4;
					break;
				case 1:
					ri=scan_conv_struct->polar_tensor->r_matrix[scan_conv_struct->indicial_x_y[0][i]][scan_conv_struct->indicial_x_y[1][i]];
					ti=scan_conv_struct->polar_tensor->theta_matrix[scan_conv_struct->indicial_x_y[0][i]][scan_conv_struct->indicial_x_y[1][i]];
					r1=scan_conv_struct->polar_tensor->r_vector[scan_conv_struct->indicial_r_theta[2][i]][scan_conv_struct->indicial_r_theta[0][i]];
					r2=scan_conv_struct->polar_tensor->r_vector[scan_conv_struct->indicial_r_theta[3][i]][scan_conv_struct->indicial_r_theta[1][i]];
					t1=scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[2][i]][0];
					t2=scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[3][i]][0];
					Rk1=(ri-r1)/(scan_conv_struct->settings->dr);
					Rk2=(ri-r2)/(scan_conv_struct->settings->dr);
					Psi=(ti-t1)/(t2-t1);
					scan_conv_struct->weight[0][i]=(1.f-Psi)*(1.f-Rk1);
					scan_conv_struct->weight[1][i]=Psi*(1.f-Rk2);
					scan_conv_struct->weight[2][i]=(1.f-Psi)*Rk1;
					scan_conv_struct->weight[3][i]=Psi*Rk2;
					break;
				case 2: //identical to case 1
					ri=scan_conv_struct->polar_tensor->r_matrix[scan_conv_struct->indicial_x_y[0][i]][scan_conv_struct->indicial_x_y[1][i]];
					ti=scan_conv_struct->polar_tensor->theta_matrix[scan_conv_struct->indicial_x_y[0][i]][scan_conv_struct->indicial_x_y[1][i]];
					r1=scan_conv_struct->polar_tensor->r_vector[scan_conv_struct->indicial_r_theta[2][i]][scan_conv_struct->indicial_r_theta[0][i]];
					r2=scan_conv_struct->polar_tensor->r_vector[scan_conv_struct->indicial_r_theta[3][i]][scan_conv_struct->indicial_r_theta[1][i]];
					t1=scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[2][i]][0];
					t2=scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[3][i]][0];
					Rk1=(ri-r1)/(scan_conv_struct->settings->dr);
					Rk2=(ri-r2)/(scan_conv_struct->settings->dr);
					Psi=(ti-t1)/(t2-t1);
					scan_conv_struct->weight[0][i]=(1.f-Psi)*(1.f-Rk1);
					scan_conv_struct->weight[1][i]=Psi*(1.f-Rk2);
					scan_conv_struct->weight[2][i]=(1.f-Psi)*Rk1;
					scan_conv_struct->weight[3][i]=Psi*Rk2;
					break;
				case 3:
					break;
			}
		}
	}
	else if (scan_conv_struct->option==1) //distance interpolation
	{
		float r3=0.0, r4=0.0;
		float xi=0.0, xt=0.0, yi=0.0, yt=0.0;
		float norm=0.0;
		switch (scan_conv_struct->settings->option_sc)
		{
			case 0:
				for (i=0 ; i<scan_conv_struct->N_point_to_change ; i++)
				{
					xi=scan_conv_struct->cartesian_tensor->x_vector[scan_conv_struct->indicial_x_y[0][i]];
					yi=scan_conv_struct->cartesian_tensor->y_vector[scan_conv_struct->indicial_x_y[1][i]];
					xt=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[0][i]]*sin(scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[2][i]][0]);
					yt=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[0][i]]*cos(scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[2][i]][0]);
					r1=sqrt((xi-xt)*(xi-xt)+(yi-yt)*(yi-yt));
					xt=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[0][i]]*sin(scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[3][i]][0]);
      				yt=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[0][i]]*cos(scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[3][i]][0]);
      				r2=sqrt((xi-xt)*(xi-xt)+(yi-yt)*(yi-yt));
					xt=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[1][i]]*sin(scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[2][i]][0]);
      				yt=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[1][i]]*cos(scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[2][i]][0]);
      				r3=sqrt((xi-xt)*(xi-xt)+(yi-yt)*(yi-yt));
					xt=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[1][i]]*sin(scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[3][i]][0]);
      				yt=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[1][i]]*cos(scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[3][i]][0]);
      				r4=sqrt((xi-xt)*(xi-xt)+(yi-yt)*(yi-yt));
					norm=1/r1+1/r2+1/r3+1/r4;
					norm=1/norm;
					scan_conv_struct->weight[0][i]=norm/r1;
					scan_conv_struct->weight[1][i]=norm/r2;
					scan_conv_struct->weight[2][i]=norm/r3;
					scan_conv_struct->weight[3][i]=norm/r4;
				}
				break;
			case 1:
				for (i=0 ; i<scan_conv_struct->N_point_to_change ; i++)
				{
					xi=scan_conv_struct->cartesian_tensor->x_vector[scan_conv_struct->indicial_x_y[0][i]];
					yi=scan_conv_struct->cartesian_tensor->y_vector[scan_conv_struct->indicial_x_y[1][i]];
					xt=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[0][i]]*sin(scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[2][i]][0]);
					yt=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[0][i]]*cos(scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[2][i]][0]);
					r1=sqrt((xi-xt)*(xi-xt)+(yi-yt)*(yi-yt));
					xt=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[1][i]]*sin(scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[3][i]][0]);
      				yt=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[1][i]]*cos(scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[3][i]][0]);
      				r2=sqrt((xi-xt)*(xi-xt)+(yi-yt)*(yi-yt));
					xt=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[0][i]+1]*sin(scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[2][i]][0]);
      				yt=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[0][i]+1]*cos(scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[2][i]][0]);
      				r3=sqrt((xi-xt)*(xi-xt)+(yi-yt)*(yi-yt));
					xt=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[1][i]+1]*sin(scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[3][i]][0]);
      				yt=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[1][i]+1]*cos(scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[3][i]][0]);
      				r4=sqrt((xi-xt)*(xi-xt)+(yi-yt)*(yi-yt));
					norm=1/r1+1/r2+1/r3+1/r4;
					norm=1/norm;
					scan_conv_struct->weight[0][i]=norm/r1;
					scan_conv_struct->weight[1][i]=norm/r2;
					scan_conv_struct->weight[2][i]=norm/r3;
					scan_conv_struct->weight[3][i]=norm/r4;
				}
				break;
            case 2: //identical to case 1
				for (i=0 ; i<scan_conv_struct->N_point_to_change ; i++)
				{
					xi=scan_conv_struct->cartesian_tensor->x_vector[scan_conv_struct->indicial_x_y[0][i]];
					yi=scan_conv_struct->cartesian_tensor->y_vector[scan_conv_struct->indicial_x_y[1][i]];
					xt=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[0][i]]*sin(scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[2][i]][0]);
					yt=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[0][i]]*cos(scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[2][i]][0]);
					r1=sqrt((xi-xt)*(xi-xt)+(yi-yt)*(yi-yt));
					xt=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[1][i]]*sin(scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[3][i]][0]);
      				yt=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[1][i]]*cos(scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[3][i]][0]);
      				r2=sqrt((xi-xt)*(xi-xt)+(yi-yt)*(yi-yt));
					xt=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[0][i]+1]*sin(scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[2][i]][0]);
      				yt=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[0][i]+1]*cos(scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[2][i]][0]);
      				r3=sqrt((xi-xt)*(xi-xt)+(yi-yt)*(yi-yt));
					xt=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[1][i]+1]*sin(scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[3][i]][0]);
      				yt=scan_conv_struct->polar_tensor->r_vector[0][scan_conv_struct->indicial_r_theta[1][i]+1]*cos(scan_conv_struct->polar_tensor->theta_vector[scan_conv_struct->indicial_r_theta[3][i]][0]);
      				r4=sqrt((xi-xt)*(xi-xt)+(yi-yt)*(yi-yt));
					norm=1/r1+1/r2+1/r3+1/r4;
					norm=1/norm;
					scan_conv_struct->weight[0][i]=norm/r1;
					scan_conv_struct->weight[1][i]=norm/r2;
					scan_conv_struct->weight[2][i]=norm/r3;
					scan_conv_struct->weight[3][i]=norm/r4;
				}
				break;
			case 3:
				break;
		}
	}
}

void create_scan_conv_struct (scan_conv *scan_conv_struct, int Nr_probe, int Nline_probe, float sector_probe, float R0_probe, float Rf_probe, int Nx_im, int Ny_im, int option_selection, float nh0, float nhp, float nsr, int ndec, float ndelay, float ndx, float speed, int nosc, int npt, float nRb, float nl1, float nt1)
{
	//init attribuite of the structure
	sector_probe=sector_probe*PIf/180.0;
	scan_conv_struct->settings=(sc_settings *)malloc(sizeof(sc_settings));
	scan_conv_struct->cartesian_tensor=(x_y_tensor *)malloc(sizeof(x_y_tensor));
	scan_conv_struct->polar_tensor=(r_theta_tensor *)malloc(sizeof(r_theta_tensor));
	scan_conv_struct->Nr=Nr_probe;
	scan_conv_struct->Nline=Nline_probe;
	scan_conv_struct->sector=sector_probe;
	scan_conv_struct->polar_tensor->R0=R0_probe;
	scan_conv_struct->polar_tensor->Rf=Rf_probe;
	scan_conv_struct->Nx=Nx_im;
	scan_conv_struct->Ny=Ny_im;
	scan_conv_struct->option=option_selection;

	create_struct_sc_settings (scan_conv_struct->settings, Nline_probe);
	init_struct_sc_settings (scan_conv_struct->settings, nh0, nhp, sector_probe, nsr, ndec, ndelay, ndx, speed, nosc, npt, nRb, nl1, nt1);

	create_x_y_tensor (scan_conv_struct->cartesian_tensor, Nx_im, Ny_im);
	fill_x_y_vector(scan_conv_struct);

	create_r_theta_tensor (scan_conv_struct->polar_tensor, scan_conv_struct->settings, R0_probe, Rf_probe, Nr_probe, Nline_probe, Nx_im, Ny_im);
	fill_r_theta_vectors(scan_conv_struct);
	fill_r_theta_matrices(scan_conv_struct);

	create_image(scan_conv_struct, Nx_im, Ny_im);
	create_indicial_x_y_buffers(scan_conv_struct);
	fill_indicial_x_y_buffers(scan_conv_struct);
	create_indicial_weight_matrix(scan_conv_struct);
	indicial_matrix_calculation(scan_conv_struct);
	weight_matrix_calculation(scan_conv_struct);

	//clear all temporary buffer
	clear_x_y_tensor(scan_conv_struct->cartesian_tensor);
	clear_r_theta_tensor(scan_conv_struct->polar_tensor);
}


void create_scan_conv_struct_pure_rotation(scan_conv *scan_conv_struct, int Nr_probe, int Nline_probe, float sector_probe, float R0_probe, float Rf_probe, int Nx_im, int Ny_im, float nh0, float nhp, float nsr, int ndec, float ndelay, float nspeed, int theta_option, int wheigt_option)
{
    create_scan_conv_struct (scan_conv_struct, Nr_probe, Nline_probe, sector_probe, R0_probe, Rf_probe, Nx_im, Ny_im, wheigt_option, nh0, nhp, nsr, ndec, ndelay, 0.0, nspeed, 0, theta_option, 0.f, 0.f, 0.f);
}

void create_scan_conv_struct_linear_displacement(scan_conv *scan_conv_struct, int Nr_probe, int Nline_probe, float sector_probe, float R0_probe, float Rf_probe, int Nx_im, int Ny_im, float nh0, float nhp, float nsr, int ndec, float ndelay, float ndx, float nspeed, int theta_option, int wheigt_option)
{
    create_scan_conv_struct (scan_conv_struct, Nr_probe, Nline_probe, sector_probe, R0_probe, Rf_probe, Nx_im, Ny_im, wheigt_option, nh0, nhp, nsr, ndec, ndelay, ndx, nspeed, 1, theta_option, 0.f, 0.f, 0.f);
}

void create_scan_conv_struct_lounger (scan_conv *scan_conv_struct, int Nr_probe, int Nline_probe, float sector_probe, float R0_probe, float Rf_probe, int Nx_im, int Ny_im, float nh0, float nhp, float nsr, int ndec, float ndelay, float ndx, float nspeed, int wheigt_option, float nRb, float nl1, float nt1, int theta_option)
{
    create_scan_conv_struct (scan_conv_struct, Nr_probe, Nline_probe, sector_probe, R0_probe, Rf_probe, Nx_im, Ny_im, wheigt_option, nh0, nhp, nsr, ndec, ndelay, ndx, nspeed, 2, theta_option, nRb, nl1, nt1);
}

void resize_scan_conv_struct (scan_conv *scan_conv_struct, int Nr_probe, int Nline_probe, float sector_probe, float R0_probe, float Rf_probe, int Nx_im, int Ny_im, int option_selection, float nh0, float nhp, float nsr, int ndec, float ndelay, float ndx, float speed, int nosc, int npt, float nRb, float nl1, float nt1)
{
	//take old value for resizing attribuite of the structure
	sector_probe=sector_probe*PIf/180.0;
	int Nx_old, Ny_old, Nin_old, Nout_old;//, option_old, Nr_old, Nline_old;
	Nx_old=scan_conv_struct->Nx;
	Ny_old=scan_conv_struct->Ny;
	Nin_old=scan_conv_struct->N_point_to_change;
	Nout_old=scan_conv_struct->N_out;

	scan_conv_struct->Nr=Nr_probe;
	scan_conv_struct->Nline=Nline_probe;
	scan_conv_struct->sector=sector_probe;
	scan_conv_struct->polar_tensor->R0=R0_probe;
	scan_conv_struct->polar_tensor->Rf=Rf_probe;
	scan_conv_struct->Nx=Nx_im;
	scan_conv_struct->Ny=Ny_im;
	scan_conv_struct->option=option_selection;

	resize_struct_sc_settings (scan_conv_struct->settings, Nline_probe);
	init_struct_sc_settings (scan_conv_struct->settings, nh0, nhp, sector_probe, nsr, ndec, ndelay, ndx, speed, nosc, npt, nRb, nl1, nt1);

	create_x_y_tensor (scan_conv_struct->cartesian_tensor, Nx_im, Ny_im);
	fill_x_y_vector(scan_conv_struct);

	create_r_theta_tensor (scan_conv_struct->polar_tensor, scan_conv_struct->settings, R0_probe, Rf_probe, Nr_probe, Nline_probe, Nx_im, Ny_im);
	fill_r_theta_vectors(scan_conv_struct);
	fill_r_theta_matrices(scan_conv_struct);

	if (Nx_im!=Nx_old || Ny_im!=Ny_old)  {resize_image(scan_conv_struct, Nx_im, Ny_im, Nx_old, Ny_old);}
	resize_indicial_x_y_buffers(scan_conv_struct, Nin_old, Nout_old);
	fill_indicial_x_y_buffers(scan_conv_struct);
	resize_indicial_weight_matrix(scan_conv_struct, Nin_old);
	indicial_matrix_calculation(scan_conv_struct);
	weight_matrix_calculation(scan_conv_struct);

	//clear all temporary buffer
	clear_x_y_tensor(scan_conv_struct->cartesian_tensor);
	clear_r_theta_tensor(scan_conv_struct->polar_tensor);
}

void delete_scan_conv_struct (scan_conv *scan_conv_struct)
{
	delete_image(scan_conv_struct);
	delete_indicial_x_y_buffers(scan_conv_struct);
	delete_indicial_weight_matrix(scan_conv_struct);
	delete_struct_sc_settings(scan_conv_struct->settings);
	free(scan_conv_struct->settings);
    scan_conv_struct->settings=NULL;
	free(scan_conv_struct->cartesian_tensor);
    scan_conv_struct->cartesian_tensor=NULL;
	free(scan_conv_struct->polar_tensor);
    scan_conv_struct->polar_tensor=NULL;
}

void image_scan_conversion (scan_conv *scan_conv_struct, float **image_r_theta)
{
	int i=0;
	float P1=0.0, P2=0.0, P3=0.0, P4=0.0;
	for (i=0 ; i<scan_conv_struct->N_point_to_change ; i++)
	{
		switch (scan_conv_struct->settings->option_sc)
		{
			case 0:
				P1=scan_conv_struct->weight[0][i]*image_r_theta[scan_conv_struct->indicial_r_theta[2][i]][scan_conv_struct->indicial_r_theta[0][i]];
				P2=scan_conv_struct->weight[1][i]*image_r_theta[scan_conv_struct->indicial_r_theta[3][i]][scan_conv_struct->indicial_r_theta[0][i]];
				P3=scan_conv_struct->weight[2][i]*image_r_theta[scan_conv_struct->indicial_r_theta[2][i]][scan_conv_struct->indicial_r_theta[1][i]];
				P4=scan_conv_struct->weight[3][i]*image_r_theta[scan_conv_struct->indicial_r_theta[3][i]][scan_conv_struct->indicial_r_theta[1][i]];
				break;
			case 1:
				P1=scan_conv_struct->weight[0][i]*image_r_theta[scan_conv_struct->indicial_r_theta[2][i]][scan_conv_struct->indicial_r_theta[0][i]];
				P2=scan_conv_struct->weight[1][i]*image_r_theta[scan_conv_struct->indicial_r_theta[3][i]][scan_conv_struct->indicial_r_theta[1][i]];
				P3=scan_conv_struct->weight[2][i]*image_r_theta[scan_conv_struct->indicial_r_theta[2][i]][scan_conv_struct->indicial_r_theta[0][i]+1];
				P4=scan_conv_struct->weight[3][i]*image_r_theta[scan_conv_struct->indicial_r_theta[3][i]][scan_conv_struct->indicial_r_theta[1][i]+1];
				break;
			case 2:
				P1=scan_conv_struct->weight[0][i]*image_r_theta[scan_conv_struct->indicial_r_theta[2][i]][scan_conv_struct->indicial_r_theta[0][i]];
				P2=scan_conv_struct->weight[1][i]*image_r_theta[scan_conv_struct->indicial_r_theta[3][i]][scan_conv_struct->indicial_r_theta[1][i]];
				P3=scan_conv_struct->weight[2][i]*image_r_theta[scan_conv_struct->indicial_r_theta[2][i]][scan_conv_struct->indicial_r_theta[0][i]+1];
				P4=scan_conv_struct->weight[3][i]*image_r_theta[scan_conv_struct->indicial_r_theta[3][i]][scan_conv_struct->indicial_r_theta[1][i]+1];
				break;
			case 3:
				break;
		}
		scan_conv_struct->image[scan_conv_struct->indicial_x_y[0][i]][scan_conv_struct->indicial_x_y[1][i]]=(P1+P2+P3+P4);
	}
}

void change_image_background (scan_conv *scan_conv_struct, float background)
{
	int i=0;
	for (i=0 ; i<scan_conv_struct->N_out ; i++)
	{
		scan_conv_struct->image[scan_conv_struct->indicial_x_y_out[0][i]][scan_conv_struct->indicial_x_y_out[1][i]]=background;
	}
}
