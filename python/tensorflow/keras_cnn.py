# coding:utf-8
from captcha.image import ImageCaptcha
import random
from PIL import Image
import numpy as np
import tensorflow as tf

number = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9']
alphabet = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
            'v', 'w', 'x', 'y', 'z']
ALPHABET = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
            'V', 'W', 'X', 'Y', 'Z']
SAVE_PATH = "D:/test/tf2/keras_cnn/"
CHAR_SET = number + alphabet + ALPHABET
CHAR_SET_LEN = len(CHAR_SET)
IMAGE_HEIGHT = 60
IMAGE_WIDTH = 160


def random_captcha_text(char_set=None, captcha_size=4):
    if char_set is None:
        char_set = number + alphabet + ALPHABET

    captcha_text = []
    for i in range(captcha_size):
        c = random.choice(char_set)
        captcha_text.append(c)
    return captcha_text


def gen_captcha_text_and_image(width=160, height=60, char_set=CHAR_SET):
    image = ImageCaptcha(width=width, height=height)

    captcha_text = random_captcha_text(char_set)
    captcha_text = ''.join(captcha_text)

    captcha = image.generate(captcha_text)

    captcha_image = Image.open(captcha)
    captcha_image = np.array(captcha_image)
    return captcha_text, captcha_image


text, image = gen_captcha_text_and_image(char_set=CHAR_SET)
MAX_CAPTCHA = len(text)
print('CHAR_SET_LEN=', CHAR_SET_LEN, ' MAX_CAPTCHA=', MAX_CAPTCHA)


def convert2gray(img):
    if len(img.shape) > 2:
        gray = np.mean(img, -1)
        return gray
    else:
        return img


def text2vec(text):
    vector = np.zeros([MAX_CAPTCHA, CHAR_SET_LEN])
    for i, c in enumerate(text):
        idx = CHAR_SET.index(c)
        vector[i][idx] = 1.0
    return vector


def vec2text(vec):
    text = []
    for i, c in enumerate(vec):
        text.append(CHAR_SET[c])
    return "".join(text)


def get_next_batch(batch_size=128):
    batch_x = np.zeros([batch_size, IMAGE_HEIGHT, IMAGE_WIDTH, 1])
    batch_y = np.zeros([batch_size, MAX_CAPTCHA, CHAR_SET_LEN])

    def wrap_gen_captcha_text_and_image():
        while True:
            text, image = gen_captcha_text_and_image(char_set=CHAR_SET)
            if image.shape == (60, 160, 3):
                return text, image

    for i in range(batch_size):
        text, image = wrap_gen_captcha_text_and_image()
        image = tf.reshape(convert2gray(image), (IMAGE_HEIGHT, IMAGE_WIDTH, 1))
        batch_x[i, :] = image
        batch_y[i, :] = text2vec(text)

    return batch_x, batch_y


def crack_captcha_cnn():
    model = tf.keras.Sequential()

    model.add(tf.keras.layers.Conv2D(32, (3, 3)))
    model.add(tf.keras.layers.PReLU())
    model.add(tf.keras.layers.MaxPool2D((2, 2), strides=2))

    model.add(tf.keras.layers.Conv2D(64, (5, 5)))
    model.add(tf.keras.layers.PReLU())
    model.add(tf.keras.layers.MaxPool2D((2, 2), strides=2))

    model.add(tf.keras.layers.Conv2D(128, (5, 5)))
    model.add(tf.keras.layers.PReLU())
    model.add(tf.keras.layers.MaxPool2D((2, 2), strides=2))

    model.add(tf.keras.layers.Flatten())
    model.add(tf.keras.layers.Dense(MAX_CAPTCHA * CHAR_SET_LEN))
    model.add(tf.keras.layers.Reshape([MAX_CAPTCHA, CHAR_SET_LEN]))
    model.add(tf.keras.layers.Softmax())

    return model


def train():
    try:
        model = tf.keras.models.load_model(SAVE_PATH + 'model')
    except Exception as e:
        print('#######Exception', e)
        model = crack_captcha_cnn()

    model.compile(optimizer='Adam',
                  metrics=['accuracy'],
                  loss='categorical_crossentropy')

    for times in range(500000):
        batch_x, batch_y = get_next_batch(512)
        print('times=', times, ' batch_x.shape=', batch_x.shape, ' batch_y.shape=', batch_y.shape)
        model.fit(batch_x, batch_y, epochs=4)
        print("y预测=\n", np.argmax(model.predict(batch_x), axis=2))
        print("y实际=\n", np.argmax(batch_y, axis=2))

        if 0 == times % 10:
            print("save model at times=", times)
            model.save(SAVE_PATH + 'model')


def predict():
    model = tf.keras.models.load_model(SAVE_PATH + 'model')
    success = 0
    count = 100
    for _ in range(count):
        data_x, data_y = get_next_batch(1)
        prediction_value = model.predict(data_x)
        data_y = vec2text(np.argmax(data_y, axis=2)[0])
        prediction_value = vec2text(np.argmax(prediction_value, axis=2)[0])

        if data_y == prediction_value:
            print("y预测=", prediction_value, "y实际=", data_y, "预测成功。")
            success += 1
        else:
            print("y预测=", prediction_value, "y实际=", data_y, "预测失败。")

    print("预测", count, "次", "成功率 =", success / count)

    pass


if __name__ == "__main__":
    train()
    # predict()
