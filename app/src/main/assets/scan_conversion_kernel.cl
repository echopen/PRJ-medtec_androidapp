__kernel void scanConverter( __global uchar* out,
                            int size,
                          __global uchar4*  envelope_data,
                          int length,
                          int im_width,
                          int im_height,
                          int N_samples,
                          __global uchar* index_samp_line,
                          __global uchar* image_index,
                          __global uchar* weight_coef,
                          int n_values)
{
    int           ij_index_coef;
    __global uint *env_pointer;
    __global uchar *weight_pointer;

    int i = get_global_id(0);
    int gx	= get_global_id(0);
    int gy	= get_global_id(1);
    int inIdx= gy*im_width+gx;

    ij_index_coef = 4 *inIdx;
    weight_pointer = &(weight_coef[ij_index_coef]);
    env_pointer = &(envelope_data[index_samp_line[inIdx]]);
    out[image_index[inIdx]]
            =         weight_pointer[0] * env_pointer[0]
                      + weight_pointer[1] * env_pointer[1]
                      + weight_pointer[2] * env_pointer[N_samples]
                      + weight_pointer[3] * env_pointer[N_samples+1] + 0.5;
}

