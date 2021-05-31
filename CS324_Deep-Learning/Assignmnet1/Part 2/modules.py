import numpy as np

class Linear(object):
    def __init__(self, in_features, out_features):
        """
        Module initialisation.
        Args:
            in_features: input dimension
            out_features: output dimension
        TODO:
        1) Initialize weights self.params['weight'] using normal distribution with mean = 0 and 
        std = 0.0001.
        2) Initialize biases self.params['bias'] with 0. 
        3) Initialize gradients with zeros.
        """
        mean = 0
        std = 0.0001
        size = (in_features, out_features)
        self.params = {}

        self.params['weight'] = np.random.normal(mean, std, size)
        self.params['bias'] = np.zeros(out_features)
        self.gradients = {}
        
    def forward(self, x):
        """
        Forward pass (i.e., compute output from input).
        Args:
            x: input to the module
        Returns:
            out: output of the module
        Hint: Similarly to pytorch, you can store the computed values inside the object
        and use them in the backward pass computation. This is true for *all* forward methods of *all* modules in this class
        """
        self.x = x
        w = self.params['weight']
        b = self.params['bias']
        out = np.dot(x, w) + b
        self.out = out
        return out

    def backward(self, dout):
        """
        Backward pass (i.e., compute gradient).
        Args:
            dout: gradients of the previous module
        Returns:
            dx: gradients with respect to the input of the module
        TODO:
        Implement backward pass of the module. Store gradient of the loss with respect to 
        layer parameters in self.grads['weight'] and self.grads['bias']. 
        """
        # TODO
        self.gradients['weight'] = np.dot(np.transpose(self.x), dout)  / self.x.shape[0]
        self.gradients['bias'] = np.mean(dout, axis=0)
        dx = np.dot(dout, np.transpose(self.params['weight']))
        return dx

class ReLU(object):
    def forward(self, x):
        """
        Forward pass.
        Args:
            x: input to the module
        Returns:
            out: output of the module
        """
        self.x = x;
        out = np.maximum(x, 0)
        return out

    def backward(self, dout):
        """
        Backward pass.
        Args:
            dout: gradients of the previous module
        Returns:
            dx: gradients with respect to the input of the module
        """
        dx = np.where(self.x > 0, dout, 0)
        return dx

class SoftMax(object):
    def exp_normalize(self, x):
        b = x.max()
        y = np.exp(x - b)
        return y / np.reshape(y.sum(axis=1), (-1, 1))

    def forward(self, x):
        """
        Forward pass.
        Args:
            x: input to the module
        Returns:
            out: output of the module
    
        TODO:
        Implement forward pass of the module. 
        To stabilize computation you should use the so-called Max Trick
        https://timvieira.github.io/blog/post/2014/02/11/exp-normalize-trick/
        
        """
        self.x = x
        out = self.exp_normalize(x)
        self.out = out
        return out

    def backward(self, dout):
        """
        Backward pass. 
        Args:
            dout: gradients of the previous module
        Returns:
            dx: gradients with respect to the input of the module
        """
        # TODO
        dx = (dout - np.reshape(np.sum(dout * self.out, 1), [-1, 1])) * self.out
        return dx

class CrossEntropy(object):
    def forward(self, x, y):
        """
        Forward pass.
        Args:
            x: input to the module
            y: labels of the input
        Returns:
            out: cross entropy loss
        """
        # TODO
        out = np.sum(- np.log(np.maximum(x, 1e-8)) * y) / x.shape[0]
        return out

    def backward(self, x, y):
        """
        Backward pass.
        Args:
            x: input to the module
            y: labels of the input
        Returns:
            dx: gradient of the loss with respect to the input x.
        """
        dx = - y / (np.maximum(x, 1e-8))
        return dx


if __name__ == "__main__":
    softmax = SoftMax()

    crossentropy = CrossEntropy()
