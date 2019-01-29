/**
 * @file LinearQuantificationFilter.cpp
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
#include "LinearQuantificationFilter.hpp"

void LinearQuantificationFilter::setUp(std::shared_ptr<ThreadPool> iThreadPool)
{
  mThreadPool = iThreadPool;
}

void LinearQuantificationFilter::applyFilter(const Image2D<float> &iImageInput, Image2D<int> &oImageOutput, float iLowerBound, float iUpperBound)
{
  int lImageWidth = iImageInput.getWidth();
  int lImageHeight = iImageInput.getHeight();

  if(lImageWidth != oImageOutput.getWidth() || lImageHeight != oImageOutput.getHeight())
  {
    std::cerr << "Image data format invalid - Input/Output" << std::endl;
    return;
  }


  std::vector< std::future<bool> > lResults;
  // lambda used to process partial data packet
  // TODO: may required optimisation on data input / output
  auto partialLinearQuantification = [&](int iStartLine, int iEndLine, float iLowerBound, float iUpperBound)
  {
    float lSlope = MaxPixelValue / (iUpperBound - iLowerBound);

    for(int i = iStartLine; i < iEndLine; i ++)
    {
      for(int j = 0; j < iImageInput.getWidth(); j ++)
      {
        if(iImageInput.mData[i][j] <= iLowerBound)
        {
          oImageOutput.mData[i][j] = MinPixelValue | MinPixelValue << 8 | MinPixelValue << 16 | 0xFF000000;
        }
        else if(iImageInput.mData[i][j] >= iUpperBound)
        {
          oImageOutput.mData[i][j] = MaxPixelValue | MaxPixelValue << 8 | MaxPixelValue << 16 | 0xFF000000;
        }
        else
        {
          int lPixelValue =(iImageInput.mData[i][j] - iLowerBound) * lSlope ;
          oImageOutput.mData[i][j] = lPixelValue | lPixelValue << 8 | lPixelValue << 16 | 0xFF000000;
        }
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
      mThreadPool->enqueue(partialLinearQuantification, iStartLine, iEndLine, iLowerBound, iUpperBound)
    );
  }

  for(auto && lResult: lResults)
  {
    lResult.wait();
  }

}
