/**
 * @file pointer_management.h
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
 * A simple C librairy made to easily manage "vector" and "matrix"
 * pointers. Just need to call create, resize and delete function.
 * Usage exemple for int:
 *
 * int *vec=NULL;
 * int numel=12;
 * create_vector_int(&vec, numel);
 * resize_vector_int(&vec, numel+4);
 * delete_vector_int(&vec);
 *
 * successfully pass varied valgrind tests
 *
 */

#ifndef POINTER_MANAGEMENT_HPP
#define POINTER_MANAGEMENT_HPP

#include<stdio.h>
#include<stdlib.h>
#include<stdint.h> //int16_t etc
#include<complex.h>

//complex typedef
typedef double _Complex cplxd;
typedef float _Complex cplxf;

// char vector management
void create_vector_char (char **data_vec, int N_data);
void create_0_vector_char (char **data_vec, int N_data);
void resize_vector_char (char **data_vec, int N_data);
void delete_vector_char (char **data_vec);
// char matrix management
void create_matrix_char (char ***data_mat, int Nx_data, int Ny_data);
void create_0_matrix_char (char ***data_mat, int Nx_data, int Ny_data);
void resize_matrix_char (char ***data_mat, int Nx_data, int Ny_data, int Nx_old_data, int Ny_old_data);
void delete_matrix_char (char ***data_mat, int Nx_data);

// int vector management
void create_vector_int (int **data_vec, int N_data);
void create_0_vector_int (int **data_vec, int N_data);
void resize_vector_int (int **data_vec, int N_data);
void delete_vector_int (int **data_vec);
// int matrix management
void create_matrix_int (int ***data_mat, int Nx_data, int Ny_data);
void create_0_matrix_int (int ***data_mat, int Nx_data, int Ny_data);
void resize_matrix_int (int ***data_mat, int Nx_data, int Ny_data, int Nx_old_data, int Ny_old_data);
void delete_matrix_int (int ***data_mat, int Nx_data);

// int16_t vector management
void create_vector_int16_t (int16_t **data_vec, int N_data);
void create_0_vector_int16_t (int16_t **data_vec, int N_data);
void resize_vector_int16_t (int16_t **data_vec, int N_data);
// int matrix management
void delete_vector_int16_t (int16_t **data_vec);
void create_matrix_int16_t (int16_t ***data_mat, int Nx_data, int Ny_data);
void create_0_matrix_int16_t (int16_t ***data_mat, int Nx_data, int Ny_data);
void resize_matrix_int16_t (int16_t ***data_mat, int Nx_data, int Ny_data, int Nx_old_data, int Ny_old_data);
void delete_matrix_int16_t (int16_t ***data_mat, int Nx_data);

// float vector management
void create_vector_float (float **data_vec, int N_data);
void create_0_vector_float (float **data_vec, int N_data);
void resize_vector_float (float **data_vec, int N_data);
void delete_vector_float (float **data_vec);
// float matrix management
void create_matrix_float (float ***data_mat, int Nx_data, int Ny_data);
void create_0_matrix_float (float ***data_mat, int Nx_data, int Ny_data);
void resize_matrix_float (float ***data_mat, int Nx_data, int Ny_data, int Nx_old_data, int Ny_old_data);
void delete_matrix_float (float ***data_mat, int Nx_data);

// double vector management
void create_vector_double (double **data_vec, int N_data);
void create_0_vector_double (double **data_vec, int N_data);
void resize_vector_double (double **data_vec, int N_data);
void delete_vector_double (double **data_vec);
// float matrix management
void create_matrix_double (double ***data_mat, int Nx_data, int Ny_data);
void create_0_matrix_double (double ***data_mat, int Nx_data, int Ny_data);
void resize_matrix_double (double ***data_mat, int Nx_data, int Ny_data, int Nx_old_data, int Ny_old_data);
void delete_matrix_double (double ***data_mat, int Nx_data);

// complex float vector management
void create_vector_cplxf (cplxf **data_vec, int N_data);
void create_0_vector_cplxf (cplxf **data_vec, int N_data);
void resize_vector_cplxf (cplxf **data_vec, int N_data);
void delete_vector_cplxf (cplxf **data_vec);

// complex double vector management
void create_vector_cplxd (cplxd **data_vec, int N_data);
void create_0_vector_cplxd (cplxd **data_vec, int N_data);
void resize_vector_cplxd (cplxd **data_vec, int N_data);
void delete_vector_cplxd (cplxd **data_vec);


