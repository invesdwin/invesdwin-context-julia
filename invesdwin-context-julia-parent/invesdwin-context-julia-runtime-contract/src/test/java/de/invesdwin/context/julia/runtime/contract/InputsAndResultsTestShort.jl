print("getShort")
if(isdefined(Main, :getShort) && !isnothing(getShort)){
	error("getShort already defined!")
}
getShort = putShort
print(typeof(getShort))
print(getShort)
if(typeof(getShort) != Int16){
	error("getShort not Int16!")
}

print("getShortVector")
if(isdefined(Main, :getShortVector) && !isnothing(getShortVector)){
	error("getShortVector already defined!")
}
getShortVector = putShortVector
print(typeof(getShortVector))
print(getShortVector)
if(typeof(getShortVector) != Int16){
	error("getShortVector not Int16!")
}

print("getShortVectorAsList")
if(isdefined(Main, :getShortVectorAsList) && !isnothing(getShortVectorAsList)){
	error("getShortVectorAsList already defined!")
}
getShortVectorAsList = putShortVectorAsList
print(typeof(getShortVectorAsList))
print(getShortVectorAsList)
if(typeof(getShortVectorAsList) != Int16){
	error("getShortVectorAsList not Int16!")
}

print("getShortMatrix")
if(isdefined(Main, :getShortMatrix) && !isnothing(getShortMatrix)){
	error("getShortMatrix already defined!")
}
getShortMatrix = putShortMatrix
print(typeof(getShortMatrix))
print(getShortMatrix)
if(typeof(getShortMatrix) != Int16){
	error("getShortMatrix not Int16!")
}

print("getShortMatrixAsList")
if(isdefined(Main, :getShortMatrixAsList) && !isnothing(getShortMatrixAsList)){
	error("getShortMatrixAsList already defined!")
}
getShortMatrixAsList = putShortMatrixAsList
print(typeof(getShortMatrixAsList))
print(getShortMatrixAsList)
if(typeof(getShortMatrixAsList) != Int16){
	error("getShortMatrixAsList not Int16!")
}
