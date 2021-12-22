package de.invesdwin.context.julia.runtime.contract;

import de.invesdwin.context.log.Log;

public interface IScriptTaskRunnerJulia {

    String CLEANUP_SCRIPT = "rm(list=ls(all=TRUE))";

    Log LOG = new Log(IScriptTaskRunnerJulia.class);

    <T> T run(AScriptTaskJulia<T> scriptTask);

}
