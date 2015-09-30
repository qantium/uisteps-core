package com.qantium.uisteps.core.run.verify;

import com.qantium.uisteps.core.run.verify.conditions.Result;
import com.qantium.uisteps.core.run.verify.conditions.Condition;
import com.qantium.uisteps.core.run.verify.results.ExpectedResult;
import com.qantium.uisteps.core.run.verify.results.LastExpectedResult;
import org.junit.Assert;

/**
 *
 * @author ASolyankin
 */
public class Verify {

    private Result result;

    public Verify() {
        this(new Result());
    }
    
    public Verify(Result result) {
        this.result = result;
    }

    public ExpectedResult _that(boolean condition) {
        return new ExpectedResult(this, condition);
    }

    public Then _that(Condition... conditions) {
        result.add(conditions);
        return new Then(this);
    }
    
    public LastExpectedResult that(boolean condition) {
        return new LastExpectedResult(this, condition);
    }

    public Then that(Condition... conditions) {
        Then then = _that(conditions);
        this.result();
        return then;
    }

    public Result result(Result result) {
        try {
            Assert.assertTrue(result.isSuccessful());
            return result;
        } finally {
            result.reset();
        }
    }

    public Result result() {
        return result(result);
    }

    public boolean isSuccessful() {
        return result.isSuccessful();
    }

    public Result getResult() {
        return result;
    }

}
