import random

import matplotlib.pyplot as plt
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


def draw():
    x_data, y_data = [], []
    for _ in range(100):
        x, y = create_data(True)
        x_data.append(x)
        y_data.append(y)
    plt.figure()
    plt.scatter(x_data, y_data)
    plt.show()


def run():
    LOSS = []
    STEP = []
    X = tf.placeholder(tf.float32, [1, 1])
    Y = tf.placeholder(tf.float32, [1, 1])

    W1 = tf.Variable(tf.random_normal([1, 10]))
    B1 = tf.Variable(tf.random_normal([10]))
    L1 = tf.nn.relu(tf.add(tf.matmul(X, W1), B1))

    W2 = tf.Variable(tf.random_normal([10, 10]))
    B2 = tf.Variable(tf.random_normal([10]))
    L2 = tf.nn.relu(tf.add(tf.matmul(L1, W2), B2))

    WOUT = tf.Variable(tf.random_normal([10, 1]))
    BOUT = tf.Variable(tf.random_normal([1]))
    OUT = tf.nn.relu(tf.add(tf.matmul(L2, WOUT), BOUT))

    loss = tf.reduce_mean(tf.square(Y - OUT))
    optimizer = tf.train.AdamOptimizer(0.005).minimize(loss)

    with tf.Session() as sess:
        sess.run(tf.global_variables_initializer())
        for epoch in range(50000):
            x_data, y_data = create_data(True)
            _, _loss = sess.run([optimizer, loss], feed_dict={X: [[x_data]], Y: [[y_data]]})
            if 0 == epoch % 500:
                LOSS.append(_loss)
                STEP.append(epoch)
            if 0 == epoch % 5000:
                print("epoch=", epoch, "_loss=", _loss)

        print("")

        for step in range(10):
            x_data, y_data = create_data(False)
            prediction_value = sess.run(OUT, feed_dict={X: [[x_data]]})
            print("x=", x_data, "y预测=", prediction_value, "y实际=", y_data)

    # plt.figure()
    # plt.scatter(STEP, LOSS)
    # plt.show()


if __name__ == "__main__":
    # draw()
    run()
