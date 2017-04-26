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

#pragma version(1)
#pragma rs java_package_name(com.android.rs)

#include "common.rsh"

uint32_t t = 0;
uint32_t finalFFT = 0;
void root(const float2 *in, float2 *out, const void *usrData, uint32_t x){
  uint32_t p = *(uint32_t *)usrData;
  uint32_t k = x&(p-1);            // index in input sequence, in 0..P-1
  uint32_t j = ((x-k)<<1) + k;     // output index
  float alpha = -M_PI*(float)k/(float)p;
  
  float2 u0 = in[0];
  float2 u1 = twiddle(in[t],1,alpha);

  // In-place DFT-2
  DFT2(u0,u1);
  out += j-x;
  // Write output
  out[0] = u0;
  out[p] = u1;
}

void runRestricted(rs_script fftScript, rs_allocation in_alloc, rs_allocation out_alloc) {
	 uint32_t numElements = rsAllocationGetDimX(in_alloc);
	 t = numElements / 2;
     struct rs_script_call restrict_for;
     restrict_for.strategy = RS_FOR_EACH_STRATEGY_DONT_CARE;
     restrict_for.xStart = 0;
     restrict_for.xEnd = t;
     restrict_for.yStart = 0;
     restrict_for.yEnd = rsAllocationGetDimY(in_alloc);
     restrict_for.zStart = 0;
     restrict_for.zEnd = 0;
     restrict_for.arrayStart = 0;
     restrict_for.arrayEnd = 0;
     
     uint32_t aIndex = 1; // I/O A initially placed in index 0 after mod operation
     for(uint32_t p = 1; p < numElements; p=p*2){
     	aIndex = (aIndex + 1) % 2;
     	if (aIndex == 0){
     		rsForEach(fftScript, in_alloc, out_alloc, &p, sizeof(uint32_t), &restrict_for); 
     	} else {
     		rsForEach(fftScript,  out_alloc, in_alloc, &p, sizeof(uint32_t), &restrict_for);
     	}
     }
     
     finalFFT = aIndex;
}

