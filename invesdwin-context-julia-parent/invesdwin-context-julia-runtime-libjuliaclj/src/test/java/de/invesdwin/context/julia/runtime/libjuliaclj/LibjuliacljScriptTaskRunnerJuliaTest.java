package de.invesdwin.context.julia.runtime.libjuliaclj;

import javax.annotation.concurrent.NotThreadSafe;
import jakarta.inject.Inject;

import org.junit.jupiter.api.Test;

import de.invesdwin.context.julia.runtime.contract.InputsAndResultsTests;
import de.invesdwin.context.test.ATest;

@NotThreadSafe
public class LibjuliacljScriptTaskRunnerJuliaTest extends ATest {

    @Inject
    private LibjuliacljScriptTaskRunnerJulia runner;

    @Test
    public void test() {
        new InputsAndResultsTests(runner).test();
    }

    @Test
    public void testParallel() {
        new InputsAndResultsTests(runner).testParallel();
    }

}
