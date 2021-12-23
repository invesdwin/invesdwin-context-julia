print("getString")
if isdefined(Main, :getString) && !isnothing(getString)
	error("getString already defined!")
end
getString = putString
print(typeof(getString))
print(getString)
if typeof(getString) != String
	error("getString not String!")
end

print("getStringWithNull")
if isdefined(Main, :getStringWithNull) && !isnothing(getStringWithNull)
	error("getStringWithNull already defined!")
end
getStringWithNull = putStringWithNull
print(typeof(getStringWithNull))
print(getStringWithNull)
if typeof(getStringWithNull) != Nothing
	error("getStringWithNull not Nothing!")
end
if !isnothing(getStringWithNull)
	error("getStringWithNull not nothing!")
end

print("getStringVector")
if isdefined(Main, :getStringVector) && !isnothing(getStringVector)
	error("getStringVector already defined!")
end
getStringVector = putStringVector
print(eltype(getStringVector))
print(getStringVector)
if eltype(getStringVector) != String
	error("getStringVector not String!")
end


print("getStringVectorWithNull")
if isdefined(Main, :getStringVectorWithNull) && !isnothing(getStringVectorWithNull)
	error("getStringVectorWithNull already defined!")
end
getStringVectorWithNull = putStringVectorWithNull
print(eltype(getStringVectorWithNull))
print(getStringVectorWithNull)
if eltype(getStringVectorWithNull) != String
	error("getStringVectorWithNull not String!")
end
if !isempty(getStringVectorWithNull[2])
	error("getStringVectorWithNull[2] not empty!")
end

print("getStringVectorAsList")
if isdefined(Main, :getStringVectorAsList) && !isnothing(getStringVectorAsList)
	error("getStringVectorAsList already defined!")
end
getStringVectorAsList = putStringVectorAsList
print(eltype(getStringVectorAsList))
print(getStringVectorAsList)
if eltype(getStringVectorAsList) != String
	error("getStringVectorAsList not String!")
end

print("getStringVectorAsListWithNull")
if isdefined(Main, :getStringVectorAsListWithNull) && !isnothing(getStringVectorAsListWithNull)
	error("getStringVectorAsListWithNull already defined!")
end
getStringVectorAsListWithNull = putStringVectorAsListWithNull
print(eltype(getStringVectorAsListWithNull))
print(getStringVectorAsListWithNull)
if eltype(getStringVectorAsListWithNull) != String
	error("getStringVectorAsListWithNull not String!")
end
if !isempty(getStringVectorAsListWithNull[2])
	error("getStringVectorAsListWithNull[2] not empty!")
end

print("getStringMatrix")
if isdefined(Main, :getStringMatrix) && !isnothing(getStringMatrix)
	error("getStringMatrix already defined!")
end
getStringMatrix = putStringMatrix
print(eltype(getStringMatrix))
print(getStringMatrix)
if eltype(getStringMatrix) != String
	error("getStringMatrix not String!")
end


print("getStringMatrixWithNull")
if isdefined(Main, :getStringMatrixWithNull) && !isnothing(getStringMatrixWithNull)
	error("getStringMatrixWithNull already defined!")
end
getStringMatrixWithNull = putStringMatrixWithNull
print(eltype(getStringMatrixWithNull))
print(getStringMatrixWithNull)
if eltype(getStringMatrixWithNull) != String
	error("getStringMatrixWithNull not String!")
end
if !isempty(getStringMatrixWithNull[1][1])
	error("getStringMatrixWithNull[1][1] not empty!")
end
if !isempty(getStringMatrixWithNull[2][2])
	error("getStringMatrixWithNull[2][2] not empty!")
end
if !isempty(getStringMatrixWithNull[3][3])
	error("getStringMatrixWithNull[3][3] not empty!")
end

print("getStringMatrixAsList")
if isdefined(Main, :getStringMatrixAsList) && !isnothing(getStringMatrixAsList)
	error("getStringMatrixAsList already defined!")
end
getStringMatrixAsList = putStringMatrixAsList
print(eltype(getStringMatrixAsList))
print(getStringMatrixAsList)
if eltype(getStringMatrixAsList) != String
	error("getStringMatrixAsList not String!")
end

print("getStringMatrixAsListWithNull")
if isdefined(Main, :getStringMatrixAsListWithNull) && !isnothing(getStringMatrixAsListWithNull)
	error("getStringMatrixAsListWithNull already defined!")
end
getStringMatrixAsListWithNull = putStringMatrixAsListWithNull
print(eltype(getStringMatrixAsListWithNull))
print(getStringMatrixAsListWithNull)
if eltype(getStringMatrixAsListWithNull) != String
	error("getStringMatrixAsListWithNull not String!")
end
if !isempty(getStringMatrixAsListWithNull[1][1])
	error("getStringMatrixAsListWithNull[1][1] not empty!")
end
if !isempty(getStringMatrixAsListWithNull[2][2])
	error("getStringMatrixAsListWithNull[2][2] not empty!")
end
if !isempty(getStringMatrixAsListWithNull[3][3])
	error("getStringMatrixAsListWithNull[3][3] not empty!")
end
