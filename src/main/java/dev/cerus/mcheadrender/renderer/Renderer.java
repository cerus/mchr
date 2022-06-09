package dev.cerus.mcheadrender.renderer;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import org.jetbrains.annotations.NotNull;

/**
 * A skin renderer
 */
public abstract class Renderer {

    /**
     * Renders a skin
     *
     * @param src            The source image (skin image)
     * @param widthAndHeight The resulting width and height (result is always square)
     * @param overlay        True if the 2nd layer (overlay) should be rendered
     *
     * @return The rendered skin
     */
    @NotNull
    public abstract BufferedImage render(@NotNull BufferedImage src, int widthAndHeight, boolean overlay);

    /**
     * Get the id of this renderer
     *
     * @return The renderer id
     */
    @NotNull
    public abstract String id();

    /**
     * Get a skin part
     *
     * @param src   The source image (skin image)
     * @param x     The x pos
     * @param y     The y pos
     * @param w     The width
     * @param h     The height
     * @param scale The scale of the resulting image
     *
     * @return The scaled skin part
     */
    @NotNull
    protected BufferedImage getPart(@NotNull final BufferedImage src, final int x, final int y, final int w, final int h, final double scale) {
        final BufferedImage subimage = src.getSubimage(x, y, w, h);
        return this.resize(subimage, scale);
    }

    /**
     * Resize an image
     *
     * @param src   The source image
     * @param scale The scale factor
     *
     * @return The scaled image
     */
    @NotNull
    protected BufferedImage resize(@NotNull final BufferedImage src, final double scale) {
        final BufferedImage scaled = new BufferedImage((int) (src.getWidth() * scale), (int) (src.getHeight() * scale), BufferedImage.TYPE_INT_ARGB);
        final Graphics2D graphics = scaled.createGraphics();
        graphics.drawImage(src, AffineTransform.getScaleInstance(scale, scale), null);
        graphics.dispose();
        return scaled;
    }

}
