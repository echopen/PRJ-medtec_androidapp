/*
 * Copyright (C) 2011-2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * This file is auto-generated. DO NOT MODIFY!
 * The source Renderscript file: /Users/mehdibenchoufi/Desktop/Sites/echopen/tools/GPUChallenge/app/src/main/rs/scanconversion.rs
 */

package com.asso.echopen.gpuchallenge;

import android.support.v8.renderscript.*;
import com.asso.echopen.gpuchallenge.scanconversionBitCode;

/**
 * @hide
 */
public class ScriptC_scanconversion extends ScriptC {
    private static final String __rs_resource_name = "scanconversion";
    // Constructor
    public  ScriptC_scanconversion(RenderScript rs) {
        super(rs,
              __rs_resource_name,
              scanconversionBitCode.getBitCode32(),
              scanconversionBitCode.getBitCode64());
        __U32 = Element.U32(rs);
        mExportVar_pixelsCount = 262144;
        mExportVar_numLines = 128;
    }

    private Element __U32;
    private FieldPacker __rs_fp_U32;
    private final static int mExportVarIdx_counter = 0;
    private long mExportVar_counter;
    public synchronized void set_counter(long v) {
        if (__rs_fp_U32!= null) {
            __rs_fp_U32.reset();
        } else {
            __rs_fp_U32 = new FieldPacker(4);
        }
        __rs_fp_U32.addU32(v);
        setVar(mExportVarIdx_counter, __rs_fp_U32);
        mExportVar_counter = v;
    }

    public long get_counter() {
        return mExportVar_counter;
    }

    public Script.FieldID getFieldID_counter() {
        return createFieldID(mExportVarIdx_counter, null);
    }

    private final static int mExportVarIdx_weight_index = 1;
    private long mExportVar_weight_index;
    public synchronized void set_weight_index(long v) {
        if (__rs_fp_U32!= null) {
            __rs_fp_U32.reset();
        } else {
            __rs_fp_U32 = new FieldPacker(4);
        }
        __rs_fp_U32.addU32(v);
        setVar(mExportVarIdx_weight_index, __rs_fp_U32);
        mExportVar_weight_index = v;
    }

    public long get_weight_index() {
        return mExportVar_weight_index;
    }

    public Script.FieldID getFieldID_weight_index() {
        return createFieldID(mExportVarIdx_weight_index, null);
    }

    private final static int mExportVarIdx_pixelsCount = 2;
    private long mExportVar_pixelsCount;
    public synchronized void set_pixelsCount(long v) {
        if (__rs_fp_U32!= null) {
            __rs_fp_U32.reset();
        } else {
            __rs_fp_U32 = new FieldPacker(4);
        }
        __rs_fp_U32.addU32(v);
        setVar(mExportVarIdx_pixelsCount, __rs_fp_U32);
        mExportVar_pixelsCount = v;
    }

    public long get_pixelsCount() {
        return mExportVar_pixelsCount;
    }

    public Script.FieldID getFieldID_pixelsCount() {
        return createFieldID(mExportVarIdx_pixelsCount, null);
    }

    private final static int mExportVarIdx_numLines = 3;
    private long mExportVar_numLines;
    public synchronized void set_numLines(long v) {
        if (__rs_fp_U32!= null) {
            __rs_fp_U32.reset();
        } else {
            __rs_fp_U32 = new FieldPacker(4);
        }
        __rs_fp_U32.addU32(v);
        setVar(mExportVarIdx_numLines, __rs_fp_U32);
        mExportVar_numLines = v;
    }

    public long get_numLines() {
        return mExportVar_numLines;
    }

    public Script.FieldID getFieldID_numLines() {
        return createFieldID(mExportVarIdx_numLines, null);
    }

    private final static int mExportVarIdx_envelope_data = 4;
    private Allocation mExportVar_envelope_data;
    public void bind_envelope_data(Allocation v) {
        mExportVar_envelope_data = v;
        if (v == null) bindAllocation(null, mExportVarIdx_envelope_data);
        else bindAllocation(v, mExportVarIdx_envelope_data);
    }

    public Allocation get_envelope_data() {
        return mExportVar_envelope_data;
    }

