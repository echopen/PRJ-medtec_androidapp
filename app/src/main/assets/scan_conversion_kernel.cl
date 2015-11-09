__kernel void scanConverter( __global uchar4* out,
                            int size,
                          __global uchar*  envelope_data,
                          int length,
                          int im_width,
                          int im_height,
                          int N_samples,
                          int* index_samp_line,
                          int* image_index,
                          __global double* weight_coef,
                          int n_values)
{
    int           ij_index_coef;
    unsigned char *env_pointer;   
    float         *weight_pointer;

    int i = get_global_id(0);
    ij_index_coef = 4 *i;
    weight_pointer = &(weight_coef[ij_index_coef]);
    env_pointer = &(envelope_data[index_samp_line[i]]);
    image[image_index[i]]
            =         weight_pointer[0] * env_pointer[0]
                      + weight_pointer[1] * env_pointer[1]
                      + weight_pointer[2] * env_pointer[N_samples]
                      + weight_pointer[3] * env_pointer[N_samples+1] + 0.5;
}

