#pragma version(1)
#pragma rs java_package_name(com.echopen.asso.echopen.gpuchallenge)
#pragma rs_fp_relaxed

int32_t num_samples_per_line = 0;
int32_t *envelope_data;
int32_t *index_samp_line;
double *weight_coef;
int32_t *output_image;
int32_t *image_index;
int32_t *index_counter;
int32_t *out_index_counter;

void RS_KERNEL scan_convert(int in) {

  int32_t env_index = index_samp_line[in];
  int32_t index = image_index[in];
  int32_t weight_index = 4 * in;

  output_image[index] = (int)(weight_coef[weight_index] * envelope_data[env_index]
                 + weight_coef[weight_index + 1] * envelope_data[env_index + 1]
                  + weight_coef[weight_index + 2] * envelope_data[env_index + num_samples_per_line]
                  + weight_coef[weight_index + 3] * envelope_data[env_index + num_samples_per_line + 1]
                  + 0.5);

}