#disable repl output during initialization by wrapping everything in a begin ... end
begin

	using Sockets
	using Pkg
	
	# https://discourse.julialang.org/t/how-to-use-pkg-dependencies-instead-of-pkg-installed/36416/10
	isinstalled(pkg::String) = any(x -> x.name == pkg && x.is_direct_dep, values(Pkg.dependencies()))
	
	if !isinstalled("JSON")
		# redirect stderr to stdout so that stderr parsing on java does not confused falsely in jajub or juliacaller
	    redirect_stderr(stdout) do
	        Pkg.add("JSON")
	    end
	end
	
	using JSON
	
	function callJava_createSocket()
		if socketScriptTaskCallbackServerHost == "localhost"
			global socketScriptTaskCallbackSocket = connect(socketScriptTaskCallbackServerPort)
		else
	    	global socketScriptTaskCallbackSocket = connect(socketScriptTaskCallbackServerHost, socketScriptTaskCallbackServerPort)
		end
	    write(socketScriptTaskCallbackSocket, socketScriptTaskCallbackContextUuid * "\n")
	end
	
	function callJava_invokeSocket(methodName, parameters)
	    write(socketScriptTaskCallbackSocket, methodName * ";" * json(parameters) * "\n")
	    # WORKAOUND: newlines need to be escaped over the wire, unescape here
	    returnExpression = replace(readline(socketScriptTaskCallbackSocket), "\\n" => "\n")
	    return eval(Meta.parse(returnExpression))
	end
	
	function callJava(methodName, parameters...)
	    if !isdefined(Main, :socketScriptTaskCallbackContext)
	        if isdefined(Main, :socketScriptTaskCallbackContextUuid)
	            callJava_createSocket()
	        else
	            error("IScriptTaskCallback not available")
	        end
	    end
        return callJava_invokeSocket(methodName, parameters)
	end


end