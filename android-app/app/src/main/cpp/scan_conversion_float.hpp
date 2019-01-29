/**
* @file scan_conversion_float.hpp
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
 * A C librairy create to make scan conversion using
 * linear or weight interpolation. Weight interpolation often use
 * in acoustic imaging, but linear interpolation give better results.
 *
 * This version of the scan conversion take into account the linear displacement plus rotation of the transducer.
 *
 * This librairy use structures to facilitate the use in C code. These structures can
 * be resize. Need the pointer_management.h librairy.
 *
 * @section USAGE
 * scan_conv scan_conv_struct;
 * float **dataf=NULL;
 * create_matrix_float(&dataf, Nline, Npoint);
 * create_scan_conv_struct (&scan_conv_struct, Npoint, Nline, sector, r0, rf, nx, ny, 0); //initiate the structure and detemined the wieght matrix to make scan conversion
 * image_scan_conversion (&scan_conv_struct, dataf);
 */

#ifndef SCAN_CONVERSION_FLOAT_HPP
#define SCAN_CONVERSION_FLOAT_HPP

#define PIf 3.14159265358979323846

typedef struct x_y_tensor x_y_tensor;
typedef struct r_theta_tensor r_theta_tensor;
typedef struct sc_settings sc_settings;
typedef struct scan_conv scan_conv;

void create_x_y_tensor (x_y_tensor *xyt, int Nxt, int Nyt);
void fill_x_y_vector (scan_conv *scan_conv_struct);
void resize_x_y_tensor (x_y_tensor *xyt, int Nxt, int Nyt);
void clear_x_y_tensor (x_y_tensor *xyt);

void create_r_theta_tensor (r_theta_tensor *rtt, sc_settings *settings, float R0t, float Rft, int Nrt, int Nlt, int Nxt, int Nyt);
void fill_r_theta_vectors (scan_conv *scan_conv_struct);
void fill_r_theta_matrices (scan_conv *scan_conv_struct); //also fill matrix of x_y_tensor
void resize_r_theta_tensor (r_theta_tensor *rtt, sc_settings *settings, float R0t, float Rft, int Nrt, int Nlt, int Nxt, int Nyt);
void clear_r_theta_tensor(r_theta_tensor *rtt);

void create_struct_sc_settings (sc_settings *set, int Nl);
void init_struct_sc_settings (sc_settings *set, float nh0, float nhp, float nsector, float nsr, int ndec, float ndelay, float ndx, float nspeed, int nosc, int not2, float nRb, float nl1, float nt1);
void resize_struct_sc_settings (sc_settings *set, int nnl);
void delete_struct_sc_settings (sc_settings *set);

void create_image (scan_conv *scan_conv_struct, int Nxt, int Nyt);
void resize_image (scan_conv *scan_conv_struct, int Nxnew, int Nynew, int Nxold, int Nyold);
void delete_image (scan_conv *scan_conv_struct);

void create_indicial_x_y_buffers (scan_conv *scan_conv_struct);
void fill_indicial_x_y_buffers (scan_conv *scan_conv_struct);
void resize_indicial_x_y_buffers (scan_conv *scan_conv_struct, int Nin_old, int Nout_old);
void delete_indicial_x_y_buffers (scan_conv *scan_conv_struct);

void create_indicial_weight_matrix(scan_conv *scan_conv_struct);
void resize_indicial_weight_matrix(scan_conv *scan_conv_struct, int Nin_old);
void delete_indicial_weight_matrix(scan_conv *scan_conv_struct);
void indicial_matrix_calculation(scan_conv *scan_conv_struct);
void weight_matrix_calculation(scan_conv *scan_conv_struct);

/*
 *
 * Scan conversion struct creation
 * int Nr_probe: number of point per line
 * int Nline_probe: number of line per image
 * float sector_probe: sector of the image
 * float R0_probe: minimum radius of the scan conversion
 * float Rf_probe: maximum radius of the scan conversion
 * int Nx_im: number of pixels of the image in the x direction
 * int Ny_im: number of pixels of the image in the y direction
 * int option_selection = int wheigt_option: 0, bilinear interpolation ; 1, weight/distance interpolation
 * float nh0: distance cart/pivot
 * float nhp: distance pivot/nose of the probe
 * float nsr: sampling rate of us lines
 * int ndec: decimation of us lines
 * float ndelay: delay before sampling us lines
 * float ndx: total translation distance of cart
 * float nspeed: speed of sound in water
 * int nosc: scan conversion type: 0, pure rotation ; 1, linear displacement a cart ; 2, mechanism V3 from full concept, transducer on a lounger
 * int npt = int theta_option: theta sampling type: 0, dx = constant, dtheta varies ; 1, dtheta = constant, dx varies
 *
*/
void create_scan_conv_struct (scan_conv *scan_conv_struct, int Nr_probe, int Nline_probe, float sector_probe, float R0_probe, float Rf_probe, int Nx_im, int Ny_im, int option_selection, float nh0, float nhp, float nsr, int ndec, float ndelay, float ndx, float nspeed, int nosc, int npt, float nRb, float nl1, float nt1);

