package com.rubik;

public class GLColor {	// 魔方的颜色类

    public final float red;
    public final float green;
    public final float blue;
    public final float alpha;
    
    public static GLColor RED = new GLColor(1, 0, 0, 1);
    public static GLColor GREEN = new GLColor(0, 1, 0 , 1);
    public static GLColor BLUE = new GLColor(0, 0, 1, 1);
    public static GLColor YELLOW = new GLColor(1, 1, 0, 1);
    public static GLColor ORANGE = new GLColor(1, 0.5f,0,1);
    public static GLColor WHITE = new GLColor(1, 1, 1, 1);
    public static GLColor BLACK = new GLColor(0, 0, 0, 1);
    
    public GLColor(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public GLColor(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = 1;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof GLColor) {
            GLColor color = (GLColor)other;
            return (red == color.red &&
                    green == color.green &&
                    blue == color.blue &&
                    alpha == color.alpha);
        }
        return false;
    }
}

