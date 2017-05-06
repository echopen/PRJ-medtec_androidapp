package com.echopen.asso.echopen.model.EchoImage;

import android.graphics.Bitmap;

/**
 * Created by mehdibenchoufi on 21/10/15.
 */
interface EchoImageVisitor {

    Bitmap visit(EchoIntImage echoIntImage);

    Bitmap visit(EchoCharImage echoCharImage);

}
