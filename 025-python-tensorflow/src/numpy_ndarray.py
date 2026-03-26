
import numpy as np

numpy3DArray = np.array([
  [
    [1, 2],
    [3, 4]
  ], [
    [5, 6],
    [7, 8]
  ]
])

theType = type(numpy3DArray)
print("Type of numpy3DArray: " + theType.__module__ + "." + theType.__qualname__)

#
# Python 3 supports operator overloading, do I can divide
# the numpy.ndarray elementwise like in Matlab.
#
# I guess this is done by overloading __truediv__, see:
#   https://numpy.org/doc/stable/reference/generated/numpy.ndarray.__truediv__.html
#   https://docs.python.org/3/library/operator.html#operator.__truediv__
#
numpy3DArrayDivided = numpy3DArray / 4

#
# Unlike in Matlab, in Python, the indexes of numpy.ndarray start at 0:
#
print(f"Element 1,1,0 before division: {numpy3DArray[1][1][0]}")
print(f"Element 1,1,0 after division: {numpy3DArrayDivided[1][1][0]}")

