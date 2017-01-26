#pragma version(1)
#pragma rs java_package_name(com.asso.echopen.gpuchallenge)
#pragma rs_fp_relaxed

uint32_t counter;
uint32_t weight_index;

uint32_t pixelsCount = 262144;
uint32_t numLines = 128;
int32_t *envelope_data;
int32_t *index_samp_line;
double *weight_coef;
uint32_t *output_image;
uint32_t *image_index;
uint32_t *index_counter;

void init(){
  counter = 0;
}

void set_PixelsCount(int val) {
    // more processing in real life
    rsDebug("===========pixelsCount==================", val);
    //rsDebug("===========EnvelopeData==================", envelope_data);
    pixelsCount = (uint32_t) val;
}

void set_NumLines(int val) {
    // more processing in real life
    rsDebug("===========numLines==================", val);
    numLines = (uint32_t) val;
}

//void RS_KERNEL scan_convert(uint in) {
// rsAtomicInc(&counter);
//if(counter %4 == 0)
//          weight_index = (counter-1)*4;
  //rsDebug("===========Counter==================", counter);

//  uint32_t env_index = index_samp_line[counter-1];
//  uint32_t index = image_index[counter-1];

//  output_image[index] = (int)(weight_coef[weight_index] * envelope_data[env_index]
//                  + weight_coef[weight_index + 1] * envelope_data[env_index + 1]
//                  + weight_coef[weight_index + 2] * envelope_data[env_index + numLines]
//                  + weight_coef[weight_index + 3] * envelope_data[env_index + numLines + 1]
//                  + 0.5);
//}

//void process(rs_allocation image_index) {
//  rsForEach(scan_convert, image_index);
//}

void RS_KERNEL scan_convert(uint in) {

  //rsDebug("===========Counter==================", counter);

  uint32_t env_index = index_samp_line[in];
  uint32_t index = image_index[in];
  if(in %4 == 0) {
    uint32_t weight_index = 4*(in-1);
    }

  output_image[index] = (int)(weight_coef[weight_index] * envelope_data[env_index]
                 + weight_coef[weight_index + 1] * envelope_data[env_index + 1]
                  + weight_coef[weight_index + 2] * envelope_data[env_index + numLines]
                  + weight_coef[weight_index + 3] * envelope_data[env_index + numLines + 1]
                  + 0.5);
}

void process(rs_allocation index_counter) {
  rsForEach(scan_convert, index_counter);
}
