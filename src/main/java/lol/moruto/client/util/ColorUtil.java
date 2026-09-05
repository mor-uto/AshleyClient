package lol.moruto.mod.util;

import java.awt.*;

public class ColorUtil {
    public static int getRainbowColor(float offset) {
        return Color.HSBtoRGB((System.currentTimeMillis() % 10000L / 10000F + offset), 1.0f, 1.0f);
    }

    public static int rgbToHex(int r, int g, int b) {
        return (255 << 24) | (r << 16) | (g << 8) | b;
    }

    public static int rgbaToHex(int r, int g, int b, int a) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static int darken(int color, float amount) {
        Color c = new Color(color, true);
        int r = Math.max(0, (int)(c.getRed() * (1 - amount)));
        int g = Math.max(0, (int)(c.getGreen() * (1 - amount)));
        int b = Math.max(0, (int)(c.getBlue() * (1 - amount)));
        return (c.getAlpha() << 24) | (r << 16) | (g << 8) | b;
    }
}
