
import matplotlib.pyplot as plt
import numpy as np

img = np.array([
  [0, 1, 1, 1, 1],
  [0, 0, 1, 1, 1],
  [0, 0, 0, 1, 1],
  [0, 0, 0, 1, 1]
])

fig = plt.figure()
# turn off the axes:
ax = fig.add_axes([0, 0, 1, 1])
ax.set_axis_off()
ax.imshow(img)
plt.show()

print(img)

