import traceback
from collections import defaultdict

import cv2
import imutils
import numpy as np
from imutils import paths


class CardExtractor:

    def __init__(self, image):

        # 卡片的高度和宽度
        self._card_ratio = 0.6
        self.card_height = int(540 * self._card_ratio)  # 卡片高度
        self.card_width = int(856 * self._card_ratio)  # 卡片宽度

        # 将图片按照特定高度进行缩放，并算出缩放以后的宽度
        self.img_height = 480  # 图片高度
        self._image = imutils.resize(image, height=self.img_height)
        self.img_width = self._image.shape[1]
        if self.img_width < self.card_width:
            self.img_width = int(self.card_width * 1.2)
            self._image = imutils.resize(image, width=self.img_width)
            self.img_height = self._image.shape[0]
        self.center = (int(self.img_width / 2), int(self.img_height / 2))

        # 算出四边留空的距离
        self._gap_height = int((self.img_height - self.card_height) / 2)
        self._gap_width = int((self.img_width - self.card_width) / 2)
        self._valid_distance = int(self.card_height / 2)
        self._invalid_distance = int(self.card_height / 4.8)

        # 算出卡片标准框的位置
        self._black_points = [(self._gap_width, self._gap_height), (self.img_width - self._gap_width, self._gap_height),
                              (self.img_width - self._gap_width, self.img_height - self._gap_height),
                              (self._gap_width, self.img_height - self._gap_height)]
        self._black_contour = np.array(
            [self._black_points[0], self._black_points[1], self._black_points[2], self._black_points[3]])

    @staticmethod
    def _line_2_xy(line):
        x1, y1, x2, y2 = 0, 0, 0, 0
        for rho, theta in line:
            a = np.cos(theta)
            b = np.sin(theta)
            x0 = a * rho
            y0 = b * rho
            x1 = int(x0 + 1000 * (-b))
            y1 = int(y0 + 1000 * (a))
            x2 = int(x0 - 1000 * (-b))
            y2 = int(y0 - 1000 * (a))
        return x1, y1, x2, y2

    # 将输入的所有线分类，按照角度分成两类
    @staticmethod
    def _segment_by_angle_kmeans(lines, k=2, **kwargs):
        """Groups lines based on angle with k-means.

        Uses k-means on the coordinates of the angle on the unit circle
        to segment `k` angles inside `lines`.
        """

        # Define criteria = (type, max_iter, epsilon)
        default_criteria_type = cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER
        criteria = kwargs.get('criteria', (default_criteria_type, 10, 1.0))
        flags = kwargs.get('flags', cv2.KMEANS_RANDOM_CENTERS)
        attempts = kwargs.get('attempts', 10)

        # returns angles in [0, pi] in radians
        angles = np.array([line[0][1] for line in lines])
        # multiply the angles by two and find coordinates of that angle
        pts = np.array([[np.cos(2 * angle), np.sin(2 * angle)]
                        for angle in angles], dtype=np.float32)

        # run kmeans on the coords
        labels, centers = cv2.kmeans(pts, k, None, criteria, attempts, flags)[1:]
        labels = labels.reshape(-1)  # transpose to row vec

        # segment lines based on their kmeans label
        segmented = defaultdict(list)
        for i, line in zip(range(len(lines)), lines):
            segmented[labels[i]].append(line)
        segmented = list(segmented.values())
        return segmented

    # 获得两条线的焦点
    @staticmethod
    def _intersection(line1, line2):
        """Finds the intersection of two lines given in Hesse normal form.

        Returns closest integer pixel locations.
        See https://stackoverflow.com/a/383527/5087436
        """
        rho1, theta1 = line1[0]
        rho2, theta2 = line2[0]
        A = np.array([
            [np.cos(theta1), np.sin(theta1)],
            [np.cos(theta2), np.sin(theta2)]
        ])
        b = np.array([[rho1], [rho2]])
        x0, y0 = np.linalg.solve(A, b)
        x0, y0 = int(np.round(x0)), int(np.round(y0))
        return (x0, y0)

    # 获得两类线的所有焦点
    @staticmethod
    def _segmented_intersections(lines):
        """Finds the intersections between groups of lines."""

        intersections = []
        for i, group in enumerate(lines[:-1]):
            for next_group in lines[i + 1:]:
                for line1 in group:
                    for line2 in next_group:
                        intersections.append(CardExtractor._intersection(line1, line2))

        return intersections

    # 在所有焦点中获取里卡片标准框四角最近的焦点
    def _get_points(self, intersections):
        points = self._black_points.copy()
        points_r = self._black_points.copy()
        for index in range(len(self._black_points)):
            point = []
            dis = []
            for intersection in intersections:
                distance = np.math.sqrt(
                    np.math.pow(points[index][0] - intersection[0], 2)
                    + np.math.pow(points[index][1] - intersection[1], 2))
                if self._valid_distance > distance:
                    point.append(intersection)
                    dis.append(distance)
            if 0 < len(point):
                points[index] = tuple(np.mean(point, axis=0, dtype=int))
            max = self._valid_distance
            for intersection in intersections:
                distance = np.math.sqrt(
                    np.math.pow(points[index][0] - intersection[0], 2)
                    + np.math.pow(points[index][1] - intersection[1], 2))
                if max > distance:
                    max = distance
                    points_r[index] = intersection

        return points_r

    def card_extract(self):
        """
        在输入的图片中提取卡片的位置，并做梯形变换以后返回
        :return:    image_o:    标记以后的原图
        :return:    img_card:   提取出的卡片
        """

        roop_count = 0
        cnt_count = 1000
        while 600 < cnt_count:
            # 灰度化处理
            image = cv2.cvtColor(self._image, cv2.COLOR_BGR2GRAY)

            # 中值模糊
            img_g = cv2.medianBlur(image, 1 + roop_count * 2)
            roop_count += 1

            # 自适应阀值调整
            image = cv2.adaptiveThreshold(img_g, 255, cv2.ADAPTIVE_THRESH_GAUSSIAN_C, cv2.THRESH_BINARY_INV, 19, 2)

            cnts, _ = cv2.findContours(image, cv2.RETR_LIST, cv2.CHAIN_APPROX_SIMPLE)
            cnt_count = len(cnts)

        LINE_COUNT = 4
        THETA_L = 0.2
        roop_count = 0
        line_count1 = 0
        line_count2 = 0
        while LINE_COUNT > line_count1 or LINE_COUNT > line_count2:
            try:
                # 获取所有直线
                new_lines = []
                lines = cv2.HoughLines(image, 1 + 0.1 * roop_count, np.pi / 180, 300)
                roop_count += 1
                if LINE_COUNT * 2 > len(lines):
                    continue
                x3, y3 = self.center
                for line in lines:
                    theta_l = line[0][1]
                    if theta_l < THETA_L or np.pi / 2 - THETA_L < theta_l < np.pi / 2 + THETA_L or theta_l > np.pi - THETA_L:
                        x1, y1, x2, y2 = CardExtractor._line_2_xy(line)
                        dis_12 = np.math.sqrt(np.math.pow(x2 - x1, 2) + np.math.pow(y2 - y1, 2))
                        s = np.abs(x1 * y2 + x2 * y3 + x3 * y1 - x1 * y3 - x2 * y1 - x3 * y2) / 2
                        h = s / dis_12
                        if h > self._invalid_distance:
                            new_lines.append(line)
                if LINE_COUNT * 2 > len(new_lines):
                    continue

                # 将所有直线分为垂直的两类
                segmented = CardExtractor._segment_by_angle_kmeans(new_lines)

                line_count1 = len(segmented[0])
                line_count2 = len(segmented[1])
            except TypeError:
                traceback.print_exc()
                if 50 < roop_count:
                    break
                pass

        # 获取两类垂直线的所有焦点
        intersections = CardExtractor._segmented_intersections(segmented)

        # 在所有焦点中找出离标准点范围内最近的四个作为卡片的顶点
        points = self._get_points(intersections)

        # 根据获取的四个角顶点，提取出卡片内容并做梯形变换
        src_points = np.float32((points[0], points[1], points[2], points[3]))
        dst_points = np.float32(
            [[0, 0], [self.card_width, 0], [self.card_width, self.card_height], [0, self.card_height]])
        perspective_matrix = cv2.getPerspectiveTransform(np.array(src_points), np.array(dst_points))
        img_card = cv2.warpPerspective(self._image.copy(), perspective_matrix, (self.card_width, self.card_height))

        # 在原图上标记提示框
        image_o = cv2.drawContours(self._image.copy(), [self._black_contour], -1, (0, 0, 0), 2)

        # 在原图上标记探测到的直线
        if 2 <= len(segmented):
            for line in segmented[0]:
                x1, y1, x2, y2 = CardExtractor._line_2_xy(line)
                image_o = cv2.line(image_o, (x1, y1), (x2, y2), (0, 0, 255), 1)
            for line in segmented[1]:
                x1, y1, x2, y2 = CardExtractor._line_2_xy(line)
                image_o = cv2.line(image_o, (x1, y1), (x2, y2), (0, 255, 0), 1)

        # 在原图上标记四个角的可识别范围
        for point in self._black_points:
            image_o = cv2.circle(image_o, point, self._valid_distance, (255, 255, 255))

        # 在原图上标记探测出的四个角位置
        for point in points:
            image_o = cv2.circle(image_o, point, 5, (0, 255, 255))
            image_o = cv2.putText(image_o, "(" + str(point[0]) + "," + str(point[1]) + ")", point,
                                  cv2.FONT_HERSHEY_SIMPLEX, .5, (0, 255, 255), 1)

        return image_o, img_card