//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//int vector management
void create_vector_char (char **data_vec, int N_data)
{
  *data_vec=(char *)malloc(N_data*sizeof(char));
  if (*data_vec==NULL)
  {
    printf("error allocating char vector\n");
    exit(11);
  }
}

void create_0_vector_char (char **data_vec, int N_data)
{
  *data_vec=(char *)calloc(N_data,sizeof(char));
  if (*data_vec==NULL)
  {
    printf("error allocating 0 char vector\n");
    exit(11);
  }
}

void resize_vector_char (char **data_vec, int N_data)
{
  if (*data_vec==NULL)
  {
    printf("warning, resizing a NULL char pointer\n");
    create_vector_char(data_vec, N_data);
  }
  else
  {
    char *tmp=NULL;
    tmp=(char *)realloc(*data_vec,N_data*sizeof(char));
    if (tmp==NULL)
    {
      printf("error reallocating char vector\n");
      delete_vector_char(data_vec);
    }
    else {*data_vec=tmp;}
  }
}

void delete_vector_char (char **data_vec)
{
  if (*data_vec==NULL) {printf("Warning, deleting an already NULL char pointer\n");}
  else
  {
    free(*data_vec);
    *data_vec=NULL;
  }
}

//char matrix management
void create_matrix_char (char ***data_mat, int Nx_data, int Ny_data)
{
  int i=0;
  char **tmp=NULL;

  tmp=(char **)malloc(Nx_data*sizeof(char *));
  if (tmp==NULL)
  {
    printf("error allocating char matrix\n");
    exit(11);
  }
  for (i=0 ; i<Nx_data ; i++)
  {
    create_vector_char(&tmp[i], Ny_data);
  }
  *data_mat=tmp;
}

void create_0_matrix_char (char ***data_mat, int Nx_data, int Ny_data)
{
  int i=0;
  char **tmp;
  tmp=(char **)malloc(Nx_data*sizeof(char *));
  if (tmp==NULL)
  {
    printf("error allocating 0 char matrix\n");
    exit(11);
  }
  for (i=0 ; i<Nx_data ; i++)
  {
    create_0_vector_char(&tmp[i], Ny_data);
  }
  *data_mat=tmp;
}


void resize_matrix_char (char ***data_mat, int Nx_data, int Ny_data, int Nx_old_data, int Ny_old_data)
{
  int i=0;
  char **tmp=*data_mat;
  char **tmp2=NULL;
  if (tmp==NULL)
  {
    printf("Warning, resizing, NULL pointer\n");
    create_matrix_char(data_mat, Nx_data, Ny_data);
  }
  else
  {
    if (Nx_data<Nx_old_data)
    {
      for (i=Nx_data ; i<Nx_old_data ; i++)
      {
        delete_vector_char(&tmp[i]);
      }
      tmp2=(char **)realloc(tmp,Nx_data*sizeof(char *));
      if (tmp2==NULL)
      {
        printf("error reallocating char matrix\n");
        exit(12);
      }
      else
      {
        tmp=tmp2;
        for (i=0 ; i<Nx_data ; i++)
        {
          resize_vector_char(&tmp[i], Ny_data);
        }
      }
      *data_mat=tmp;
    }
    else if(Nx_data>Nx_old_data)
    {
      tmp2=(char **)realloc(tmp,Nx_data*sizeof(char *));
      if (tmp2==NULL)
      {
        printf("error reallocating char matrix\n");
        exit(12);
      }
      else
      {
        tmp=tmp2;
        for (i=0 ; i<Nx_old_data ; i++)
        {
          resize_vector_char(&tmp[i], Ny_data);
        }
        for (i=Nx_old_data ; i<Nx_data ; i++)
        {
          create_vector_char(&tmp[i], Ny_data);
        }
      }
      *data_mat=tmp;
    }
    else if (Ny_data!=Ny_old_data)
    {
      for (i=0 ; i<Nx_data ; i++)
      {
        resize_vector_char(&tmp[i], Ny_data);
      }
      *data_mat=tmp;
    }
    else {printf("Warning, the size of char matrix has not changed\n");}
  }
}

