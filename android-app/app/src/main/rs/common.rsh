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