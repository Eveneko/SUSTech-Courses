# Dependence For GPU user

- [nvidia driver](https://github.com/NVIDIA/nvidia-docker/wiki/Frequently-Asked-Questions#how-do-i-install-the-nvidia-driver)
- [nvidia-docker](https://github.com/NVIDIA/nvidia-docker)

# The image base on

- [nvidia/cuda](https://hub.docker.com/r/nvidia/cuda/)

# How to run

1. Get the docker image: 

    ```
    ./docker_build.sh
    ```

    or 

    ````
    docker pull youngwilliam/olmp:gpu_python3.6
    ```

2. Run

    ```
    ./docker_run.sh path/to/parameter.json
    ```

# Result

After run the docker, you will get:

```
...
I1021 12:22:16.430500     1 solver.cpp:242]     Train net output #0: accuracy = 1
I1021 12:22:16.430508     1 solver.cpp:242]     Train net output #1: loss = 0.0139253 (* 1 = 0.0139253 loss)
I1021 12:22:16.430516     1 solver.cpp:521] Iteration 30000, lr = 0.00353553
Compression:14.568056390361182, Accuracy:1.0
random seed:961449
Time:0.6409
fit:[0.13419976745058326]
0.8658002325494167
```

The `0.8658002325494167` in the last line is the result.