void delete_matrix_char (char ***data_mat, int Nx_data)
{
  int i=0;
  char **tmp=*data_mat;
  if (tmp==NULL) {printf("Warning, deleting an already NULL pointer matrix\n");}
  else
  {
    for (i=0 ; i<Nx_data ; i++)
    {
      delete_vector_char(&tmp[i]);
    }
    free(*data_mat);
    *data_mat=NULL;
  }
}

//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//int vector management
void create_vector_int (int **data_vec, int N_data)
{
  *data_vec=(int *)malloc(N_data*sizeof(int));
  if (*data_vec==NULL)
  {
    printf("error allocating int vector\n");
    exit(11);
  }
}

void create_0_vector_int (int **data_vec, int N_data)
{
  *data_vec=(int *)calloc(N_data,sizeof(int));
  if (*data_vec==NULL)
  {
    printf("error allocating 0 int vector\n");
    exit(11);
  }
}

void resize_vector_int (int **data_vec, int N_data)
{
  if (*data_vec==NULL)
  {
    printf("warning, resizing a NULL int pointer\n");
    create_vector_int(data_vec, N_data);
  }
  else
  {
    int *tmp=NULL;
    tmp=(int *)realloc(*data_vec,N_data*sizeof(int));
    if (tmp==NULL)
    {
      printf("error reallocating int vector\n");
      delete_vector_int(data_vec);
    }
    else {*data_vec=tmp;}
  }
}

void delete_vector_int (int **data_vec)
{
  if (*data_vec==NULL) {printf("Warning, deleting an already NULL int pointer\n");}
  else
  {
    free(*data_vec);
    *data_vec=NULL;
  }
}

//int matrix management
void create_matrix_int (int ***data_mat, int Nx_data, int Ny_data)
{
  int i=0;
  int **tmp=NULL;

  tmp=(int **)malloc(Nx_data*sizeof(int *));
  if (tmp==NULL)
  {
    printf("error allocating int matrix\n");
    exit(11);
  }
  for (i=0 ; i<Nx_data ; i++)
  {
    create_vector_int(&tmp[i], Ny_data);
  }
  *data_mat=tmp;
}

void create_0_matrix_int (int ***data_mat, int Nx_data, int Ny_data)
{
  int i=0;
  int **tmp;
  tmp=(int **)malloc(Nx_data*sizeof(int *));
  if (tmp==NULL)
  {
    printf("error allocating 0 int matrix\n");
    exit(11);
  }
  for (i=0 ; i<Nx_data ; i++)
  {
    create_0_vector_int(&tmp[i], Ny_data);
  }
  *data_mat=tmp;
}


void resize_matrix_int (int ***data_mat, int Nx_data, int Ny_data, int Nx_old_data, int Ny_old_data)
{
  int i=0;
  int **tmp=*data_mat;
  int **tmp2=NULL;
  if (tmp==NULL)
  {
    printf("Warning, resizing, NULL pointer\n");
    create_matrix_int(data_mat, Nx_data, Ny_data);
  }
  else
  {
    if (Nx_data<Nx_old_data)
    {
      for (i=Nx_data ; i<Nx_old_data ; i++)
      {
        delete_vector_int(&tmp[i]);
      }
      tmp2=(int **)realloc(tmp,Nx_data*sizeof(int *));
      if (tmp2==NULL)
      {
        printf("error reallocating int matrix\n");
        exit(12);
      }
      else
      {
        tmp=tmp2;
        for (i=0 ; i<Nx_data ; i++)
        {
          resize_vector_int(&tmp[i], Ny_data);
        }
      }
      *data_mat=tmp;
    }
    else if(Nx_data>Nx_old_data)
    {
      tmp2=(int **)realloc(tmp,Nx_data*sizeof(int *));
      if (tmp2==NULL)
      {
        printf("error reallocating int matrix\n");
        exit(12);
      }
      else
      {
        tmp=tmp2;
        for (i=0 ; i<Nx_old_data ; i++)
        {
          resize_vector_int(&tmp[i], Ny_data);
        }
        for (i=Nx_old_data ; i<Nx_data ; i++)
        {
          create_vector_int(&tmp[i], Ny_data);
        }
      }
      *data_mat=tmp;
    }
    else if (Ny_data!=Ny_old_data)
    {
      for (i=0 ; i<Nx_data ; i++)
      {
        resize_vector_int(&tmp[i], Ny_data);
      }
      *data_mat=tmp;
    }
    else {printf("Warning, the size of int matrix has not changed\n");}
  }
}

