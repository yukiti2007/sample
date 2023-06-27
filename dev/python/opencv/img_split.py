import cv2
import imutils
import numpy as np
import matplotlib.pyplot as plt

img = cv2.imread("./IMG_2678.JPG", cv2.IMREAD_COLOR)
img = imutils.resize(img, height=800)
print("img", img.shape)

channels = cv2.split(img)

cv2.imshow("img0", channels[0])
cv2.imshow("img1", channels[1])
cv2.imshow("img2", channels[2])
print("channels[0]", channels[0].shape)
print("channels[1]", channels[1].shape)
print("channels[2]", channels[2].shape)
cv2.waitKey(0)
cv2.destroyAllWindows()

new_img0 = np.zeros(img.shape, dtype='uint8')
new_img0[:, :, 0] = channels[0]
cv2.imshow("img0", new_img0)

new_img1 = np.zeros(img.shape, dtype='uint8')
new_img1[:, :, 1] = channels[1]
cv2.imshow("img1", new_img1)

new_img2 = np.zeros(img.shape, dtype='uint8')
new_img2[:, :, 2] = channels[2]
cv2.imshow("img2", new_img2)

print("new_img0", new_img0.shape)
print("new_img1", new_img1.shape)
print("new_img2", new_img2.shape)
cv2.waitKey(0)
cv2.destroyAllWindows()

cv2.imshow("img", img)
plt.imshow(img)
plt.show()

img = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)

cv2.imshow("img", img)
plt.imshow(img)
plt.show()
