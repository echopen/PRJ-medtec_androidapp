# We want a recent CMake file
cmake_minimum_required(VERSION 3.4.1)

# The project
project(renderingCpp)

# Create variables with files to be compiled
set(SRCS
        scan_conversion_float.cpp
        rendering.cpp
        LinearQuantificationFilter.cpp
        Convolution3x3Filter.cpp
        MedianFilter.cpp
        )

set(HEADERS
        scan_conversion_float.hpp
        pointer_management.hpp
        ThreadPool.h
        Image2D.hpp
        LinearQuantificationFilter.hpp
        Convolution3x3Filter.hpp
        MedianFilter.hpp
        )

#Compile with debug symbol
set(CMAKE_BUILD_TYPE Debug)

add_library(renderingCpp SHARED ${SRCS} ${HEADERS})

target_link_libraries(renderingCpp
                      android
                      log)

