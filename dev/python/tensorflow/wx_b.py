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
    LOSS=[]
    STEP=[]
    X = tf.placeholder(tf.float32)
    Y = tf.placeholder(tf.float32)

    W = tf.Variable(tf.zeros([1]))
    B = tf.Variable(tf.zeros([1]))
    OUT = X * W + B

    loss = tf.reduce_mean(tf.square(Y - OUT))
    optimizer = tf.train.AdamOptimizer(0.005).minimize(loss)

    with tf.Session() as sess:
        sess.run(tf.global_variables_initializer())
        for epoch in range(50000):
            x_data, y_data = create_data(True)
            _, _loss, _w, _b = sess.run([optimizer, loss, W, B], feed_dict={X: x_data, Y: y_data})
            if 5000 >= epoch and 0 == epoch % 30:
                LOSS.append(_loss)
                STEP.append(epoch)
            if 0 == epoch % 5000:
                print("epoch=",epoch,"_loss=", _loss, "_w=", _w, "_n=", _b)

        print("")

        for step in range(10):
            x_data, y_data = create_data(False)
            prediction_value = sess.run(OUT, feed_dict={X: x_data})
            print("x=", x_data, "y预测=", prediction_value, "y实际=", y_data)

    plt.figure()
    plt.scatter(STEP,LOSS)
    plt.show()

if __name__ == "__main__":
    draw()
    run()
