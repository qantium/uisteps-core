package com.qantium.uisteps.core.utils.grid.servlets.robot;

import java.awt.*;

/**
 * Created by Anton Solyankin
 */
public class RobotActionFactory {


    public RobotAction mouseMove(int x, int y) {
        return new RobotAction("mouseMove", new Class<?>[]{Integer.TYPE, Integer.TYPE}, new Object[]{x, y});
    }

    public RobotAction mousePress(int buttons) {
        return new RobotAction("mousePress", new Class<?>[]{Integer.TYPE}, new Object[]{buttons});
    }

    public RobotAction mouseRelease(int buttons) {
        return new RobotAction("mouseRelease", new Class<?>[]{Integer.TYPE}, new Object[]{buttons});
    }

    public RobotAction mouseWheel(int wheelAmt) {
        return new RobotAction("mouseWheel", new Class<?>[]{Integer.TYPE}, new Object[]{wheelAmt});
    }

    public RobotAction keyPress(int keycode) {
        return new RobotAction("keyPress", new Class<?>[]{Integer.TYPE}, new Object[]{keycode});
    }

    public RobotAction keyRelease(int keycode) {
        return new RobotAction("keyRelease", new Class<?>[]{Integer.TYPE}, new Object[]{keycode});
    }

    public RobotAction getPixelColor(int x, int y) {
        return new RobotAction("getPixelColor", new Class<?>[]{Integer.TYPE, Integer.TYPE}, new Object[]{x, y});
    }

    public RobotAction createScreenCapture(Rectangle screenRect) {
        return new RobotAction("createScreenCapture", new Class<?>[]{Rectangle.class}, new Object[]{screenRect});
    }

    public RobotAction setAutoWaitForIdle(boolean isOn) {
        return new RobotAction("setAutoWaitForIdle", new Class<?>[]{Boolean.TYPE}, new Object[]{isOn});
    }

    public RobotAction setAutoDelay(int ms) {
        return new RobotAction("setAutoDelay", new Class<?>[]{Integer.TYPE}, new Object[]{ms});
    }

    public RobotAction delay(int ms) {
        return new RobotAction("delay", new Class<?>[]{Integer.TYPE}, new Object[]{ms});
    }

    public RobotAction waitForIdle() {
        return new RobotAction("waitForIdle");
    }
}
