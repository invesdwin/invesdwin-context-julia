println("getCharacter")
if isdefined(Main, :getCharacter) && !isnothing(getCharacter)
	error("getCharacter already defined!")
end
getCharacter = callJava("getCharacter")
println(typeof(getCharacter))
println(getCharacter)
if typeof(getCharacter) != Char
	error("getCharacter not Char!")
end
callJava("setCharacter",getCharacter)

println("getCharacterVector")
if isdefined(Main, :getCharacterVector) && !isnothing(getCharacterVector)
	error("getCharacterVector already defined!")
end
getCharacterVector = callJava("getCharacterVector")
println(eltype(getCharacterVector))
println(getCharacterVector)
if eltype(getCharacterVector) != Char
	error("getCharacterVector not Char!")
end
callJava("setCharacterVector",getCharacterVector)

println("getCharacterVectorAsList")
if isdefined(Main, :getCharacterVectorAsList) && !isnothing(getCharacterVectorAsList)
	error("getCharacterVectorAsList already defined!")
end
getCharacterVectorAsList = callJava("getCharacterVectorAsList")
println(eltype(getCharacterVectorAsList))
println(getCharacterVectorAsList)
if eltype(getCharacterVectorAsList) != Char
	error("getCharacterVectorAsList not Char!")
end
callJava("setCharacterVectorAsList",getCharacterVectorAsList)

println("getCharacterMatrix")
if isdefined(Main, :getCharacterMatrix) && !isnothing(getCharacterMatrix)
	error("getCharacterMatrix already defined!")
end
getCharacterMatrix = callJava("getCharacterMatrix")
println(eltype(getCharacterMatrix))
println(getCharacterMatrix)
if eltype(getCharacterMatrix) != Char
	error("getCharacterMatrix not Char!")
end
callJava("setCharacterMatrix",getCharacterMatrix)

println("getCharacterMatrixAsList")
if isdefined(Main, :getCharacterMatrixAsList) && !isnothing(getCharacterMatrixAsList)
	error("getCharacterMatrixAsList already defined!")
end
getCharacterMatrixAsList = callJava("getCharacterMatrixAsList")
println(eltype(getCharacterMatrixAsList))
println(getCharacterMatrixAsList)
if eltype(getCharacterMatrixAsList) != Char
	error("getCharacterMatrixAsList not Char!")
end
callJava("setCharacterMatrixAsList",getCharacterMatrixAsList)
