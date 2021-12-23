print("getLong")
if isdefined(Main, :getLong) && !isnothing(getLong)
	error("getLong already defined!")
end
getLong = putLong
print(typeof(getLong))
print(getLong)
if typeof(getLong) != Int64
	error("getLong not Int64!")
end

print("getLongVector")
if isdefined(Main, :getLongVector) && !isnothing(getLongVector)
	error("getLongVector already defined!")
end
getLongVector = putLongVector
print(eltype(getLongVector))
print(getLongVector)
if eltype(getLongVector) != Int64
	error("getLongVector not Int64!")
end

print("getLongVectorAsList")
if isdefined(Main, :getLongVectorAsList) && !isnothing(getLongVectorAsList)
	error("getLongVectorAsList already defined!")
end
getLongVectorAsList = putLongVectorAsList
print(eltype(getLongVectorAsList))
print(getLongVectorAsList)
if eltype(getLongVectorAsList) != Int64
	error("getLongVectorAsList not Int64!")
end

print("getLongMatrix")
if isdefined(Main, :getLongMatrix) && !isnothing(getLongMatrix)
	error("getLongMatrix already defined!")
end
getLongMatrix = putLongMatrix
print(eltype(getLongMatrix))
print(getLongMatrix)
if eltype(getLongMatrix) != Int64
	error("getLongMatrix not Int64!")
end

print("getLongMatrixAsList")
if isdefined(Main, :getLongMatrixAsList) && !isnothing(getLongMatrixAsList)
	error("getLongMatrixAsList already defined!")
end
getLongMatrixAsList = putLongMatrixAsList
print(eltype(getLongMatrixAsList))
print(getLongMatrixAsList)
if eltype(getLongMatrixAsList) != Int64
	error("getLongMatrixAsList not Int64!")
end
