from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

import argparse
import numpy as np
import os
from mlp_numpy import MLP
from modules import CrossEntropy, Linear

import random
from sklearn import datasets
import matplotlib.pyplot as plt

# Default constants
DNN_HIDDEN_UNITS_DEFAULT = '20'
LEARNING_RATE_DEFAULT = 1e-2
MAX_EPOCHS_DEFAULT = 200
EVAL_FREQ_DEFAULT = 10
GRADIENT_DESCENT_DEFAULT = 'BGD'

FLAGS = None


def draw_figure(train_x, train_y, test_x, test_y, train_acc_list, train_loss_list, test_acc_list, test_loss_list):
    plt.title('train data, make_moons')
    plt.scatter(train_x[:,0], train_x[:,1])
    plt.xlabel('train_x[:,0]')
    plt.ylabel('train_x[:,1]')
    plt.savefig('./img/train_moon.png')
    plt.show()

    plt.cla()

    plt.title('test data, make_moons')
    plt.scatter(test_x[:,0], test_x[:,1])
    plt.xlabel('test_x[:,0]')
    plt.ylabel('test_x[:,1]')
    plt.savefig('./img/test_moon.png')
    plt.show()

    plt.cla()

    epoch = [i for i in range(FLAGS.max_steps)]
    plt.title('train accuracy cuve')
    plt.plot(epoch, train_acc_list)
    plt.xlabel('epochs')
    plt.ylabel('accuracy')
    plt.savefig('./img/train_accuracy_curve.png')
    plt.show()

    plt.cla()

    epoch = [i for i in range(FLAGS.max_steps)]
    plt.title('train loss cuve')
    plt.plot(epoch, train_loss_list)
    plt.xlabel('epochs')
    plt.ylabel('loss')
    plt.savefig('./img/train_loss_curve.png')
    plt.show()

    plt.cla()

    epoch = [i for i in range(FLAGS.max_steps)]
    plt.title('test accuracy cuve')
    plt.plot(epoch, test_acc_list)
    plt.xlabel('epochs')
    plt.ylabel('accuracy')
    plt.savefig('./img/test_accuracy_curve.png')
    plt.show()

    plt.cla()

    epoch = [i for i in range(FLAGS.max_steps)]
    plt.title('test loss cuve')
    plt.plot(epoch, test_loss_list)
    plt.xlabel('epochs')
    plt.ylabel('loss')
    plt.savefig('./img/test_loss_curve.png')
    plt.show()


def generate_dataset():
    data_size = 200
    train_size = 160
    x, y = datasets.make_moons(data_size, shuffle=True, noise=None)
    # one hot
    data_shape = 2
    y = np.eye(data_shape)[y.reshape(-1)]
    train_x = x[:train_size]
    train_y = y[:train_size]
    test_x = x[train_size:]
    test_y = y[train_size:]

    return train_x, train_y, test_x, test_y



def accuracy(predictions, targets):
    """
    Computes the prediction accuracy, i.e., the average of correct predictions
    of the network.
    Args:
        predictions: 2D float array of size [number_of_data_samples, n_classes]
        labels: 2D int array of size [number_of_data_samples, n_classes] with one-hot encoding of ground-truth labels
    Returns:
        accuracy: scalar float, the accuracy of predictions.
    """
    accuracy = len(np.where(predictions == targets)[0]) / len(targets)
    return accuracy


def train_BGD(train_x, train_y, test_x, test_y):
    n_input = len(train_x[0])
    n_hidden = list(map(int, FLAGS.dnn_hidden_units.split()))
    n_classes = len(train_y[0])
    mlp = MLP(n_input, n_hidden, n_classes)
    cs = CrossEntropy()

    train_acc_list = []
    train_loss_list = []
    test_acc_list = []
    test_loss_list = []

    for epoch in range(1, FLAGS.max_steps+1):

        for i in range(len(train_x)):
            x = train_x[[i]]
            y = train_y[[i]]
            out = mlp.forward(x)
            cle = cs.backward(out, y)
            mlp.backward(cle)

            for layer in mlp.layers:
                if isinstance(layer, Linear):
                    layer.params['weight'] -= FLAGS.learning_rate * layer.gradients['weight']
                    layer.params['bias'] -= FLAGS.learning_rate * layer.gradients['bias']

        train_out = mlp.forward(train_x)
        test_out = mlp.forward(test_x)
        train_pred = np.argmax(train_out, axis=1)
        test_pred = np.argmax(test_out, axis=1)
        train_loss = cs.forward(train_out, train_y)
        test_loss = cs.forward(test_out, test_y)
        train_targets = np.argmax(train_y, axis=1)
        test_targets = np.argmax(test_y, axis=1)
        train_acc = accuracy(train_pred, train_targets)
        test_acc = accuracy(test_pred, test_targets)

        train_acc_list.append(train_acc)
        train_loss_list.append(train_loss)
        test_acc_list.append(test_acc)
        test_loss_list.append(test_loss)


        if epoch % FLAGS.eval_freq == 0:
            print(f'epoch {epoch}: train acc {train_acc}; train loss {train_loss}; test acc {test_acc}; test loss{test_loss}')

    return train_acc_list, train_loss_list, test_acc_list, test_loss_list


