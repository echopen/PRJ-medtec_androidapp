/*Copyright (c) 2013 The Regents of the University of California.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above
 *    copyright notice, this list of conditions and the following
 *    disclaimer in the documentation and/or other materials provided
 *    with the distribution.
 * 3. Neither the name of the University of California nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE REGENTS
 * OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
*/


#pragma once

//Functions Shared between FFT and IFFT

// Return A*B
static inline float2 mul(float2 a,float2 b)
{
  return (float2){mad(a.x,b.x,-a.y*b.y),mad(a.x,b.y,a.y*b.x)}; // mad
}

// Return A * exp(K*ALPHA*i)
static inline float2 twiddle(float2 a, uint32_t k,float alpha)
{
  float cs,sn;
  sn = sincos((float)k*alpha,&cs);
  return mul(a,(float2){cs,sn});
}

static inline float2 conj(const float2 in)
{
	return (float2){in.x, -in.y};
} 

static float abs_im(const float2 v){
	return sqrt(v.x*v.x + v.y*v.y);
}


// In-place DFT-2, output is (a,b). Arguments must be variables.
#define DFT2(a,b) { float2 tmp = a - b; a += b; b = tmp; }