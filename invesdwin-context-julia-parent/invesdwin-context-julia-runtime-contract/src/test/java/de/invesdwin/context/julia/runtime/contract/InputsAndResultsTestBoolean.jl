print("getBoolean")
if isdefined(Main, :getBoolean) && !isnothing(getBoolean)
	error("getBoolean already defined!")
end
getBoolean = putBoolean
print(typeof(getBoolean))
print(getBoolean)
if typeof(getBoolean) != Bool
	error("getBoolean not Bool!")
end

print("getBooleanVector")
if isdefined(Main, :getBooleanVector) && !isnothing(getBooleanVector)
	error("getBooleanVector already defined!")
end
getBooleanVector = putBooleanVector
print(eltype(getBooleanVector))
print(getBooleanVector)
if eltype(getBooleanVector) != Bool 
	error("getBooleanVector not Bool!")
end

print("getBooleanVectorAsList")
if isdefined(Main, :getBooleanVectorAsList) && !isnothing(getBooleanVectorAsList)
	error("getBooleanVectorAsList already defined!")
end
getBooleanVectorAsList = putBooleanVectorAsList
print(eltype(getBooleanVectorAsList))
print(getBooleanVectorAsList)
if eltype(getBooleanVectorAsList) != Bool
	error("getBooleanVectorAsList not Bool!")
end

print("getBooleanMatrix")
if isdefined(Main, :getBooleanMatrix) && !isnothing(getBooleanMatrix)
	error("getBooleanMatrix already defined!")
end
getBooleanMatrix = putBooleanMatrix
print(eltype(getBooleanMatrix))
print(getBooleanMatrix)
if eltype(getBooleanMatrix) != Bool
	error("getBooleanMatrix not Bool!")
end

print("getBooleanMatrixAsList")
if isdefined(Main, :getBooleanMatrixAsList) && !isnothing(getBooleanMatrixAsList)
	error("getBooleanMatrixAsList already defined!")
end
getBooleanMatrixAsList = putBooleanMatrixAsList
print(eltype(getBooleanMatrixAsList))
print(getBooleanMatrixAsList)
if eltype(getBooleanMatrixAsList) != Bool
	error("getBooleanMatrixAsList not Bool!")
end