void delete_matrix_int (int ***data_mat, int Nx_data)
{
  int i=0;
  int **tmp=*data_mat;
  if (tmp==NULL) {printf("Warning, deleting an already NULL pointer matrix\n");}
  else
  {
    for (i=0 ; i<Nx_data ; i++)
    {
      delete_vector_int(&tmp[i]);
    }
    free(*data_mat);
    *data_mat=NULL;
  }
}

//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//int16_t vector management
void create_vector_int16_t (int16_t **data_vec, int N_data)
{
  *data_vec=(int16_t *)malloc(N_data*sizeof(int16_t));
  if (*data_vec==NULL)
  {
    printf("error allocating int16_t vector\n");
    exit(11);
  }
}

void create_0_vector_int16_t (int16_t **data_vec, int N_data)
{
  *data_vec=(int16_t *)calloc(N_data,sizeof(int16_t));
  if (*data_vec==NULL)
  {
    printf("error allocating 0 int16_t vector\n");
    exit(11);
  }
}

void resize_vector_int16_t (int16_t **data_vec, int N_data)
{
  if (*data_vec==NULL)
  {
    printf("warning, resizing a NULL int16_t pointer\n");
    create_vector_int16_t(data_vec, N_data);
  }
  else
  {
    int16_t *tmp=NULL;
    tmp=(int16_t *)realloc(*data_vec,N_data*sizeof(int16_t));
    if (tmp==NULL)
    {
      printf("error reallocating int16_t vector\n");
      delete_vector_int16_t(data_vec);
    }
    else {*data_vec=tmp;}
  }
}

void delete_vector_int16_t (int16_t **data_vec)
{
  if (*data_vec==NULL) {printf("Warning, deleting an already NULL int pointer\n");}
  else
  {
    free(*data_vec);
    *data_vec=NULL;
  }
}

//int matrix management
void create_matrix_int16_t (int16_t ***data_mat, int Nx_data, int Ny_data)
{
  int i=0;
  int16_t **tmp=NULL;

  tmp=(int16_t **)malloc(Nx_data*sizeof(int16_t *));
  if (tmp==NULL)
  {
    printf("error allocating int16_t matrix\n");
    exit(11);
  }
  for (i=0 ; i<Nx_data ; i++)
  {
    create_vector_int16_t(&tmp[i], Ny_data);
  }
  *data_mat=tmp;
}

void create_0_matrix_int16_t (int16_t ***data_mat, int Nx_data, int Ny_data)
{
  int i=0;
  int16_t **tmp;
  tmp=(int16_t **)malloc(Nx_data*sizeof(int16_t *));
  if (tmp==NULL)
  {
    printf("error allocating 0 int16_t matrix\n");
    exit(11);
  }
  for (i=0 ; i<Nx_data ; i++)
  {
    create_0_vector_int16_t(&tmp[i], Ny_data);
  }
  *data_mat=tmp;
}


void resize_matrix_int16_t (int16_t ***data_mat, int Nx_data, int Ny_data, int Nx_old_data, int Ny_old_data)
{
  int i=0;
  int16_t **tmp=*data_mat;
  int16_t **tmp2=NULL;
  if (tmp==NULL)
  {
    printf("Warning, resizing, NULL pointer\n");
    create_matrix_int16_t(data_mat, Nx_data, Ny_data);
  }
  else
  {
    if (Nx_data<Nx_old_data)
    {
      for (i=Nx_data ; i<Nx_old_data ; i++)
      {
        delete_vector_int16_t(&tmp[i]);
      }
      tmp2=(int16_t **)realloc(tmp,Nx_data*sizeof(int16_t *));
      if (tmp2==NULL)
      {
        printf("error reallocating int16_t matrix\n");
        exit(12);
      }
      else
      {
        tmp=tmp2;
        for (i=0 ; i<Nx_data ; i++)
        {
          resize_vector_int16_t(&tmp[i], Ny_data);
        }
      }
      *data_mat=tmp;
    }
    else if(Nx_data>Nx_old_data)
    {
      tmp2=(int16_t **)realloc(tmp,Nx_data*sizeof(int16_t *));
      if (tmp2==NULL)
      {
        printf("error reallocating int16_t matrix\n");
        exit(12);
      }
      else
      {
        tmp=tmp2;
        for (i=0 ; i<Nx_old_data ; i++)
        {
          resize_vector_int16_t(&tmp[i], Ny_data);
        }
        for (i=Nx_old_data ; i<Nx_data ; i++)
        {
          create_vector_int16_t(&tmp[i], Ny_data);
        }
      }
      *data_mat=tmp;
    }
    else if (Ny_data!=Ny_old_data)
    {
      for (i=0 ; i<Nx_data ; i++)
      {
        resize_vector_int16_t(&tmp[i], Ny_data);
      }
      *data_mat=tmp;
    }
    else {printf("Warning, the size of int16_t matrix has not changed\n");}
  }
}

