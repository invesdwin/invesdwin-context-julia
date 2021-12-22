package de.invesdwin.context.julia.runtime.ju4ja;

import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import de.invesdwin.context.julia.runtime.ju4ja.Ju4jaScriptTaskRunnerJulia;
import de.invesdwin.context.r.runtime.contract.InputsAndResultsTests;
import de.invesdwin.context.test.ATest;

@NotThreadSafe
public class Ju4jaScriptTaskRunnerJuliaTest extends ATest {

    @Inject
    private Ju4jaScriptTaskRunnerJulia runner;

    @Test
    public void test() {
        new InputsAndResultsTests(runner).test();
    }

    @Test
    public void testParallel() {
        new InputsAndResultsTests(runner).testParallel();
    }

}
