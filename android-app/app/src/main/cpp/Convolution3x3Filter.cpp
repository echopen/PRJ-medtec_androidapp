/**
 * @file Convolution3x3Filter.cpp
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


#include "Convolution3x3Filter.hpp"

void Convolution3x3Filter::setUp(std::shared_ptr<ThreadPool> iThreadPool, float iKernel[][3])
{
  mThreadPool = iThreadPool;
  float lSum = 0.f;

  for(int i = 0; i < 3; i++)
  {
    for(int j = 0; j < 3; j++)
    {
      mKernel[i][j] = iKernel[i][j];
      lSum += mKernel[i][j];
    }
  }

  // normalize the kernel
  for(int i = 0; i < 3; i++)
  {
    for(int j = 0; j < 3; j++)
    {
      mKernel[i][j] = mKernel[i][j] / lSum;
    }
  }
}

void Convolution3x3Filter::applyFilter(const Image2D<float> &iImageInput, Image2D<float> &oImageOutput)
{
  int lImageWidth = iImageInput.getWidth();
  int lImageHeight = iImageInput.getHeight();

  if(lImageWidth != oImageOutput.getWidth() || lImageHeight != oImageOutput.getHeight())
  {
    std::cerr << "Image data format invalid - Input/Output" << std::endl;
    return;
  }

  // apply filter to borders
  applyBorders(iImageInput, oImageOutput);

  std::vector< std::future<bool> > lResults;
  // lambda used to process partial data packet
  // TODO: may required optimisation on data input / output
  auto partialMedianFilter = [&](int iStartLine, int iEndLine)
  {
    // ignore image edges
    if(iStartLine == 0)
    {
      iStartLine++;
    }

    if(iEndLine == oImageOutput.getHeight())
    {
      iEndLine--;
    }

    for(int i = iStartLine; i < iEndLine; i ++)
    {
      for(int j = 1; j < iImageInput.getWidth() - 1; j ++)
      {
        oImageOutput.mData[i][j] = getWeightedValueOn3x3Sector(iImageInput.mData, i, j);
      }
    }

      return true;
  };

  int lPacketSize = lImageHeight / NumberOfPackets + 1;
  int iStartLine = 0, iEndLine = 0;
  for(int i = 0; i < NumberOfPackets; i++)
  {
    iStartLine = i * lPacketSize;
    iEndLine = std::min((i+1)*lPacketSize, lImageHeight);
    lResults.emplace_back(
      mThreadPool->enqueue(partialMedianFilter, iStartLine, iEndLine)
    );
  }

  for(auto && lResult: lResults)
  {
    lResult.wait();
  }

}

void Convolution3x3Filter::applyBorders(const Image2D<float> &iImageInput, Image2D<float> &oImageOutput)
{
  int lImageWidth = iImageInput.getWidth();
  int lImageHeight = iImageInput.getHeight();

  for(int i = 0; i < lImageWidth; i ++)
  {
    oImageOutput.mData[0][i] = iImageInput.mData[0][i];
    oImageOutput.mData[lImageHeight - 1][i] = iImageInput.mData[lImageHeight - 1][i];
  }

  for(int i = 0; i < lImageHeight; i ++)
  {
    oImageOutput.mData[i][0] = iImageInput.mData[i][0];
    oImageOutput.mData[i][lImageWidth - 1] = iImageInput.mData[i][lImageWidth - 1];
  }
}

float Convolution3x3Filter::getWeightedValueOn3x3Sector(float ** iImage, int iX, int iY)
{
  float lWeightedSum = 0.f;

  int iX0 = iX - 1; int iY0;
  for(int i = 0;  i < 3; i++)
  {
    iY0 = iY - 1;
    for(int j = 0; j < 3; j++)
    {
      lWeightedSum += iImage[iX0][iY0] * mKernel[i][j];
      iY0 ++;
    }
    iX0 ++;
  }

  return lWeightedSum;
}
