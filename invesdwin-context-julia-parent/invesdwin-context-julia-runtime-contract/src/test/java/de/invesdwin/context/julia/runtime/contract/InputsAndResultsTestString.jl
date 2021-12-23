print("getString")
if(isdefined(Main, :getString) && !isnothing(getString)){
	error("getString already defined!")
}
getString = putString
print(typeof(getString))
print(getString)
if(typeof(getString) != String){
	error("getString not String!")
}

print("getStringWithNull")
if(isdefined(Main, :getStringWithNull) && !isnothing(getStringWithNull)){
	error("getStringWithNull already defined!")
}
getStringWithNull = putStringWithNull
print(typeof(getStringWithNull))
print(getStringWithNull)
if(typeof(getStringWithNull) != String){
	error("getStringWithNull not String!")
}
if(!isnothing(getStringWithNull)){
	error("getStringWithNull not nothing!")
}

print("getStringVector")
if(isdefined(Main, :getStringVector) && !isnothing(getStringVector)){
	error("getStringVector already defined!")
}
getStringVector = putStringVector
print(typeof(getStringVector))
print(getStringVector)
if(typeof(getStringVector) != String){
	error("getStringVector not String!")
}


print("getStringVectorWithNull")
if(isdefined(Main, :getStringVectorWithNull) && !isnothing(getStringVectorWithNull)){
	error("getStringVectorWithNull already defined!")
}
getStringVectorWithNull = putStringVectorWithNull
print(typeof(getStringVectorWithNull))
print(getStringVectorWithNull)
if(typeof(getStringVectorWithNull) != String){
	error("getStringVectorWithNull not String!")
}
if(!isnothing(getStringVectorWithNull[2])){
	error("getStringVectorWithNull[2] not nothing!")
}

print("getStringVectorAsList")
if(isdefined(Main, :getStringVectorAsList) && !isnothing(getStringVectorAsList)){
	error("getStringVectorAsList already defined!")
}
getStringVectorAsList = putStringVectorAsList
print(typeof(getStringVectorAsList))
print(getStringVectorAsList)
if(typeof(getStringVectorAsList) != String){
	error("getStringVectorAsList not String!")
}

print("getStringVectorAsListWithNull")
if(isdefined(Main, :getStringVectorAsListWithNull) && !isnothing(getStringVectorAsListWithNull)){
	error("getStringVectorAsListWithNull already defined!")
}
getStringVectorAsListWithNull = putStringVectorAsListWithNull
print(typeof(getStringVectorAsListWithNull))
print(getStringVectorAsListWithNull)
if(typeof(getStringVectorAsListWithNull) != String){
	error("getStringVectorAsListWithNull not String!")
}
if(!isnothing(getStringVectorAsListWithNull[2])){
	error("getStringVectorAsListWithNull[2] not nothing!")
}

print("getStringMatrix")
if(isdefined(Main, :getStringMatrix) && !isnothing(getStringMatrix)){
	error("getStringMatrix already defined!")
}
getStringMatrix = putStringMatrix
print(typeof(getStringMatrix))
print(getStringMatrix)
if(typeof(getStringMatrix) != String){
	error("getStringMatrix not String!")
}


print("getStringMatrixWithNull")
if(isdefined(Main, :getStringMatrixWithNull) && !isnothing(getStringMatrixWithNull)){
	error("getStringMatrixWithNull already defined!")
}
getStringMatrixWithNull = putStringMatrixWithNull
print(typeof(getStringMatrixWithNull))
print(getStringMatrixWithNull)
if(typeof(getStringMatrixWithNull) != String){
	error("getStringMatrixWithNull not String!")
}
if(!isnothing(getStringMatrixWithNull[1][1])){
	error("getStringMatrixWithNull[1][1] not nothing!")
}
if(!isnothing(getStringMatrixWithNull[2][2])){
	error("getStringMatrixWithNull[2][2] not nothing!")
}
if(!isnothing(getStringMatrixWithNull[3][3])){
	error("getStringMatrixWithNull[3][3] not nothing!")
}

print("getStringMatrixAsList")
if(isdefined(Main, :getStringMatrixAsList) && !isnothing(getStringMatrixAsList)){
	error("getStringMatrixAsList already defined!")
}
getStringMatrixAsList = putStringMatrixAsList
print(typeof(getStringMatrixAsList))
print(getStringMatrixAsList)
if(typeof(getStringMatrixAsList) != String){
	error("getStringMatrixAsList not String!")
}

print("getStringMatrixAsListWithNull")
if(isdefined(Main, :getStringMatrixAsListWithNull) && !isnothing(getStringMatrixAsListWithNull)){
	error("getStringMatrixAsListWithNull already defined!")
}
getStringMatrixAsListWithNull = putStringMatrixAsListWithNull
print(typeof(getStringMatrixAsListWithNull))
print(getStringMatrixAsListWithNull)
if(typeof(getStringMatrixAsListWithNull) != String){
	error("getStringMatrixAsListWithNull not String!")
}
if(!isnothing(getStringMatrixAsListWithNull[1][1])){
	error("getStringMatrixAsListWithNull[1][1] not nothing!")
}
if(!isnothing(getStringMatrixAsListWithNull[2][2])){
	error("getStringMatrixAsListWithNull[2][2] not nothing!")
}
if(!isnothing(getStringMatrixAsListWithNull[3][3])){
	error("getStringMatrixAsListWithNull[3][3] not nothing!")
}
