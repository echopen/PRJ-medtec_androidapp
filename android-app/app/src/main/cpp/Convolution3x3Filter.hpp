/**
 * @file Convolution3x3Filter.hpp
 * @author  clecoued <clement@echopen.org>
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
#include <memory>
#include "ThreadPool.h"
#include "Image2D.hpp"



class Convolution3x3Filter
{
  public:
    Convolution3x3Filter(){}
    ~Convolution3x3Filter(){}

    void setUp(std::shared_ptr<ThreadPool> iThreadPool, float iKernel[][3]);
    void applyFilter(const Image2D<float> &iImageInput, Image2D<float> &oImageOutput);

  private:
    const static int NumberOfPackets = 8;

    std::shared_ptr<ThreadPool> mThreadPool;
    float mKernel[3][3] = {{1.f/9.f, 1.f/9.f, 1.f/9.f},{1.f/9.f, 1.f/9.f, 1.f/9.f},{1.f/9.f, 1.f/9.f, 1.f/9.f}};

    // apply filter to borders
    void applyBorders(const Image2D<float> &iImageInput, Image2D<float> &oImageOutput);
    float getWeightedValueOn3x3Sector(float ** iImage, int iX, int iY);
};
