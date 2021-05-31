FROM nvidia/cuda:10.1-cudnn7-devel-ubuntu18.04
LABEL maintainer youngwilliam.zheng@gmail.com

ADD sources.list /etc/apt/sources.list 

ENV DEBIAN_FRONTEND noninteractive
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential \
    cmake \
    git \
    wget \
    gcc-5 \
    g++-5 \
    libatlas-base-dev \
    libboost-all-dev \
    libgflags-dev \
    libgoogle-glog-dev \
    libhdf5-serial-dev \
    libleveldb-dev \
    liblmdb-dev \
    libopencv-dev \
    libprotobuf-dev \
    libsnappy-dev \
    protobuf-compiler \
    python3 \
    python3-dev \
    python-numpy \
    python3-pip \
    python3-setuptools \
    python-scipy && \
rm -rf /var/lib/apt/lists/*

ENV CAFFE_ROOT=/opt/caffe
WORKDIR $CAFFE_ROOT

Add data /home/data
Add . .
RUN cd python && pip3 install -r requirements.txt
RUN apt update
RUN apt install python3-opencv -y
RUN make clean && make -j"$(nproc)" all && make -j"$(nproc)" pycaffe
RUN pip3 install easydict

ENV PYCAFFE_ROOT $CAFFE_ROOT/python
ENV PYTHONPATH $PYCAFFE_ROOT:$PYTHONPATH
ENV PATH $CAFFE_ROOT/build/tools:$PYCAFFE_ROOT:$PATH
RUN echo "$CAFFE_ROOT/build/lib" >> /etc/ld.so.conf.d/caffe.conf && ldconfig