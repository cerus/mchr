package dev.cerus.mcheadrender.filter;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Inverts an image
 */
public class InvertFilter extends Filter {

    @Override
    public void apply(final BufferedImage img) {
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getHeight(); y++) {
                final int pixel = img.getRGB(x, y);
                final Color color = new Color(pixel, true);
                img.setRGB(x, y, new Color(
                        255 - color.getRed(),
                        255 - color.getGreen(),
                        255 - color.getBlue(),
                        color.getAlpha()
                ).getRGB());
            }
        }
    }

    @Override
    public String id() {
        return "invert";
    }

}
