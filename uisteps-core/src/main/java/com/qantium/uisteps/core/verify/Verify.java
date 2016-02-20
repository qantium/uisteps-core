package com.qantium.uisteps.core.verify;

import com.qantium.uisteps.core.verify.results.Result;
import com.qantium.uisteps.core.verify.conditions.Condition;
import com.qantium.uisteps.core.verify.results.LogicOperation;
import com.qantium.uisteps.core.verify.results.ExpectedResult;
import com.qantium.uisteps.core.verify.results.LastExpectedResult;

import java.util.ArrayList;

import org.junit.Assert;

/**
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

    private class ConditionCandidate {

        private Boolean successful = null;
        private String expectedResult = null;
        private String actualResult = null;
        private LogicOperation logicOperation = null;

        public void setSuccessful(Boolean condition) {
            this.successful = condition;
        }

        public void setResult(String result) {

            if (expectedResult == null) {
                expectedResult = result;
            } else if (actualResult == null) {
                actualResult = result;
            } else {
                throw new RuntimeException("Expected and actual results have been already set!");
            }
        }

        public void setLogicOperation(LogicOperation logicOperation) {
            this.logicOperation = logicOperation;
        }

        public Condition getCondition() {

            if (successful == null) {
                throw new RuntimeException("Condition is not set!");
            }

            if (expectedResult == null) {
                throw new RuntimeException("Expected result is not set!");
            }

            if (actualResult == null) {
                actualResult = "";
            }

            if (logicOperation == null) {
                logicOperation = LogicOperation.OR;
            }

            return new Condition(successful, expectedResult, actualResult).set(logicOperation);
        }
    }

    protected Condition[] getConditions(Object... args) {

        ArrayList<Condition> conditions = new ArrayList();
        ArrayList<ConditionCandidate> candidates = new ArrayList();

        ConditionCandidate conditionCandidate = null;

        for (Object arg : args) {

            if (arg instanceof Boolean || arg instanceof Condition) {
                conditionCandidate = new ConditionCandidate();
                candidates.add(conditionCandidate);
            }

            if (conditionCandidate == null) {
                throw new RuntimeException("First parameter must be Boolean or Condition, but not " + arg.getClass() + "!");
            }

            if (arg instanceof Boolean) {
                conditionCandidate.setSuccessful((Boolean) arg);
            } else if (arg instanceof String) {
                conditionCandidate.setResult((String) arg);
            } else if (arg instanceof LogicOperation) {
                conditionCandidate.setLogicOperation((LogicOperation) arg);
            } else if (arg instanceof Condition) {
                Condition condition = (Condition) arg;
                conditionCandidate.setSuccessful(condition.isSuccessful());
                conditionCandidate.setResult(condition.getExpectedResult());
                conditionCandidate.setResult(condition.getActualResult());
                conditionCandidate.setLogicOperation(condition.getLogicOperation());
            } else {
                throw new RuntimeException("Parameter type must be Boolean, String, LogicOperation or Condition, but not " + arg.getClass() + "!");
            }
        }

        for (ConditionCandidate candidate : candidates) {
            Condition conn = candidate.getCondition();
            conditions.add(conn);
        }

        Condition[] conditionsArr = new Condition[conditions.size()];
        return conditions.toArray(conditionsArr);
    }

    public ExpectedResult _that(boolean condition) {
        return new ExpectedResult(this, condition);
    }

    public Then _conditions(Condition... conditions) {
        result.add(conditions);
        return new Then(this);
    }

    public Then _that(Object... args) {
        return _conditions(getConditions(args));
    }

    public LastExpectedResult that(boolean condition) {
        return new LastExpectedResult(this, condition);
    }

    public Then conditions(Condition... conditions) {
        Then then = _conditions(conditions);
        this.result();
        return then;
    }

    public Then that(Object... args) {
        return conditions(getConditions(args));
    }

    public void check(Result result) {
        try {
            Assert.assertTrue(result.toString(), result.isSuccessful());
        } catch (Exception ex) {
            throw ex;
        } finally {
            result.reset();
        }
    }

    public void result() {
        check(result);
    }

    public boolean isSuccessful() {
        return result.isSuccessful();
    }

    public Result getResult() {
        return result;
    }

}
