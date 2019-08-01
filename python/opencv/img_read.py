import numpy as np
import cv2
import imutils

img1 = cv2.imread("./IMG_2678.JPG", cv2.IMREAD_GRAYSCALE)
img2 = cv2.imread("./IMG_2678.JPG", cv2.IMREAD_COLOR)
img3 = cv2.imread("./IMG_2678.JPG", cv2.IMREAD_ANYDEPTH)
img4 = cv2.imread("./IMG_2678.JPG", cv2.IMREAD_ANYCOLOR)

print("cv2.IMREAD_GRAYSCALE", img1.shape)
print("cv2.IMREAD_COLOR", img2.shape)
print("cv2.IMREAD_ANYDEPTH", img3.shape)
print("cv2.IMREAD_ANYCOLOR", img4.shape)

cv2.imshow("IMREAD_GRAYSCALE", imutils.resize(img1, height=512))
cv2.imshow("IMREAD_COLOR", imutils.resize(img2, height=512))
cv2.imshow("IMREAD_ANYDEPTH", imutils.resize(img3, height=512))
cv2.imshow("IMREAD_ANYCOLOR", imutils.resize(img4, height=512))
cv2.waitKey(0)
cv2.destroyAllWindows()

img1 = cv2.imread("./IMG_2678.JPG", cv2.IMREAD_IGNORE_ORIENTATION)
img2 = cv2.imread("./IMG_2678.JPG", cv2.IMREAD_LOAD_GDAL)
img3 = cv2.imread("./IMG_2678.JPG", cv2.IMREAD_UNCHANGED)

print("cv2.IMREAD_IGNORE_ORIENTATION", img1.shape)
print("cv2.IMREAD_LOAD_GDAL", img2.shape)
print("cv2.IMREAD_UNCHANGED", img3.shape)

cv2.imshow("IMREAD_IGNORE_ORIENTATION", imutils.resize(img1, height=512))
cv2.imshow("IMREAD_LOAD_GDAL", imutils.resize(img2, height=512))
cv2.imshow("IMREAD_UNCHANGED", imutils.resize(img3, height=512))
cv2.waitKey(0)
cv2.destroyAllWindows()

img1 = cv2.imread("./IMG_2678.JPG", cv2.IMREAD_REDUCED_COLOR_2)
img2 = cv2.imread("./IMG_2678.JPG", cv2.IMREAD_REDUCED_COLOR_4)
img3 = cv2.imread("./IMG_2678.JPG", cv2.IMREAD_REDUCED_COLOR_8)

print("cv2.IMREAD_REDUCED_COLOR_2", img1.shape)
print("cv2.IMREAD_REDUCED_COLOR_4", img2.shape)
print("cv2.IMREAD_REDUCED_COLOR_8", img3.shape)

cv2.imshow("IMREAD_REDUCED_COLOR_2", imutils.resize(img1, height=512))
cv2.imshow("IMREAD_REDUCED_COLOR_4", imutils.resize(img2, height=512))
cv2.imshow("IMREAD_REDUCED_COLOR_8", imutils.resize(img3, height=512))
cv2.waitKey(0)
cv2.destroyAllWindows()

img1 = cv2.imread("./IMG_2678.JPG", cv2.IMREAD_REDUCED_GRAYSCALE_2)
img2 = cv2.imread("./IMG_2678.JPG", cv2.IMREAD_REDUCED_GRAYSCALE_4)
img3 = cv2.imread("./IMG_2678.JPG", cv2.IMREAD_REDUCED_GRAYSCALE_8)

print("cv2.IMREAD_REDUCED_GRAYSCALE_2", img1.shape)
print("cv2.IMREAD_REDUCED_GRAYSCALE_4", img2.shape)
print("cv2.IMREAD_REDUCED_GRAYSCALE_8", img3.shape)

cv2.imshow("IMREAD_REDUCED_GRAYSCALE_2", imutils.resize(img1, height=512))
cv2.imshow("IMREAD_REDUCED_GRAYSCALE_4", imutils.resize(img2, height=512))
cv2.imshow("IMREAD_REDUCED_GRAYSCALE_8", imutils.resize(img3, height=512))
cv2.waitKey(0)
cv2.destroyAllWindows()
