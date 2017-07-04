package com.echopen.asso.echopen.model.Ruler;

import com.echopen.asso.echopen.utils.Constants;

/**
 * Created by mehdibenchoufi on 28/06/16.
 */
public class Ruler {

    private int screenSize = Constants.RulerParam.height;

    static public float pixelStep = Constants.RulerParam.step;

    private int width;

    private int height;

    private Point point;

    private float rulerSize;

    public int getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(int screenSize) {
        this.screenSize = screenSize;
    }

    public static float getPixelStep() {
        return pixelStep;
    }

    public static void setPixelStep(float pixelStep) {
        Ruler.pixelStep = pixelStep;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public float getRulerSize() {
        return rulerSize;
    }

    public void setRulerSize(float rulerSize) {
        this.rulerSize = rulerSize;
    }


    public Ruler() {
        point = new Point();
    }

    public class Point{

        private int midScreenPoint;

        private float mainPoint = 0;

        private float mainPointClone = 0;

        private int endPoint;

        public int getMidScreenPoint() {
            return midScreenPoint;
        }

        public void setMidScreenPoint(int midScreenPoint) {
            this.midScreenPoint = midScreenPoint;
        }

        public float getMainPoint() {
            return mainPoint;
        }

        public void setMainPoint(float mainPoint) {
            this.mainPoint = mainPoint;
        }

        public float getMainPointClone() {
            return mainPointClone;
        }

        public void setMainPointClone(float mainPointClone) {
            this.mainPointClone = mainPointClone;
        }

        public int getEndPoint() {
            return endPoint;
        }

        public void setEndPoint(int endPoint) {
            this.endPoint = endPoint;
        }
    }
}
