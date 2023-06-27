import random

import tensorflow as tf

SAVE_PATH = "./tf1/xw_b/"


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
    X = tf.placeholder(tf.float32, name='X')
    Y = tf.placeholder(tf.float32, name='Y')

    W = tf.Variable(tf.zeros([1]), name='W')
    B = tf.Variable(tf.zeros([1]), name='B')
    OUT = tf.add(tf.multiply(X, W), B, name='OUT')

    loss = tf.reduce_mean(tf.square(Y - OUT), name='loss')
    optimizer = tf.train.AdamOptimizer(0.005).minimize(loss)

    with tf.Session() as sess:
        sess.run(tf.global_variables_initializer())
        for epoch in range(50000):
            x_data, y_data = create_data(True)
            _, _loss, _w, _b = sess.run([optimizer, loss, W, B], feed_dict={X: x_data, Y: y_data})
            if 0 == epoch % 5000:
                print("epoch=", epoch, "_loss=", _loss, "_w=", _w, "_n=", _b)

        print("训练完成，开始预测。。。")
        for step in range(10):
            x_data, y_data = create_data(False)
            prediction_value = sess.run(OUT, feed_dict={X: x_data})
            print("x=", x_data, "y预测=", prediction_value, "y实际=", y_data)

        saver = tf.train.Saver()
        saver.save(sess, SAVE_PATH + 'model')


def predict():
    sess = tf.Session()
    saver = tf.train.import_meta_graph(SAVE_PATH + 'model.meta')
    saver.restore(sess, tf.train.latest_checkpoint(SAVE_PATH))

    graph = tf.get_default_graph()
    X = graph.get_tensor_by_name('X:0')
    Y = graph.get_tensor_by_name('Y:0')
    W = graph.get_tensor_by_name('W:0')
    B = graph.get_tensor_by_name('B:0')
    OUT = graph.get_tensor_by_name('OUT:0')
    loss = graph.get_tensor_by_name('loss:0')
    optimizer = graph.get_collection('train_op')
    # print(graph.get_all_collection_keys())
    # print(graph.get_collection('train_op'))
    # print(graph.get_collection('trainable_variables'))
    # print(graph.get_collection('variables'))
    # print(graph.get_operations())

    print("重新加载，开始预测。。。")
    for step in range(10):
        x_data, y_data = create_data(False)
        prediction_value = sess.run(OUT, feed_dict={X: [[x_data]]})
        print("x=", x_data, "y预测=", prediction_value, "y实际=", y_data)

    print("继续训练")
    for epoch in range(50000):
        x_data, y_data = create_data(True)
        _, _loss, _w, _b = sess.run([optimizer, loss, W, B], feed_dict={X: x_data, Y: y_data})
        if 0 == epoch % 5000:
            print("epoch=", epoch, "_loss=", _loss, "_w=", _w, "_n=", _b)

    print("继续训练完成，开始预测。。。")
    for step in range(10):
        x_data, y_data = create_data(False)
        prediction_value = sess.run(OUT, feed_dict={X: [[x_data]]})
        print("x=", x_data, "y预测=", prediction_value, "y实际=", y_data)


if __name__ == "__main__":
    train()
    predict()
