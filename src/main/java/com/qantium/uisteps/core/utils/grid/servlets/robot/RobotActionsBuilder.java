package com.qantium.uisteps.core.utils.grid.servlets.robot;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anton Solyankin
 */
public class RobotActionsBuilder {

    private RobotActionFactory factory = new RobotActionFactory();
    private List<RobotAction> actions = new ArrayList();

    public RobotActionsBuilder reset() {
        actions = new ArrayList();
        return this;
    }

    public RobotActionsBuilder mouseMove(int x, int y) {
        actions.add(factory.mouseMove(x, y));
        return this;
    }

    public RobotActionsBuilder waitForIdle() {
        actions.add(factory.waitForIdle());
        return this;
    }

    public RobotActionsBuilder mousePress(int buttons) {
        actions.add(factory.mousePress(buttons));
        return this;
    }

    public RobotActionsBuilder mouseWheel(int wheelAmt) {
        actions.add(factory.mouseWheel(wheelAmt));
        return this;
    }

    public RobotActionsBuilder mouseRelease(int buttons) {
        actions.add(factory.mouseRelease(buttons));
        return this;
    }

    public RobotActionsBuilder getPixelColor(int x, int y) {
        actions.add(factory.getPixelColor(x, y));
        return this;
    }

    public RobotActionsBuilder setAutoWaitForIdle(boolean isOn) {
        actions.add(factory.setAutoWaitForIdle(isOn));
        return this;
    }

    public RobotActionsBuilder keyRelease(Integer... keyCodes) {
        for(Integer keycode: keyCodes) {
            actions.add(factory.keyRelease(keycode));
        }
        return this;
    }

    public RobotActionsBuilder createScreenCapture(Rectangle screenRect) {
        actions.add(factory.createScreenCapture(screenRect));
        return this;
    }

    public RobotActionsBuilder keyPress(Integer... keyCodes) {
        for(Integer keycode: keyCodes) {
            actions.add(factory.keyPress(keycode));
        }
        return this;
    }

    public RobotActionsBuilder sendKeys(Integer... keyCodes) {
        for(Integer keycode: keyCodes) {
            keyPress(keycode);
            keyRelease(keycode);
        }
        return this;
    }

    public RobotActionsBuilder setAutoDelay(int ms) {
        actions.add(factory.setAutoDelay(ms));
        return this;
    }

    public RobotActionsBuilder delay(int ms) {
        actions.add(factory.delay(ms));
        return this;
    }

    public RobotActions build() {
        RobotActions robotActions = new RobotActions(this.actions);
        reset();
        return robotActions;
    }
}
