package dev.cerus.mcheadrender.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Grayscales an image
 */
public class GrayscaleFilter extends Filter {

    @Override
    public void apply(final BufferedImage img) {
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                final int pixel = img.getRGB(x, y);
                final Color color = new Color(pixel, true);
                final int avg = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                img.setRGB(x, y, new Color(avg, avg, avg, color.getAlpha()).getRGB());
            }
        }
    }

    @Override
    public String id() {
        return "grayscale";
    }

}
