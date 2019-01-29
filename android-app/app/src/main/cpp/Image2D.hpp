/**
 * @file Image2D.hpp
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
#ifndef __IMAGE_HPP__
#define __IMAGE_HPP__

#include <iostream>

template <class T>
class Image2D
{
  private:
    int mWidth;
    int mHeight;

  public:
    Image2D(int iWidth, int iHeight): mWidth(iWidth), mHeight(iHeight)
    {
      mData = new T*[mHeight];
      for(int i = 0; i < mHeight; i++)
      {
        mData[i] = new T[mWidth];
      }
    }

    ~Image2D()
    {
      for(int i = 0; i < mHeight; i++){
        delete mData[i];
      }

      delete mData;
    }

    int getWidth() const
    {
      return mWidth;
    }

    int getHeight() const
    {
      return mHeight;
    }

    T** mData;

    template <typename U>      // all instantiations of this template are my friends
    friend std::ostream& operator<<( std::ostream&, const Image2D<U>& );

};


template <class T>
inline std::ostream& operator<< (std::ostream& oOS, const Image2D<T>& iImage)
{
  return oOS;
}

template <>
inline std::ostream& operator<< <float>(std::ostream& oOS, const Image2D<float>& iImage)
{
  for(int i = 0; i < iImage.getHeight(); i++)
  {
    for(int j = 0; j < iImage.getWidth(); j++)
    {
      oOS << iImage.mData[i][j] << " ";
    }
    oOS << std::endl;
  }

  return oOS;
}

template <>
inline std::ostream& operator<< <int>(std::ostream& oOS, const Image2D<int>& iImage)
{
  for(int i = 0; i < iImage.getHeight(); i++)
  {
    for(int j = 0; j < iImage.getWidth(); j++)
    {
      oOS << "{" << ((iImage.mData[i][j]) & 0xff) << "," << ((iImage.mData[i][j] >> 8) & 0xff) << "," << ((iImage.mData[i][j] >> 16) & 0xff) << "} ";
    }
    oOS << std::endl;
  }

  return oOS;
}

#endif
