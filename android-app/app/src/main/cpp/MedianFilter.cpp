/**
 * @file MedianFilter.cpp
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
#include "MedianFilter.hpp"
#include <algorithm>
#include <functional>

void MedianFilter::setUp(std::shared_ptr<ThreadPool> iThreadPool)
{
  mThreadPool = iThreadPool;
}

void MedianFilter::applyFilter(const Image2D<float> &iImageInput, Image2D<float> &oImageOutput)
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
        oImageOutput.mData[i][j] = getMedianOn3x3Neighboors(iImageInput.mData, i - 1, i + 1, j - 1, j + 1);
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

void MedianFilter::applyBorders(const Image2D<float> &iImageInput, Image2D<float> &oImageOutput)
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

float MedianFilter::getMedianOn3x3Neighboors(float ** iImage, int iTop, int iBottom, int iLeft, int iRight)
{
  std::vector<float> lNeighboors;
  for(int i = iTop; i <= iBottom; i++)
  {
    for(int j = iLeft; j <= iRight; j++)
    {
      lNeighboors.push_back(iImage[i][j]);
    }
  }

  const auto lMedianIt = lNeighboors.begin() + lNeighboors.size() / 2;
  std::nth_element(lNeighboors.begin(), lMedianIt , lNeighboors.end());
  return *lMedianIt;
}
