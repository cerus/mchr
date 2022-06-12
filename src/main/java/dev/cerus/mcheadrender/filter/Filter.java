package dev.cerus.mcheadrender.filter;

import java.awt.image.BufferedImage;

/**
 * Applies some sort of visual effect onto the final image
 */
public abstract class Filter {

    /**
     * Apply the filter
     *
     * @param img The image that should be filtered
     */
    public abstract void apply(BufferedImage img);

    public abstract String id();

    @Override
    public int hashCode() {
        return this.id().hashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof Filter)) {
            return false;
        }
        return obj == this || ((Filter) obj).id().equals(this.id());
    }

}