void delete_matrix_int16_t (int16_t ***data_mat, int Nx_data)
{
  int i=0;
  int16_t **tmp=*data_mat;
  if (tmp==NULL) {printf("Warning, deleting an already NULL pointer matrix\n");}
  else
  {
    for (i=0 ; i<Nx_data ; i++)
    {
      delete_vector_int16_t(&tmp[i]);
    }
    free(*data_mat);
    *data_mat=NULL;
  }
}


//float vector management
void create_vector_float (float **data_vec, int N_data)
{
  *data_vec=(float *)malloc(N_data*sizeof(float));
  if (*data_vec==NULL)
  {
    printf("Error allocating float vector\n");
    exit(11);
  }
}

void create_0_vector_float (float **data_vec, int N_data)
{
  *data_vec=(float *)calloc(N_data,sizeof(float));
  if (*data_vec==NULL)
  {
    printf("Error allocating 0 float vector\n");
    exit(11);
  }
}

void resize_vector_float (float **data_vec, int N_data)
{
  if (*data_vec==NULL)
  {
    printf("Warning, resizing a NULL vector pointer\n");
    create_vector_float(data_vec, N_data);
  }
  else
  {
    float *tmp=NULL;
    tmp=(float *)realloc(*data_vec,N_data*sizeof(float));
    if (tmp==NULL)
    {
      printf("Error reallocating float vector\n");
      delete_vector_float(data_vec);
    }
    else {*data_vec=tmp;}
  }
}

void delete_vector_float (float **data_vec)
{
  if (*data_vec==NULL) {printf("Warning, deleting an already NULL pointer vector\n");}
  else
  {
    free(*data_vec);
    *data_vec=NULL;
  }
}

//float matrix management
void create_matrix_float (float ***data_mat, int Nx_data, int Ny_data)
{
  int i=0;
  float **tmp=NULL;

  tmp=(float **)malloc(Nx_data*sizeof(float *));
  if (tmp==NULL)
  {
    printf("Error allocating float matrix\n");
    exit(11);
  }
  for (i=0 ; i<Nx_data ; i++)
  {
    create_vector_float(&tmp[i], Ny_data);
  }
  *data_mat=tmp;
}

void create_0_matrix_float (float ***data_mat, int Nx_data, int Ny_data)
{
  int i=0;
  float **tmp;
  tmp=(float **)malloc(Nx_data*sizeof(float *));
  if (tmp==NULL)
  {
    printf("Error allocating 0 float matrix\n");
    exit(11);
  }
  for (i=0 ; i<Nx_data ; i++)
  {
    create_0_vector_float(&tmp[i], Ny_data);
  }
  *data_mat=tmp;
}

void resize_matrix_float (float ***data_mat, int Nx_data, int Ny_data, int Nx_old_data, int Ny_old_data)
{
  int i=0;
  float **tmp=*data_mat;
  float **tmp2=NULL;
  if (tmp==NULL)
  {
    printf("Warning, resizing a NULL pointer float matrix\n");
    create_matrix_float(data_mat, Nx_data, Ny_data);
  }
  else
  {
    if (Nx_data<Nx_old_data)
    {
      for (i=Nx_data ; i<Nx_old_data ; i++)
      {
        delete_vector_float(&tmp[i]);
      }
      tmp2=(float **)realloc(tmp,Nx_data*sizeof(float *));
      if (tmp2==NULL)
      {
        printf("Error reallocating float matrix\n");
        exit(12);
      }
      else
      {
        tmp=tmp2;
        for (i=0 ; i<Nx_data ; i++)
        {
          resize_vector_float(&tmp[i], Ny_data);
        }
      }
      *data_mat=tmp;
    }
    else if(Nx_data>Nx_old_data)
    {
      tmp2=(float **)realloc(tmp,Nx_data*sizeof(float *));
      if (tmp2==NULL)
      {
        printf("Error reallocating float matrix\n");
        exit(12);
      }
      else
      {
        tmp=tmp2;
        for (i=0 ; i<Nx_old_data ; i++)
        {
          resize_vector_float(&tmp[i], Ny_data);
        }
        for (i=Nx_old_data ; i<Nx_data ; i++)
        {
          create_vector_float(&tmp[i], Ny_data);
        }
      }
      *data_mat=tmp;
    }
    else if (Ny_data!=Ny_old_data)
    {
      for (i=0 ; i<Nx_data ; i++)
      {
        resize_vector_float(&tmp[i], Ny_data);
      }
      *data_mat=tmp;
    }
    else {printf("Warning, the size of float matrix has not changed\n");}
  }
}

