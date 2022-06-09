package dev.cerus.mcheadrender.renderer;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import org.jetbrains.annotations.NotNull;

/**
 * Renders the front of the head
 */
public class FlatRenderer extends Renderer {

    @Override
    public @NotNull BufferedImage render(final BufferedImage src, final int widthAndHeight, final boolean overlay) {
        // Calculate the scale factor (the head is 8 pixels in width and height)
        final double scale = widthAndHeight / 8d;

        // Create the output image
        final BufferedImage output = new BufferedImage(widthAndHeight, widthAndHeight, BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics = output.createGraphics();

        // Draw the front of the head
        graphics.setTransform(AffineTransform.getScaleInstance(scale, scale));
        graphics.drawImage(this.getPart(src, 8, 8, 8, 8, 1), 0, 0, null);

        if (overlay) {
            // Draw the overlay of the front of the head
            graphics.setTransform(AffineTransform.getScaleInstance(scale, scale));
            graphics.drawImage(this.getPart(src, 40, 8, 8, 8, 1), 0, 0, null);
        }

        // Cleanup and return
        graphics.dispose();
        return output;
    }

    @Override
    public @NotNull String id() {
        return "flat";
    }

}