void resize_scan_conv_struct (scan_conv *scan_conv_struct, int Nr_probe, int Nline_probe, float sector_probe, float R0_probe, float Rf_probe, int Nx_im, int Ny_im, int option_selection, float nh0, float nhp, float nsr, int ndec, float ndelay, float ndx, float nspeed, int nosc, int npt, float nRb, float nl1, float nt1);

void create_scan_conv_struct_pure_rotation(scan_conv *scan_conv_struct, int Nr_probe, int Nline_probe, float sector_probe, float R0_probe, float Rf_probe, int Nx_im, int Ny_im, float nh0, float nhp, float nsr, int ndec, float ndelay, float nspeed, int theta_option, int wheigt_option);

void create_scan_conv_struct_linear_displacement(scan_conv *scan_conv_struct, int Nr_probe, int Nline_probe, float sector_probe, float R0_probe, float Rf_probe, int Nx_im, int Ny_im, float nh0, float nhp, float nsr, int ndec, float ndelay, float ndx, float nspeed, int theta_option, int wheigt_option);

void create_scan_conv_struct_lounger (scan_conv *scan_conv_struct, int Nr_probe, int Nline_probe, float sector_probe, float R0_probe, float Rf_probe, int Nx_im, int Ny_im, float nh0, float nhp, float nsr, int ndec, float ndelay, float ndx, float nspeed, int wheigt_option, float nRb, float nl1, float nt1, int theta_option);

void delete_scan_conv_struct (scan_conv *scan_conv_struc);

void image_scan_conversion (scan_conv *scan_conv_struct, float **image_r_theta);
void change_image_background (scan_conv *scan_conv_struct, float background);

//structure definition
struct x_y_tensor
{
	int Nx; //number of point of the x vector
	int Ny; //number of point of the y vector

	float *x_vector;
	float *y_vector;
	int **indicial_x_y_tmp;
	int **indicial_x_y_out_tmp;
};

struct r_theta_tensor
{
	float R0; //begin of measuring line
	float Rf; //end of measuring line
	int Nr;
	int Nl;
	int Nx;
	int Ny;

	int option_sc;

	float **r_vector;
	float **theta_vector;
	float **r_matrix;
	float **theta_matrix;
};

struct sc_settings
{
	float h0; //height of the central point compare to cart
	float hp; //height of the nose of the probe compare to h0
	float sector; //sector of the image in radian
	float sampling_rate; //sampling rate of us line
	int decimation; //decimation of us line
	float delay; // delay in us between pulse and measurement
	float Dx; //total translation distance
	int Nl; //number of line per image
	float speed; //speed of sound in water
	float dr; //delta r in each line (it not change from one line to another)

	float *xi;
    float *yi;
	float *ri;
	float *ti;
    float rtang; //radius of circle tanget to line in case 2
    float thetamax; //maximum half angle of the sector
    float x0;
    float Rb;// distance between pivot and transducer
    float l1;// distance of the transducer
    float t1;// transducer angle

	int option_sc; //transducer movement option, 0: "classic", transducer on circular movement, 1: "simple linear", transducer on linear movement, all ray cut at the same point, 2: mecanic v3 with geometrical transformation, 3: "custom 1"
	int option_theta; //delta theta calculation 0: dxi constant, 1: dtheta constant
};

struct scan_conv
{
	int N_point_to_change; //number of pixel to calcul
	int N_out; //number of pixel outside the sector
	int **indicial_x_y; //2*N_point_to_change matrix, indicial in the image of the N_point_to_changepixel that have to be calculated in the image
	int **indicial_x_y_out; //2*N_point_to_change matrix, gather the indices of the point of the image out the imaging sector
	int **indicial_r_theta; //size 4*N_point_to_change, indicial of the 4 points in the r theta plan that are used in the calculus of the Nth pixel, pixel listed in indicial_x_y
	float **weight; //size 4*N_point_to_change, weight of the 4 points listed in indicial_r_theta for the calcul of the pixel listed in indicial_x_y
	int Nr; //number of point per line from the probe
	int Nline; //number of line per image from the probe
	float sector; //sector of the image
	int Nx; //number of pixel along x in the image
	int Ny; //number of pixel along y in the image
	float **image; //image
	int option; //type of scan conversion, 0 linear interpolation, 1 weight/distance interpolation

	x_y_tensor *cartesian_tensor;
	r_theta_tensor *polar_tensor;
	sc_settings *settings;
};


#endif
