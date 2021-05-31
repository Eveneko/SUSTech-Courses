from __future__ import absolute_import
from __future__ import division
from __future__ import print_function

from modules import * 

class MLP(object):

    def __init__(self, n_inputs, n_hidden, n_classes):
        """
        Initializes multi-layer perceptron object.    
        Args:
            n_inputs: number of inputs (i.e., dimension of an input vector).
            n_hidden: list of integers, where each integer is the number of units in each linear layer
            n_classes: number of classes of the classification problem (i.e., output dimension of the network)
        """
        self.n_inputs = n_inputs
        self.n_hidden = n_hidden
        self.n_classes = n_classes

        self.layers = []
        n_pre = n_inputs
        for n_units in n_hidden:
            self.layers.append(Linear(n_pre, n_units))
            self.layers.append(ReLU())
            pre = n_units
        self.layers.append(Linear(pre, n_classes))
        self.layers.append(SoftMax())

    def forward(self, x):
        """
        Predict network output from input by passing it through several layers.
        Args:
            x: input to the network
        Returns:
            out: output of the network
        """
        out = x
        for layer in self.layers:
            out = layer.forward(out)
        return out

    def backward(self, dout):
        """
        Performs backward propagation pass given the loss gradients. 
        Args:
            dout: gradients of the loss
        """
        for layer in self.layers[::-1]:
            dout = layer.backward(dout)
        return dout


if __name__ == '__main__':
    pass
