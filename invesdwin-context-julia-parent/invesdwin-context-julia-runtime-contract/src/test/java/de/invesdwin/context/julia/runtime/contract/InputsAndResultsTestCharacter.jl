print("getCharacter")
if isdefined(Main, :getCharacter) && !isnothing(getCharacter)
	error("getCharacter already defined!")
end
getCharacter = putCharacter
print(typeof(getCharacter))
print(getCharacter)
if typeof(getCharacter) != Char
	error("getCharacter not Char!")
end

print("getCharacterVector")
if isdefined(Main, :getCharacterVector) && !isnothing(getCharacterVector)
	error("getCharacterVector already defined!")
end
getCharacterVector = putCharacterVector
print(eltype(getCharacterVector))
print(getCharacterVector)
if eltype(getCharacterVector) != Char
	error("getCharacterVector not Char!")
end

print("getCharacterVectorAsList")
if isdefined(Main, :getCharacterVectorAsList) && !isnothing(getCharacterVectorAsList)
	error("getCharacterVectorAsList already defined!")
end
getCharacterVectorAsList = putCharacterVectorAsList
print(eltype(getCharacterVectorAsList))
print(getCharacterVectorAsList)
if eltype(getCharacterVectorAsList) != Char
	error("getCharacterVectorAsList not Char!")
end

print("getCharacterMatrix")
if isdefined(Main, :getCharacterMatrix) && !isnothing(getCharacterMatrix)
	error("getCharacterMatrix already defined!")
end
getCharacterMatrix = putCharacterMatrix
print(eltype(getCharacterMatrix))
print(getCharacterMatrix)
if eltype(getCharacterMatrix) != Char
	error("getCharacterMatrix not Char!")
end

print("getCharacterMatrixAsList")
if isdefined(Main, :getCharacterMatrixAsList) && !isnothing(getCharacterMatrixAsList)
	error("getCharacterMatrixAsList already defined!")
end
getCharacterMatrixAsList = putCharacterMatrixAsList
print(eltype(getCharacterMatrixAsList))
print(getCharacterMatrixAsList)
if eltype(getCharacterMatrixAsList) != Char
	error("getCharacterMatrixAsList not Char!")
end