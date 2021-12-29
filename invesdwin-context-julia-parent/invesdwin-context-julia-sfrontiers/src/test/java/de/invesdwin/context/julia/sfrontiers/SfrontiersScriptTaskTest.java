package de.invesdwin.context.julia.sfrontiers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.concurrent.NotThreadSafe;
import javax.inject.Inject;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import de.invesdwin.context.julia.runtime.contract.IScriptTaskRunnerJulia;
import de.invesdwin.context.julia.runtime.jajub.JajubScriptTaskRunnerJulia;
import de.invesdwin.context.julia.runtime.julia4j.Julia4jScriptTaskRunnerJulia;
import de.invesdwin.context.julia.runtime.juliacaller.JuliaCallerScriptTaskRunnerJulia;
import de.invesdwin.context.test.ATest;
import de.invesdwin.util.assertions.Assertions;
import de.invesdwin.util.math.Doubles;
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
        //        y = [1,2,3,4,5,6,7,8,9,10]
        final double[] y = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        //        x = [1.1 1.2 1.3;2.1 2.2 2.3;1.2 1.5 1.6;1.7 1.4 5.6;1.5 5.7 2.6;5.7 3.6 5.1;5.4 6.1 7.4;3.6 3.6 3.5;7.8 4.6 3.1;5.1 3.2 6.3]
        final double[][] x = { { 1.1, 1.2, 1.3 }, { 2.1, 2.2, 2.3 }, { 1.2, 1.5, 1.6 }, { 1.7, 1.4, 5.6 },
                { 1.5, 5.7, 2.6 }, { 5.7, 3.6, 5.1 }, { 5.4, 6.1, 7.4 }, { 3.6, 3.6, 3.5 }, { 7.8, 4.6, 3.1 },
                { 5.1, 3.2, 6.3 } };
        final double[] efficienciesRaw = new SfrontiersScriptTask(y, x).run(runner);
        final List<Decimal> efficiencies = new ArrayList<Decimal>();
        for (final double efficiency : efficienciesRaw) {
            efficiencies.add(new Decimal(efficiency));
        }
        Assertions.assertThat(efficiencies)
                .isEqualTo(Arrays.asList(new Decimal("0.9999999999990773"), new Decimal("0.9999999999990772"),
                        new Decimal("0.9999999999990774"), new Decimal("0.9999999999990773"),
                        new Decimal("0.9999999999990773"), new Decimal("0.9999999999990774"),
                        new Decimal("0.9999999999990773"), new Decimal("0.9999999999990773"),
                        new Decimal("0.9999999999990773"), new Decimal("0.9999999999990773")));
    }

    @Test
    public void testJajub() {
        for (int i = 0; i < ITERATIONS; i++) {
            run(jajubScriptTaskRunner);
            log.info("------------------------");
        }
    }

    @Disabled("requires signal chaining via LD_PRELOAD workaround")
    @Test
    public void testJulia4j() {
        for (int i = 0; i < ITERATIONS; i++) {
            run(julia4jScriptTaskRunner);
            log.info("------------------------");
        }
    }

    @Test
    public void testNegative() {
        //      y = [1,2,3,4,5,6,7,8,9,10]
        final double[] y = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        //        x = [1.1 1.2 1.3;2.1 2.2 2.3;1.2 1.5 1.6;1.7 1.4 5.6;1.5 5.7 2.6;5.7 3.6 5.1;5.4 6.1 7.4;3.6 3.6 3.5;7.8 4.6 3.1;5.1 3.2 6.3]
        final double[][] x = { { 1.1, 1.2, 1.3 }, { 2.1, 2.2, 2.3 }, { 1.2, 1.5, 1.6 }, { 1.7, 1.4, 5.6 },
                { 1.5, 5.7, 2.6 }, { 5.7, 3.6, 5.1 }, { 5.4, 6.1, 7.4 }, { 3.6, 3.6, 3.5 }, { 7.8, 4.6, 3.1 },
                { 5.1, 3.2, 6.3 } };
        for (int i = 0; i < y.length; i++) {
            y[i] = -y[i];
        }
        for (int i = 0; i < x.length; i++) {
            final double[] row = x[i];
            for (final int j = 0; j < row.length; i++) {
                row[j] = -row[j];
            }
        }
        final double[] optimalFsRaw = new SfrontiersScriptTask(y, x).run(juliaCallerScriptTaskRunner);
        final List<Decimal> optimalFs = new ArrayList<Decimal>();
        for (final Double optimalFStr : optimalFsRaw) {
            optimalFs.add(new Decimal(optimalFStr).round(3));
        }
        Assertions.assertThat(optimalFs).isEqualTo(Arrays.asList(Decimal.ZERO, Decimal.ZERO));
    }

    @Test
    public void testSingularException() {
        final double[] y = { 1, 2, 3 };
        final double[][] x = { { 1.1, 1.2, 1.3 }, { 2.1, 2.2, 2.3 }, { 3.1, 3.2, 3.3 } };
        for (int i = 0; i < y.length; i++) {
            y[i] = -y[i];
        }
        for (int i = 0; i < x.length; i++) {
            final double[] row = x[i];
            for (final int j = 0; j < row.length; i++) {
                row[j] = -row[j];
            }
        }
        try {
            new SfrontiersScriptTask(y, x).run(juliaCallerScriptTaskRunner);
            Assertions.failExceptionExpected();
        } catch (final Throwable t) {
            Assertions.assertThat(t.getMessage()).contains("No trades!");
        }
    }

    @Test
    public void testDomainError() {
        final double[] y = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        final double[][] x = { { 1.1, 1.2, 1.3 }, { 2.1, 2.2, 2.3 }, { 3.1, 3.2, 3.3 }, { 4.1, 4.2, 4.3 },
                { 5.1, 5.2, 5.3 }, { 6.1, 6.2, 6.3 }, { 7.1, 7.2, 7.3 }, { 8.1, 8.2, 8.3 }, { 9.1, 9.2, 9.3 },
                { 10.1, 10.2, 10.3 } };
        try {
            new SfrontiersScriptTask(y, x).run(juliaCallerScriptTaskRunner);
            Assertions.failExceptionExpected();
        } catch (final Throwable t) {
            Assertions.assertThat(t.getMessage())
                    .contains("DomainError(")
                    .contains(
                            "sqrt will only return a complex result if called with a complex argument. Try sqrt(Complex(x)).");
        }
    }

    @Test
    public void testEmptyInputs() {
        //      y = [1,2,3,4,5,6,7,8,9,10]
        final double[] y = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
        //        x = [1.1 1.2 1.3;2.1 2.2 2.3;1.2 1.5 1.6;1.7 1.4 5.6;1.5 5.7 2.6;5.7 3.6 5.1;5.4 6.1 7.4;3.6 3.6 3.5;7.8 4.6 3.1;5.1 3.2 6.3]
        final double[][] x = new double[y.length][];
        for (int i = 0; i < x.length; i++) {
            x[i] = Doubles.EMPTY_ARRAY;
        }
        try {
            new SfrontiersScriptTask(y, x).run(juliaCallerScriptTaskRunner);
            Assertions.failExceptionExpected();
        } catch (final Throwable t) {
            Assertions.assertThat(t.getMessage())
                    .contains(
                            "Error: DimensionMismatch(\"diagonal matrix is 0 by 0 but right hand side has 10 rows\")");
        }
    }

    @Test
    public void testEmptyInputsAndOutputs() {
        try {
            final double[] y = Doubles.EMPTY_ARRAY;
            final double[][] x = Doubles.EMPTY_MATRIX;
            new SfrontiersScriptTask(y, x).run(juliaCallerScriptTaskRunner);
            Assertions.failExceptionExpected();
        } catch (final Throwable t) {
            Assertions.assertThat(t.getMessage()).contains("No trades!");
        }
    }

    @Test
    public void testEmptyOutputs() {
        try {
            final double[] y = Doubles.EMPTY_ARRAY;
            final double[][] x = { { 1.1, 1.2, 1.3 }, { 2.1, 2.2, 2.3 }, { 1.2, 1.5, 1.6 }, { 1.7, 1.4, 5.6 },
                    { 1.5, 5.7, 2.6 }, { 5.7, 3.6, 5.1 }, { 5.4, 6.1, 7.4 }, { 3.6, 3.6, 3.5 }, { 7.8, 4.6, 3.1 },
                    { 5.1, 3.2, 6.3 } };
            new SfrontiersScriptTask(y, x).run(juliaCallerScriptTaskRunner);
            Assertions.failExceptionExpected();
        } catch (final Throwable t) {
            Assertions.assertThat(t.getMessage()).contains("Error: BoundsError(Float64[], (1,))");
        }
    }

}
