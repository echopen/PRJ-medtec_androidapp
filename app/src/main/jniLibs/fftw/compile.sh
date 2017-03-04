#!/bin/sh -xe

NDK=/home/max/Src/android/adk/android-ndk-r8e
PREBUILT=$NDK/toolchains/arm-linux-androideabi-4.4.3/prebuilt/linux-x86_64
PLATFORM=$NDK/platforms/android-8/arch-arm

function build_one
{

export SYS_ROOT=$PLATFORM
export CC="$PREBUILT/bin/arm-linux-androideabi-gcc --sysroot=$SYS_ROOT"
export CFLAGS="-fpic -DANDROID $OPTIMIZE_CFLAGS"
export NM="$PREBUILT/bin/arm-linux-androideabi-nm"
export AR="$PREBUILT/bin/arm-linux-androideabi-ar"
export LD="$PREBUILT/bin/arm-linux-androideabi-ld"
export LIBS="-lc -lm -ldl -llog -lgcc"
export RANLIB="$PREBUILT/bin/arm-linux-androideabi-ranlib"
export STRIP="$PREBUILT/bin/arm-linux-androideabi-strip"

./configure \
    --prefix=$PREFIX \
    --disable-shared \
    --enable-static \
    --enable-threads \
    --with-combined-threads \
    --host=arm-eabi
    
make -j16
make install
#$PREBUILT/bin/arm-linux-androideabi-ar d libavcodec/libavcodec.a inverse.o
$PREBUILT/bin/arm-linux-androideabi-ld -rpath-link=$PLATFORM/usr/lib -L$PLATFORM/usr/lib  -soname libfftw3.so -shared -nostdlib  -z,noexecstack -Bsymbolic --whole-archive --no-undefined -o $PREFIX/lib/libfftw3.so android/armv6/lib/libfftw3.a -lc -lm -lz -ldl -llog  --warn-once  --dynamic-linker=/system/bin/linker $PREBUILT/lib/gcc/arm-linux-androideabi/4.4.3/libgcc.a
}

#arm v6
CPU=armv6
OPTIMIZE_CFLAGS="-marm -march=$CPU"
PREFIX=$(pwd)/android/$CPU 
ADDITIONAL_CONFIGURE_FLAG=
build_one