def train_SGD(train_x, train_y, test_x, test_y):
    n_input = len(train_x[0])
    n_hidden = list(map(int, FLAGS.dnn_hidden_units.split()))
    n_classes = len(train_y[0])
    mlp = MLP(n_input, n_hidden, n_classes)
    cs = CrossEntropy()

    train_acc_list = []
    train_loss_list = []
    test_acc_list = []
    test_loss_list = []

    for epoch in range(1, FLAGS.max_steps+1):

        random_num = random.randint(0, len(train_x)-1)
        x = train_x[[random_num]]
        y = train_y[[random_num]]
        out = mlp.forward(x)
        cle = cs.backward(out, y)
        mlp.backward(cle)

        for layer in mlp.layers:
            if isinstance(layer, Linear):
                layer.params['weight'] -= FLAGS.learning_rate * layer.gradients['weight']
                layer.params['bias'] -= FLAGS.learning_rate * layer.gradients['bias']

        train_out = mlp.forward(train_x)
        test_out = mlp.forward(test_x)
        train_pred = np.argmax(train_out, axis=1)
        test_pred = np.argmax(test_out, axis=1)
        train_loss = cs.forward(train_out, train_y)
        test_loss = cs.forward(test_out, test_y)
        train_targets = np.argmax(train_y, axis=1)
        test_targets = np.argmax(test_y, axis=1)
        train_acc = accuracy(train_pred, train_targets)
        test_acc = accuracy(test_pred, test_targets)

        train_acc_list.append(train_acc)
        train_loss_list.append(train_loss)
        test_acc_list.append(test_acc)
        test_loss_list.append(test_loss)


        if epoch % FLAGS.eval_freq == 0:
            print(f'epoch {epoch}: train acc {train_acc}; train loss {train_loss}; test acc {test_acc}; test loss{test_loss}')

    return train_acc_list, train_loss_list, test_acc_list, test_loss_list


def train(train_x, train_y, test_x, test_y):
    """
    Performs training and evaluation of MLP model.
    NOTE: You should the model on the whole test set each eval_freq iterations.
    """
    # YOUR TRAINING CODE GOES HERE
    if FLAGS.grdient_descent == 'BGD':
        train_acc_list, train_loss_list, test_acc_list, test_loss_list = train_BGD(train_x, train_y, test_x, test_y)
    elif FLAGS.grdient_descent == 'SGD':
        train_acc_list, train_loss_list, test_acc_list, test_loss_list = train_SGD(train_x, train_y, test_x, test_y)

    return train_acc_list, train_loss_list, test_acc_list, test_loss_list


def main():
    """
    Main function
    """
    train_x, train_y, test_x, test_y = generate_dataset()
    train_acc_list, train_loss_list, test_acc_list, test_loss_list = train(train_x, train_y, test_x, test_y)
    draw_figure(train_x, train_y, test_x, test_y, train_acc_list, train_loss_list, test_acc_list, test_loss_list)


if __name__ == '__main__':
    # Command line arguments
    parser = argparse.ArgumentParser()
    parser.add_argument('--dnn_hidden_units', type=str, default=DNN_HIDDEN_UNITS_DEFAULT,
                      help='Comma separated list of number of units in each hidden layer')
    parser.add_argument('--learning_rate', type=float, default=LEARNING_RATE_DEFAULT,
                      help='Learning rate')
    parser.add_argument('--max_steps', type=int, default=MAX_EPOCHS_DEFAULT,
                      help='Number of epochs to run trainer.')
    parser.add_argument('--eval_freq', type=int, default=EVAL_FREQ_DEFAULT,
                      help='Frequency of evaluation on the test set')
    parser.add_argument('--grdient_descent', type=str, default=GRADIENT_DESCENT_DEFAULT,
                      help='BGD or SGD')
    FLAGS, unparsed = parser.parse_known_args()
    main()

