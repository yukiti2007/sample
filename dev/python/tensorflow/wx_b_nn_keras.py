import random

import numpy as np
import tensorflow as tf


def create_data(for_train=False):
    w = 5.33
    b = -23.26
    x = random.random() * 30
    y = w * x + b

    if for_train:
        noise = (random.random() - 0.5) * 10
        y += noise

    return x, y


def run():
    model = tf.keras.Sequential()
    model.add(tf.keras.layers.Dense(10))
    model.add(tf.keras.layers.Dense(10))
    model.add(tf.keras.layers.Dense(1))
    model.compile(optimizer=tf.train.AdamOptimizer(0.001),
                  loss='mse')

    for _ in range(5000):
        x_train, y_train = create_data(True)
        x_train = np.array(x_train, ndmin=2)
        y_train = np.array(y_train, ndmin=2)
        model.fit(x_train, y_train)

    for _ in range(10):
        x_data, y_data = create_data(False)
        x_data = np.array(x_data, ndmin=2)
        prediction_value = model.predict(x_data)
        print("x=", x_data, "y预测=", prediction_value, "y实际=", y_data)


if __name__ == "__main__":
    run()