    private final static int mExportVarIdx_index_samp_line = 5;
    private Allocation mExportVar_index_samp_line;
    public void bind_index_samp_line(Allocation v) {
        mExportVar_index_samp_line = v;
        if (v == null) bindAllocation(null, mExportVarIdx_index_samp_line);
        else bindAllocation(v, mExportVarIdx_index_samp_line);
    }

    public Allocation get_index_samp_line() {
        return mExportVar_index_samp_line;
    }

    private final static int mExportVarIdx_weight_coef = 6;
    private Allocation mExportVar_weight_coef;
    public void bind_weight_coef(Allocation v) {
        mExportVar_weight_coef = v;
        if (v == null) bindAllocation(null, mExportVarIdx_weight_coef);
        else bindAllocation(v, mExportVarIdx_weight_coef);
    }

    public Allocation get_weight_coef() {
        return mExportVar_weight_coef;
    }

    private final static int mExportVarIdx_output_image = 7;
    private Allocation mExportVar_output_image;
    public void bind_output_image(Allocation v) {
        mExportVar_output_image = v;
        if (v == null) bindAllocation(null, mExportVarIdx_output_image);
        else bindAllocation(v, mExportVarIdx_output_image);
    }

    public Allocation get_output_image() {
        return mExportVar_output_image;
    }

    private final static int mExportVarIdx_image_index = 8;
    private Allocation mExportVar_image_index;
    public void bind_image_index(Allocation v) {
        mExportVar_image_index = v;
        if (v == null) bindAllocation(null, mExportVarIdx_image_index);
        else bindAllocation(v, mExportVarIdx_image_index);
    }

    public Allocation get_image_index() {
        return mExportVar_image_index;
    }

    private final static int mExportVarIdx_index_counter = 9;
    private Allocation mExportVar_index_counter;
    public void bind_index_counter(Allocation v) {
        mExportVar_index_counter = v;
        if (v == null) bindAllocation(null, mExportVarIdx_index_counter);
        else bindAllocation(v, mExportVarIdx_index_counter);
    }

    public Allocation get_index_counter() {
        return mExportVar_index_counter;
    }

    //private final static int mExportForEachIdx_root = 0;
    private final static int mExportForEachIdx_scan_convert = 1;
    public Script.KernelID getKernelID_scan_convert() {
        return createKernelID(mExportForEachIdx_scan_convert, 33, null, null);
    }

    public void forEach_scan_convert(Allocation ain) {
        forEach_scan_convert(ain, null);
    }

    public void forEach_scan_convert(Allocation ain, Script.LaunchOptions sc) {
        // check ain
        if (!ain.getType().getElement().isCompatible(__U32)) {
            throw new RSRuntimeException("Type mismatch with U32!");
        }
        forEach(mExportForEachIdx_scan_convert, ain, null, null, sc);
    }

    private final static int mExportFuncIdx_set_PixelsCount = 0;
    public Script.InvokeID getInvokeID_set_PixelsCount() {
        return createInvokeID(mExportFuncIdx_set_PixelsCount);
    }

    public void invoke_set_PixelsCount(int val) {
        FieldPacker set_PixelsCount_fp = new FieldPacker(4);
        set_PixelsCount_fp.addI32(val);
        invoke(mExportFuncIdx_set_PixelsCount, set_PixelsCount_fp);
    }

    private final static int mExportFuncIdx_set_NumLines = 1;
    public Script.InvokeID getInvokeID_set_NumLines() {
        return createInvokeID(mExportFuncIdx_set_NumLines);
    }

    public void invoke_set_NumLines(int val) {
        FieldPacker set_NumLines_fp = new FieldPacker(4);
        set_NumLines_fp.addI32(val);
        invoke(mExportFuncIdx_set_NumLines, set_NumLines_fp);
    }

    private final static int mExportFuncIdx_process = 2;
    public Script.InvokeID getInvokeID_process() {
        return createInvokeID(mExportFuncIdx_process);
    }

    public void invoke_process(Allocation index_counter) {
        FieldPacker process_fp = new FieldPacker(32);
        process_fp.addObj(index_counter);
        invoke(mExportFuncIdx_process, process_fp);
    }

}