void delete_matrix_float (float ***data_mat, int Nx_data)
{
  int i=0;
  float **tmp=*data_mat;
  if (tmp==NULL) {printf("Warning, deleting an already NULL pointer matrix float\n");}
  else
  {
    for (i=0 ; i<Nx_data ; i++)
    {
      delete_vector_float(&tmp[i]);
    }
    free(*data_mat);
    *data_mat=NULL;
  }
}


//double vector management
void create_vector_double (double **data_vec, int N_data)
{
  *data_vec=(double *)malloc(N_data*sizeof(double));
  if (*data_vec==NULL)
  {
    printf("Error allocating double vector\n");
    exit(11);
  }
}

void create_0_vector_double (double **data_vec, int N_data)
{
  *data_vec=(double *)calloc(N_data,sizeof(double));
  if (*data_vec==NULL)
  {
    printf("Error allocating 0 double vector\n");
    exit(11);
  }
}

void resize_vector_double (double **data_vec, int N_data)
{
  if (*data_vec==NULL)
  {
    printf("Warning, resizing a NULL pointer double vector\n");
    create_vector_double(data_vec, N_data);
  }
  else
  {
    double *tmp=NULL;
    tmp=(double *)realloc(*data_vec,N_data*sizeof(double));
    if (tmp==NULL)
    {
      printf("Error reallocating double vector\n");
      delete_vector_double(data_vec);
    }
    else {*data_vec=tmp;}
  }
}

void delete_vector_double (double **data_vec)
{
  if (*data_vec==NULL) {printf("Warning, deleting an already NULL pointer double vector\n");}
  else
  {
    free(*data_vec);
    *data_vec=NULL;
  }
}

//double matrix management
void create_matrix_double (double ***data_mat, int Nx_data, int Ny_data)
{
  int i=0;
  double **tmp=NULL;

  tmp=(double **)malloc(Nx_data*sizeof(double *));
  if (tmp==NULL)
  {
    printf("Error allocating double matrix\n");
    exit(11);
  }
  for (i=0 ; i<Nx_data ; i++)
  {
    create_vector_double(&tmp[i], Ny_data);
  }
  *data_mat=tmp;
}

void create_0_matrix_double (double ***data_mat, int Nx_data, int Ny_data)
{
  int i=0;
  double **tmp;
  tmp=(double **)malloc(Nx_data*sizeof(double *));
  if (tmp==NULL)
  {
    printf("Error allocating double matrix\n");
    exit(11);
  }
  for (i=0 ; i<Nx_data ; i++)
  {
    create_0_vector_double(&tmp[i], Ny_data);
  }
  *data_mat=tmp;
}

