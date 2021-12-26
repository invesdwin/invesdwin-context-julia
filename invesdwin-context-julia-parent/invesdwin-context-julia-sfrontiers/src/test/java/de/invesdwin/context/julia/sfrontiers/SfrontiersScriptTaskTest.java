package de.invesdwin.context.julia.sfrontiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;

import org.junit.jupiter.api.Test;

import de.invesdwin.context.julia.runtime.contract.IScriptTaskRunnerJulia;
import de.invesdwin.context.julia.runtime.jajub.JajubScriptTaskRunnerJulia;
import de.invesdwin.context.julia.runtime.julia4j.Julia4jScriptTaskRunnerJulia;
import de.invesdwin.context.julia.runtime.juliacaller.JuliaCallerScriptTaskRunnerJulia;
import de.invesdwin.context.test.ATest;
import de.invesdwin.util.assertions.Assertions;
import de.invesdwin.util.math.decimal.Decimal;

@NotThreadSafe
public class SfrontiersScriptTaskTest extends ATest {

    private static final int ITERATIONS = 10;
    @Inject
    private JuliaCallerScriptTaskRunnerJulia juliaCallerScriptTaskRunner;
    @Inject
    private JajubScriptTaskRunnerJulia jajubScriptTaskRunner;
    @Inject
    private Julia4jScriptTaskRunnerJulia julia4jScriptTaskRunner;

    @Test
    public void testJuliaCaller() {
        for (int i = 0; i < ITERATIONS; i++) {
            run(juliaCallerScriptTaskRunner);
            log.info("------------------------");
        }
    }

    private void run(final IScriptTaskRunnerJulia runner) {
        final List<List<Double>> tradesPerStrategy = new ArrayList<>();
        //        0.5,-0.3,0.4,-0.2
        tradesPerStrategy.add(Arrays.asList(0.5, -0.3, 0.4, -0.2));
        //        0.1,-0.15,0.4,-0.1
        tradesPerStrategy.add(Arrays.asList(0.1, -0.15, 0.4, -0.1));
        final List<Double> optimalFsRaw = new SfrontiersScriptTask(tradesPerStrategy).run(runner);
        final List<Decimal> optimalFs = new ArrayList<Decimal>();
        for (final Double optimalFStr : optimalFsRaw) {
            optimalFs.add(new Decimal(optimalFStr).round(3));
        }
        Assertions.assertThat(optimalFs).isEqualTo(Arrays.asList(new Decimal("0.052"), new Decimal("0.213")));
    }

    @Test
    public void testJajub() {
        for (int i = 0; i < ITERATIONS; i++) {
            run(jajubScriptTaskRunner);
            log.info("------------------------");
        }
    }

    @Test
    public void testJulia4j() {
        for (int i = 0; i < ITERATIONS; i++) {
            run(julia4jScriptTaskRunner);
            log.info("------------------------");
        }
    }

    @Test
    public void testNegative() {
        final List<List<Double>> tradesPerStrategy = new ArrayList<>();
        //        0.5,-0.3,0.4,-0.2
        tradesPerStrategy.add(Arrays.asList(-0.5, -0.3, -0.4, -0.2));
        //        0.1,-0.15,0.4,-0.1
        tradesPerStrategy.add(Arrays.asList(-0.1, -0.15, -0.4, -0.1));
        final List<Double> optimalFsRaw = new SfrontiersScriptTask(tradesPerStrategy).run(juliaCallerScriptTaskRunner);
        final List<Decimal> optimalFs = new ArrayList<Decimal>();
        for (final Double optimalFStr : optimalFsRaw) {
            optimalFs.add(new Decimal(optimalFStr).round(3));
        }
        Assertions.assertThat(optimalFs).isEqualTo(Arrays.asList(Decimal.ZERO, Decimal.ZERO));
    }

    @Test
    public void testPositive() {
        final List<List<Double>> tradesPerStrategy = new ArrayList<>();
        //        0.5,-0.3,0.4,-0.2
        tradesPerStrategy.add(Arrays.asList(0.5, 0.3, 0.4, 0.2));
        //        0.1,-0.15,0.4,-0.1
        tradesPerStrategy.add(Arrays.asList(0.1, 0.15, 0.4, 0.1));
        try {
            new SfrontiersScriptTask(tradesPerStrategy).run(juliaCallerScriptTaskRunner);
            Assertions.failExceptionExpected();
        } catch (final Throwable t) {
            Assertions.assertThat(t.getMessage())
                    .contains("all 'events' columns must have at least one negative trade");
        }
    }

    @Test
    public void testEmptyTrades() {
        final List<List<Double>> tradesPerStrategy = new ArrayList<>();
        //        0.5,-0.3,0.4,-0.2
        tradesPerStrategy.add(Arrays.asList());
        //        0.1,-0.15,0.4,-0.1
        tradesPerStrategy.add(Arrays.asList());
        try {
            new SfrontiersScriptTask(tradesPerStrategy).run(juliaCallerScriptTaskRunner);
            Assertions.failExceptionExpected();
        } catch (final Throwable t) {
            Assertions.assertThat(t.getMessage())
                    .contains("all 'events' columns must have at least one negative trade");
        }
    }

    @Test
    public void testEmptyStrategies() {
        final List<List<Double>> tradesPerStrategy = new ArrayList<>();
        try {
            new SfrontiersScriptTask(tradesPerStrategy).run(juliaCallerScriptTaskRunner);
            Assertions.failExceptionExpected();
        } catch (final Throwable t) {
            Assertions.assertThat(t.getMessage()).contains("No trades!");
        }
    }

}