if __name__ == "__main__":
    type = "bk"
    path = "F:/ocr/idcard/" + type
    image_paths = paths.list_images(path)
    for path in image_paths:
        try:
            # path = "F:/ocr/idcard/ft/2cdeccf946f14ed985c97195dee423ab.jpeg"
            image = cv2.imread(path)

            print(str(image.shape) + path)

            # image = imutils.resize(image, 1000)
            cardExtractor = CardExtractor(image)
            img1, img2 = cardExtractor.card_extract()
            cv2.imshow("img1", img1)
            cv2.imshow("img2", img2)
            cv2.waitKey(0)
            cv2.imwrite("F:/ocr/idcard/" + type + "out/" + str(path[path.find("\\"):]), img2)
            # cv2.imwrite("F:/ocr/idcard/ftout/out01.jpg", img2, [int(cv2.IMWRITE_JPEG_CHROMA_QUALITY), 10])
            # cv2.imwrite("F:/ocr/idcard/ftout/out02.jpg", img2, [int(cv2.IMWRITE_JPEG_LUMA_QUALITY), 10])
            # cv2.imwrite("F:/ocr/idcard/ftout/out03.jpg", img2, [int(cv2.IMWRITE_JPEG_OPTIMIZE), 0])
            # cv2.imwrite("F:/ocr/idcard/ftout/out04.jpg", img2, [int(cv2.IMWRITE_JPEG_PROGRESSIVE), 0])
            # cv2.imwrite("F:/ocr/idcard/ftout/out05.jpg", img2, [int(cv2.IMWRITE_JPEG_QUALITY), 10])
            # cv2.imwrite("F:/ocr/idcard/ftout/out06.jpg", img2, [int(cv2.IMWRITE_JPEG_RST_INTERVAL), 10])
            # cv2.imwrite("F:/ocr/idcard/ftout/out07.png", img2, [int(cv2.IMWRITE_PNG_BILEVEL), 1])
            # cv2.imwrite("F:/ocr/idcard/ftout/out08.png", img2, [int(cv2.IMWRITE_PNG_COMPRESSION), 9])
            # cv2.imwrite("F:/ocr/idcard/ftout/out09.png", img2, [int(cv2.IMWRITE_PNG_STRATEGY), ])
            # cv2.imwrite("F:/ocr/idcard/ftout/out10.png", img2, [int(cv2.IMWRITE_PNG_STRATEGY_DEFAULT), ])
            # cv2.imwrite("F:/ocr/idcard/ftout/out11.png", img2, [int(cv2.IMWRITE_PNG_STRATEGY_FILTERED), ])
            # cv2.imwrite("F:/ocr/idcard/ftout/out12.png", img2, [int(cv2.IMWRITE_PNG_STRATEGY_FIXED), ])
            # cv2.imwrite("F:/ocr/idcard/ftout/out13.png", img2, [int(cv2.IMWRITE_PNG_STRATEGY_HUFFMAN_ONLY), ])
            # cv2.imwrite("F:/ocr/idcard/ftout/out14.png", img2, [int(cv2.IMWRITE_PNG_STRATEGY_RLE), ])
            # cv2.imwrite("F:/ocr/idcard/ftout/out15.pxm", img2, [int(cv2.IMWRITE_PXM_BINARY), ])
            # cv2.imwrite("F:/ocr/idcard/ftout/out16.bmp", img2, [int(cv2.IMWRITE_WEBP_QUALITY), ])
        except BaseException:
            traceback.print_exc()
    cv2.destroyAllWindows()