void resize_matrix_double (double ***data_mat, int Nx_data, int Ny_data, int Nx_old_data, int Ny_old_data)
{
  int i=0;
  double **tmp=*data_mat;
  double **tmp2=NULL;
  if (tmp==NULL)
  {
    printf("Warning,resizing a NULL pointer double matrix\n");
    create_matrix_double(data_mat, Nx_data, Ny_data);
  }
  else
  {
    if (Nx_data<Nx_old_data)
    {
      for (i=Nx_data ; i<Nx_old_data ; i++)
      {
        delete_vector_double(&tmp[i]);
      }
      tmp2=(double **)realloc(tmp,Nx_data*sizeof(double *));
      if (tmp2==NULL)
      {
        printf("Error reallocating double matrix\n");
        exit(12);
      }
      else
      {
        tmp=tmp2;
        for (i=0 ; i<Nx_data ; i++)
        {
          resize_vector_double(&tmp[i], Ny_data);
        }
      }
      *data_mat=tmp;
    }
    else if(Nx_data>Nx_old_data)
    {
      tmp2=(double **)realloc(tmp,Nx_data*sizeof(double *));
      if (tmp2==NULL)
      {
        printf("Error reallocating double matrix\n");
        exit(12);
      }
      else
      {
        tmp=tmp2;
        for (i=0 ; i<Nx_old_data ; i++)
        {
          resize_vector_double(&tmp[i], Ny_data);
        }
        for (i=Nx_old_data ; i<Nx_data ; i++)
        {
          create_vector_double(&tmp[i], Ny_data);
        }
      }
      *data_mat=tmp;
    }
    else if (Ny_data!=Ny_old_data)
    {
      for (i=0 ; i<Nx_data ; i++)
      {
        resize_vector_double(&tmp[i], Ny_data);
      }
      *data_mat=tmp;
    }
    else {printf("Warning, the size of double matrix has not changed\n");}
  }
}

void delete_matrix_double (double ***data_mat, int Nx_data)
{
  int i=0;
  double **tmp=*data_mat;
  if (tmp==NULL) {printf("Warning, deleting an already NULL pointer double matrix\n");}
  else
  {
    for (i=0 ; i<Nx_data ; i++)
    {
      delete_vector_double(&tmp[i]);
    }
    free(*data_mat);
    *data_mat=NULL;
  }
}

//complex float vector management
void create_vector_cplxf (cplxf **data_vec, int N_data)
{
  *data_vec=(cplxf *)malloc(N_data*sizeof(cplxf));
  if (*data_vec==NULL)
  {
    printf("error allocating cplxf vector\n");
    exit(11);
  }
}

void create_0_vector_cplxf (cplxf **data_vec, int N_data)
{
  *data_vec=(cplxf *)calloc(N_data,sizeof(cplxf));
  if (*data_vec==NULL)
  {
    printf("error allocating cplxf 0 vector\n");
    exit(11);
  }
}

void resize_vector_cplxf (cplxf **data_vec, int N_data)
{
  if (*data_vec==NULL)
  {
    printf("warning, cplxf is already a NULL pointer\n");
    create_vector_cplxf(data_vec, N_data);
  }
  else
  {
    cplxf *tmp=NULL;
    tmp=(cplxf *)realloc(*data_vec,N_data*sizeof(cplxf));
    if (tmp==NULL)
    {
      printf("error reallocation cplxf vector\n");
      delete_vector_cplxf(data_vec);
    }
    else {*data_vec=tmp;}
  }
}

void delete_vector_cplxf (cplxf **data_vec)
{
  if (*data_vec==NULL) {printf("cplxf is already a NULL pointer\n");}
  else
  {
    free(*data_vec);
    *data_vec=NULL;
  }
}


//complex double vector management
void create_vector_cplxd (cplxd **data_vec, int N_data)
{
  *data_vec=(cplxd *)malloc(N_data*sizeof(cplxd));
  if (*data_vec==NULL)
  {
    printf("error allocating cplxd vector\n");
    exit(11);
  }
}

void create_0_vector_cplxd (cplxd **data_vec, int N_data)
{
  *data_vec=(cplxd *)calloc(N_data,sizeof(cplxd));
  if (*data_vec==NULL)
  {
    printf("error allocation cplxd 0 vector\n");
    exit(11);
  }
}

void resize_vector_cplxd (cplxd **data_vec, int N_data)
{
  if (*data_vec==NULL)
  {
    printf("warning, cplxd is a NULL pointer\n");
    create_vector_cplxd(data_vec, N_data);
  }
  else
  {
    cplxd *tmp=NULL;
    tmp=(cplxd *)realloc(*data_vec,N_data*sizeof(cplxd));
    if (tmp==NULL)
    {
      printf("error reallocating cplxd vector\n");
      delete_vector_cplxd(data_vec);
    }
    else {*data_vec=tmp;}
  }
}

void delete_vector_cplxd (cplxd **data_vec)
{
  if (*data_vec==NULL) {printf("cplxd is already a NULL pointer\n");}
  else
  {
    free(*data_vec);
    *data_vec=NULL;
  }
}

#endif
