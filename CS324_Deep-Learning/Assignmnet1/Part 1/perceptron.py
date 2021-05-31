import numpy as np

def gen_gaussian_distribution(size, mean=None, cov=None):
    if not mean:
        mean = np.random.randn(2)
    if not cov:
        cov = np.eye(2)
    data = np.random.multivariate_normal(mean, cov, size)
    return data

class Perceptron(object):

    def __init__(self, n_inputs, max_epochs=1e3, learning_rate=1e-2):
        """
        Initializes perceptron object.
        Args:
            n_inputs: number of inputs.
            max_epochs: maximum number of training cycles.
            learning_rate: magnitude of weight changes at each training cycle
        """
        self.n_inputs = n_inputs
        self.max_epochs = max_epochs
        self.learning_rate = learning_rate
        self.weights = np.zeros(self.n_inputs)
        self.bias = 0
        
        
    def forward(self, input):
        """
        Predict label from input 
        Args:
            input: array of dimension equal to n_inputs.
        """
        sum = np.sign(np.dot(input, self.weights))
        label = np.where(sum > 0, 1, -1)
        return label
        
    def train(self, training_inputs, labels):
        """
        Train the perceptron
        Args:
            training_inputs: list of numpy arrays of training points.
            labels: arrays of expected output value for the corresponding point in training_inputs.
        """
        train_size = len(training_inputs)
        epochs = 0
        while epochs < self.max_epochs:
            epochs += 1
            for i in range(train_size):
                if np.any(labels[i] * (np.dot(self.weights, training_inputs[i]) + self.bias) <= 0):
                    self.weights = self.weights + (self.learning_rate * labels[i] * training_inputs[i]).T
                    self.bias = self.bias + self.learning_rate * labels[i]
    
    def score(self, test_inputs, test_labels):
        pred_arr = np.where(self.forward(test_inputs) > 0, 1, -1)
        true_size = len(np.where(pred_arr == test_labels)[0])
        return true_size / len(test_labels)
    

def main():
    p = Perceptron(2)
    
    """
    gen dataset
    """
    data_size = 100
    train_size = 80
    x1 = gen_gaussian_distribution(data_size, [5, 5])
    x2 = gen_gaussian_distribution(data_size, [-5, -5])
    y1 = a_label = np.ones(data_size, dtype=np.int16)
    y2 = -y1
    x_train = np.concatenate((x1[:train_size], x2[:train_size]), axis=0)
    y_train = np.concatenate((y1[:train_size], y2[:train_size]), axis=0)
    x_test = np.concatenate((x1[train_size:], x2[train_size:]), axis=0)
    y_test = np.concatenate((y1[train_size:], y2[train_size:]), axis=0)
    
    """
    train model
    """
    p.train(x_train, y_train)
    
    """
    test model
    """
    acc = p.score(x_test, y_test)
    print(f'Perceptron test accuracy: {acc * 100}%')


if __name__ == "__main__":
    main()