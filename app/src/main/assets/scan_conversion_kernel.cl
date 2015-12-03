uchar4 convertYtoRGBA(int y);

__kernel void scanConverter( __global uchar4* out,
                            int size,
                          __global uchar*  envelope_data,
                          int length,
                          int im_width,
                          int im_height,
                          int N_samples,
                          __global int* index_samp_line,
                          __global int* image_index,
                          __global float* weight_coef,
                          int n_values)
{
    int           ij_index_coef;
    __global uchar *env_pointer;
    __global float *weight_pointer;

    int i = get_global_id(0);
    int gx	= get_global_id(0);
    int gy	= get_global_id(1);
    int inIdx= gy*im_width+gx;

    ij_index_coef = 4 *inIdx;
    weight_pointer = &(weight_coef[ij_index_coef]);
    env_pointer = &(envelope_data[index_samp_line[inIdx]]);

    int y  = (0xFF & ((int) (weight_pointer[0] * env_pointer[0]
                                                  + weight_pointer[1] * env_pointer[1]
                                                  + weight_pointer[2] * env_pointer[N_samples]

                                                  + weight_pointer[3] * env_pointer[N_samples+1] + ((uchar)0.5))));
    out[image_index[inIdx]]
            =         convertYtoRGBA(y);
}

uchar4 convertYtoRGBA(int y)
{
    uchar4 ret;
    int b = y;
    int g = y;
    int r = y;
    ret.x = r>255? 255 : r<0 ? 0 : r;
    ret.y = g>255? 255 : g<0 ? 0 : g;
    ret.z = b>255? 255 : b<0 ? 0 : b;
    ret.w = 0;
    return ret;
}

