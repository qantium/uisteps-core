package com.qantium.uisteps.core.browser.wait;

import com.qantium.uisteps.core.browser.NoBrowserException;
import org.openqa.selenium.UnhandledAlertException;

public abstract class Waiter<R> extends TimeoutBuilder {

    public final static ThreadLocal<Long> startTime = new ThreadLocal<>().withInitial(() -> -1L);
    private boolean waiterIsInitialized;

    public Waiter() {
    }

    public Waiter(WithTimeout timeouts) {
        withDelay(timeouts.getDelay());
        withTimeout(timeouts.getTimeout());
        pollingEvery(timeouts.getPollingTime());
    }

    protected abstract R run();

    public R perform() throws WaitingException {
        return perform(false);
    }

    public R checkResult() throws WaitingException {
        return perform(true);
    }

    private R perform(boolean checkResult) throws WaitingException {

        sleep(getDelay());

        if (startTime.get() < 0) {
            waiterIsInitialized = true;
            startTime.set(System.currentTimeMillis());
        }

        WaitingException exception = new WaitingException(this);

        while (System.currentTimeMillis() - startTime.get() <= getTimeout()) {
            try {
                R result = run();

                if (checkResult) {
                    if (result instanceof Boolean) {
                        if (Boolean.FALSE.equals(result)) {
                            sleep(getPollingTime());
                            continue;
                        }
                    } else {
                        throw new CheckResultException("For check result waiting result \"" + result + "\" must be boolean");
                    }
                }
                flushWaiter();
                return result;
            } catch (CheckResultException ex) {
                flushWaiter();
                throw ex;
            } catch (NoBrowserException | UnhandledAlertException | IllegalArgumentException ex) {
                flushWaiter();
                throw new WaitingException(this, ex);
            } catch (Exception ex) {
                sleep(getPollingTime());
                exception = errorHandler(ex);
            }
        }
        flushWaiter();
        throw new WaitingException(this, exception);
    }

    private void flushWaiter() {
        if (waiterIsInitialized) {
            waiterIsInitialized = false;
            startTime.set(-1L);
        }
    }

    protected WaitingException errorHandler(Exception ex) {
        return new WaitingException(this, ex);
    }

    private static class CheckResultException extends RuntimeException {

        public CheckResultException(Object result) {
            super("For check result waiting result \"" + result + "\" must be boolean");
        }
    }

    private void sleep(long timeout) {
        try {
            Thread.sleep(timeout);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
