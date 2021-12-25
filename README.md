# invesdwin-context-julia
Integrate Julia functionality with these modules for the [invesdwin-context](https://github.com/subes/invesdwin-context) module system.

## Maven

Releases and snapshots are deployed to this maven repository:
```
https://invesdwin.de/repo/invesdwin-oss-remote/
```

Dependency declaration:
```xml
<dependency>
	<groupId>de.invesdwin</groupId>
	<artifactId>invesdwin-context-julia-runtime-juliacaller</artifactId>
	<version>1.0.4-SNAPSHOT</version><!---project.version.invesdwin-context-julia-parent-->
</dependency>
```

## Runtime Integration Modules

We have a few options available for integrating Julia:
- **invesdwin-context-julia-runtime-juliacaller**: This uses a forked version of [JuliaCaller](https://github.com/jbytecode/juliacaller/issues/1) to fix some compatibility issues. It talks to the julia process via a local socket. Errors are detected by parsing stderr for messages. Though this has the drawback of giving false errors for deprecation warnings.
- **invesdwin-context-julia-runtime-jajub**: This uses a modified version of [Jajub](https://github.com/org-arl/jajub/issues/2) to make it significantly faster. It talks to the julia process via pipes. Errors are detected by checking for specific protocol messages. This makes it robust against deprecation warnings, but might require debug logging to see the actual errors.
- **invesdwin-context-julia-runtime-julia4j**: This uses [Julia4j](https://github.com/rssdev10/julia4j/issues/2) as a JNI binding to Julia. It is not yet useable due to memory access violations and and error handling not working.

You are free to choose which integration method you prefer by selecting the appropriate runtime module as a dependency for your application. The `invesdwin-context-julia-runtime-contract` module defines interfaces for integrating your Julia scripts in a way that works with all of the above runtime modules. So you have the benefit of being able to write your Julia scripts once and easily test against different runtimes in order to: 
- to measure the performance impact of the different runtime solutions
- to gain flexibility in various deployment scenarios

See here for a discussion about potential other integration modules: https://discourse.julialang.org/t/running-julia-from-java-what-is-crazier/31662/39?u=subes

## Example Code

This is a minimal example of the famous `Hello World!` as a script:

```java
final AScriptTaskJulia<String> script = new AScriptTaskJulia<String>() {

    @Override
    public void populateInputs(final IScriptTaskInputs inputs) {
	inputs.putString("hello", "World");
    }

    @Override
    public void executeScript(final IScriptTaskEngine engine) {
	//execute this script inline:
	engine.eval("world = \"Hello \" * hello * \"!\"");
	//or run it from a file:
	//engine.eval(new ClassPathResource("HelloWorldScript.jl", getClass()));
    }

    @Override
    public String extractResults(final IScriptTaskResults results) {
        return results.getString("world");
    }
};
final String result = script.run(); //optionally pass a specific runner as an argument here
Assertions.assertThat(result).isEqualTo("Hello World!");
```

For more elaborate examples of the Julia script integration, have a look at the test cases in `invesdwin-context-julia-runtime-contract` which are executed in each individual runtime module test suite.

## Avoiding Bootstrap

If you want to use this project without the overhead of having to initialize a [invesdwin-context](https://github.com/invesdwin/invesdwin-context) bootstrap with its spring-context and module configuration, you can disable the bootstrap with the following code before using any scripts:

```java
de.invesdwin.context.PlatformInitializerProperties.setAllowed(false);
```

The above configuration options for the invidiual runtimes can still be provided by setting system properties before calling any script. An example for all of this can be found at: [ScriptingWithoutBootstrapMain.java](https://github.com/invesdwin/invesdwin-context/blob/master/tests/otherproject-noparent-bom-test/src/main/java/com/otherproject/scripting/ScriptingWithoutBootstrapMain.java)

## Recommended Editors

For experimenting with Julia it might be interesting to use [Juno](https://junolab.org/) or [Julia for Visual Studio Code](https://www.julia-vscode.org/) as a standalone development environment. It supports a nice variable viewer and has a nice integration of the Julia documentation, which helps a lot during Julia learning and development. It also comes with a comfortable debugger for Julia scripts.
If you want to run your scripts from your main application, you can do this easily with `invesdwin-context-julia-runtime-juiacaller` (add this module as a `test` scope dependency) during development (you also need to add a dependecy to the type `test-jar` of `invesdwin-context-julia-runtime-contract` for the log level to get activated, or alternatively change the log level of `de.invesdwin.context.julia.runtime.contract.IScriptTaskRunnerJulia` to `DEBUG` on your own). The actual deployment distribution can choose a different runtime then as a hard dependency.

## More Programming Languages

Similar integration modules like this one also exist for the following other programming languages: 

- **R Modules**: Scripting with R
	- https://github.com/invesdwin/invesdwin-context-r 
- **Python Modules**: Scripting with Python
	- https://github.com/invesdwin/invesdwin-context-python
- **Matlab/Octave/Scilab Modules**: Scripting with Matlab, Octave and Scilab
	- https://github.com/invesdwin/invesdwin-context-matlab


## Support

If you need further assistance or have some ideas for improvements and don't want to create an issue here on github, feel free to start a discussion in our [invesdwin-platform](https://groups.google.com/forum/#!forum/invesdwin-platform) mailing list.
