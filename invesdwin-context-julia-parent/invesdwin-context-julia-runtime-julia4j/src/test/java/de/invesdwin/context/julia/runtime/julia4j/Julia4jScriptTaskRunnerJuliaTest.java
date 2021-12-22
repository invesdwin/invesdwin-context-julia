package de.invesdwin.context.julia.runtime.julia4j;

import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import de.invesdwin.context.julia.runtime.julia4j.Julia4jScriptTaskRunnerJulia;
import de.invesdwin.context.r.runtime.contract.InputsAndResultsTests;
import de.invesdwin.context.test.ATest;

@NotThreadSafe
public class Julia4jScriptTaskRunnerJuliaTest extends ATest {

    @Inject
    private Julia4jScriptTaskRunnerJulia runner;

    @Test
    public void test() {
        new InputsAndResultsTests(runner).test();
    }

    @Test
    public void testParallel() {
        new InputsAndResultsTests(runner).testParallel();
    }

}
