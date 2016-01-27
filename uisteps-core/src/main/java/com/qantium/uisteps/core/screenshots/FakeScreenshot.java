package com.qantium.uisteps.core.screenshots;

import java.awt.image.BufferedImage;

/**
 * Created by Solan on 25.01.2016.
 */
public class FakeScreenshot extends Screenshot {

    public FakeScreenshot() {
        super(new BufferedImage(0, 0 , BufferedImage.TYPE_CUSTOM));
    }
}
