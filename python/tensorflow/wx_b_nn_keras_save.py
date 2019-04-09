import random

import numpy as np
import tensorflow as tf

SAVE_PATH = "D:/test/tf1/xw_b_nn_keras/"


def create_data(for_train=False):
    w = 5.33
    b = -23.26
    x = random.random() * 30
    y = w * x + b

    if for_train:
        noise = (random.random() - 0.5) * 10
        y += noise

    return x, y


def train():
    model = tf.keras.Sequential()
    model.add(tf.keras.layers.Dense(10))
    model.add(tf.keras.layers.Dense(10))
    model.add(tf.keras.layers.Dense(1))
    model.compile(optimizer='Adam',
                  loss='mse')

    for _ in range(5000):
        x_train, y_train = create_data(True)
        x_train = np.array(x_train, ndmin=2)
        y_train = np.array(y_train, ndmin=2)
        model.fit(x_train, y_train)

    print("训练完成，开始预测。。。")
    for _ in range(10):
        x_data, y_data = create_data(False)
        x_data = np.array(x_data, ndmin=2)
        prediction_value = model.predict(x_data)
        print("x=", x_data, "y预测=", prediction_value, "y实际=", y_data)

    model.save(SAVE_PATH + 'model')


def predict():
    model = tf.keras.models.load_model(SAVE_PATH + 'model')

    print("重新加载，开始预测。。。")
    for _ in range(10):
        x_data, y_data = create_data(False)
        x_data = np.array(x_data, ndmin=2)
        prediction_value = model.predict(x_data)
        print("x=", x_data, "y预测=", prediction_value, "y实际=", y_data)

    print("继续训练")
    for _ in range(5000):
        x_train, y_train = create_data(True)
        x_train = np.array(x_train, ndmin=2)
        y_train = np.array(y_train, ndmin=2)
        model.fit(x_train, y_train)

    print("继续训练完成，开始预测。。。")
    for _ in range(10):
        x_data, y_data = create_data(False)
        x_data = np.array(x_data, ndmin=2)
        prediction_value = model.predict(x_data)
        print("x=", x_data, "y预测=", prediction_value, "y实际=", y_data)


if __name__ == "__main__":
    train()
    predict()
