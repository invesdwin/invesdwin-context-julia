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
- **invesdwin-context-julia-runtime-rserve**: You could use [Rserve](https://github.com/s-u/REngine) (which seems to be licensed under the LGPL too) to talk to a separate R process via a socket to run your scripts (which might use GPL packages) there, separated from your main application. This module can also be used to run your R scripts on remote hosts. We use a pool of [Rsession](https://github.com/yannrichet/rsession) instances here to do the actual work.
- **invesdwin-context-julia-runtime-rcaller**: Another alternative would be to call the original R executable directly from your main application via a command line interface and let it run your R script. Though keep in mind that you might still have to make at least your R scripts open source if they use R packages that are licensed under the GPL. Though by using the R executable directly you would lose the performance benefit that other solutions bring to the table. On the other hand, this solution requires no additional setup on the systems it runs on, since a simple installation of R suffices as long as the `Rscript` executable is available in the `$PATH`. We use a pool of [RCaller](https://github.com/jbytecode/rcaller) instances here to do the actual work.

You are free to choose which integration method you prefer by selecting the appropriate runtime module as a dependency for your application. The `invesdwin-context-julia-runtime-contract` module defines interfaces for integrating your Julia scripts in a way that works with all of the above runtime modules. So you have the benefit of being able to write your Julia scripts once and easily test against different runtimes in order to: 
- to measure the performance impact of the different runtime solutions
- to gain flexibility in various deployment scenarios

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

For more elaborate examples of the R script integration, have a look at the test cases in `invesdwin-context-julia-runtime-contract` which are executed in each individual runtime module test suite.

## Avoiding Bootstrap

If you want to use this project without the overhead of having to initialize a [invesdwin-context](https://github.com/invesdwin/invesdwin-context) bootstrap with its spring-context and module configuration, you can disable the bootstrap with the following code before using any scripts:

```java
de.invesdwin.context.PlatformInitializerProperties.setAllowed(false);
```

The above configuration options for the invidiual runtimes can still be provided by setting system properties before calling any script. An example for all of this can be found at: [ScriptingWithoutBootstrapMain.java](https://github.com/invesdwin/invesdwin-context/blob/master/tests/otherproject-noparent-bom-test/src/main/java/com/otherproject/scripting/ScriptingWithoutBootstrapMain.java)

## Recommended Editors

For experimenting with Julia it might be interesting to use [Juno](https://junolab.org/) or [Julia for Visual Studio Code](https://www.julia-vscode.org/) as a standalone development environment. It supports a nice variable viewer and has a nice integration of the Julia documentation, which helps a lot during Julia learning and development. It also comes with a comfortable debugger for Julia scripts.

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
