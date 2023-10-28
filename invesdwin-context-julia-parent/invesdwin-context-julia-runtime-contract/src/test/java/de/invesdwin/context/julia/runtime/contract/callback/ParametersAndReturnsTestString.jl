println("getString")
if isdefined(Main, :getString) && !isnothing(getString)
	error("getString already defined!")
end
getString = callJava("getString")
println(typeof(getString))
println(getString)
if typeof(getString) != String
	error("getString not String!")
end
callJava("setString",getString)

println("getStringWithNull")
if isdefined(Main, :getStringWithNull) && !isnothing(getStringWithNull)
	error("getStringWithNull already defined!")
end
getStringWithNull = callJava("getStringWithNull")
println(typeof(getStringWithNull))
println(getStringWithNull)
if typeof(getStringWithNull) != Nothing
	error("getStringWithNull not Nothing!")
end
if !isnothing(getStringWithNull)
	error("getStringWithNull not nothing!")
end
callJava("setStringWithNull",getStringWithNull)

println("getStringVector")
if isdefined(Main, :getStringVector) && !isnothing(getStringVector)
	error("getStringVector already defined!")
end
getStringVector = callJava("getStringVector")
println(eltype(getStringVector))
println(getStringVector)
if eltype(getStringVector) != String
	error("getStringVector not String!")
end
callJava("setStringVector",getStringVector)


println("getStringVectorWithNull")
if isdefined(Main, :getStringVectorWithNull) && !isnothing(getStringVectorWithNull)
	error("getStringVectorWithNull already defined!")
end
getStringVectorWithNull = callJava("getStringVectorWithNull")
println(eltype(getStringVectorWithNull))
println(getStringVectorWithNull)
if eltype(getStringVectorWithNull) != String
	error("getStringVectorWithNull not String!")
end
if !isempty(getStringVectorWithNull[2])
	error("getStringVectorWithNull[2] not empty!")
end
callJava("setStringVectorWithNull",getStringVectorWithNull)

println("getStringVectorAsList")
if isdefined(Main, :getStringVectorAsList) && !isnothing(getStringVectorAsList)
	error("getStringVectorAsList already defined!")
end
getStringVectorAsList = callJava("getStringVectorAsList")
println(eltype(getStringVectorAsList))
println(getStringVectorAsList)
if eltype(getStringVectorAsList) != String
	error("getStringVectorAsList not String!")
end
callJava("setStringVectorAsList",getStringVectorAsList)

println("getStringVectorAsListWithNull")
if isdefined(Main, :getStringVectorAsListWithNull) && !isnothing(getStringVectorAsListWithNull)
	error("getStringVectorAsListWithNull already defined!")
end
getStringVectorAsListWithNull = callJava("getStringVectorAsListWithNull")
println(eltype(getStringVectorAsListWithNull))
println(getStringVectorAsListWithNull)
if eltype(getStringVectorAsListWithNull) != String
	error("getStringVectorAsListWithNull not String!")
end
if !isempty(getStringVectorAsListWithNull[2])
	error("getStringVectorAsListWithNull[2] not empty!")
end
callJava("setStringVectorAsListWithNull",getStringVectorAsListWithNull)

println("getStringMatrix")
if isdefined(Main, :getStringMatrix) && !isnothing(getStringMatrix)
	error("getStringMatrix already defined!")
end
getStringMatrix = callJava("getStringMatrix")
println(eltype(getStringMatrix))
println(getStringMatrix)
if eltype(getStringMatrix) != String
	error("getStringMatrix not String!")
end
callJava("setStringMatrix",getStringMatrix)


println("getStringMatrixWithNull")
if isdefined(Main, :getStringMatrixWithNull) && !isnothing(getStringMatrixWithNull)
	error("getStringMatrixWithNull already defined!")
end
getStringMatrixWithNull = callJava("getStringMatrixWithNull")
println(eltype(getStringMatrixWithNull))
println(getStringMatrixWithNull)
if eltype(getStringMatrixWithNull) != String
	error("getStringMatrixWithNull not String!")
end
if !isempty(getStringMatrixWithNull[1,1])
	error("getStringMatrixWithNull[1,1] not empty!")
end
if !isempty(getStringMatrixWithNull[2,2])
	error("getStringMatrixWithNull[2,2] not empty!")
end
if !isempty(getStringMatrixWithNull[3,3])
	error("getStringMatrixWithNull[3,3] not empty!")
end
callJava("setStringMatrixWithNull",getStringMatrixWithNull)

println("getStringMatrixAsList")
if isdefined(Main, :getStringMatrixAsList) && !isnothing(getStringMatrixAsList)
	error("getStringMatrixAsList already defined!")
end
getStringMatrixAsList = callJava("getStringMatrixAsList")
println(eltype(getStringMatrixAsList))
println(getStringMatrixAsList)
if eltype(getStringMatrixAsList) != String
	error("getStringMatrixAsList not String!")
end
callJava("setStringMatrixAsList",getStringMatrixAsList)

println("getStringMatrixAsListWithNull")
if isdefined(Main, :getStringMatrixAsListWithNull) && !isnothing(getStringMatrixAsListWithNull)
	error("getStringMatrixAsListWithNull already defined!")
end
getStringMatrixAsListWithNull = callJava("getStringMatrixAsListWithNull")
println(eltype(getStringMatrixAsListWithNull))
println(getStringMatrixAsListWithNull)
if eltype(getStringMatrixAsListWithNull) != String
	error("getStringMatrixAsListWithNull not String!")
end
if !isempty(getStringMatrixAsListWithNull[1,1])
	error("getStringMatrixAsListWithNull[1,1] not empty!")
end
if !isempty(getStringMatrixAsListWithNull[2,2])
	error("getStringMatrixAsListWithNull[2,2] not empty!")
end
if !isempty(getStringMatrixAsListWithNull[3,3])
	error("getStringMatrixAsListWithNull[3,3] not empty!")
end
callJava("setStringMatrixAsListWithNull",getStringMatrixAsListWithNull)
