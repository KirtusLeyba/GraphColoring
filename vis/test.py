import matplotlib.pyplot as plt

fig, ax = plt.subplots(nrows=2, ncols=2)

plt.subplot(2, 2, 1)
plt.plot([1, 2, 3, 4, 5])

plt.subplot(2, 2, 2)
plt.plot([1, 2, 3, 4, 5])

plt.subplot(2, 2, 3)
plt.plot([1, 2, 3, 4, 5])

plt.subplot(2, 2, 4)
plt.plot([1, 2, 3, 4, 5])

plt.show()