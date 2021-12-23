print("getCharacter")
if(isdefined(Main, :getCharacter) && !isnothing(getCharacter)){
	error("getCharacter already defined!")
}
getCharacter = putCharacter
print(typeof(getCharacter))
print(getCharacter)
if(typeof(getCharacter) != Char){
	error("getCharacter not Char!")
}

print("getCharacterVector")
if(isdefined(Main, :getCharacterVector) && !isnothing(getCharacterVector)){
	error("getCharacterVector already defined!")
}
getCharacterVector = putCharacterVector
print(typeof(getCharacterVector))
print(getCharacterVector)
if(typeof(getCharacterVector) != Char){
	error("getCharacterVector not Char!")
}

print("getCharacterVectorAsList")
if(isdefined(Main, :getCharacterVectorAsList) && !isnothing(getCharacterVectorAsList)){
	error("getCharacterVectorAsList already defined!")
}
getCharacterVectorAsList = putCharacterVectorAsList
print(typeof(getCharacterVectorAsList))
print(getCharacterVectorAsList)
if(typeof(getCharacterVectorAsList) != Char){
	error("getCharacterVectorAsList not Char!")
}

print("getCharacterMatrix")
if(isdefined(Main, :getCharacterMatrix) && !isnothing(getCharacterMatrix)){
	error("getCharacterMatrix already defined!")
}
getCharacterMatrix = putCharacterMatrix
print(typeof(getCharacterMatrix))
print(getCharacterMatrix)
if(typeof(getCharacterMatrix) != Char){
	error("getCharacterMatrix not Char!")
}

print("getCharacterMatrixAsList")
if(isdefined(Main, :getCharacterMatrixAsList) && !isnothing(getCharacterMatrixAsList)){
	error("getCharacterMatrixAsList already defined!")
}
getCharacterMatrixAsList = putCharacterMatrixAsList
print(typeof(getCharacterMatrixAsList))
print(getCharacterMatrixAsList)
if(typeof(getCharacterMatrixAsList) != Char){
	error("getCharacterMatrixAsList not Char!")
}