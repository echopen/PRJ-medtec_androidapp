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

