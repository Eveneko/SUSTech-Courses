#! /bin/bash

# set -e - 有异常就退出
# set -o pipefail - 管道内有任何一个命令返回值不为0就失败
# set -u - 把未定义的变量视为错误
# set -x - 可以让Bash把每个命令在执行前先打印出来
set -euxo pipefail

# pwd=`pwd`
targ="$(readlink -f "$1")" # target文件夹地址
dist="$(readlink -f "$2")" # 输出地址
cnt_f=0 # 文件数量
cnt_d=0 # 文件夹数量
queue=() # 文件夹列表
ptr1=0
ptr2=0

function read ()
{
    echo "[${1##*/}]" # 删掉最后一个 / 及其左边的字符串
    for file in `ls "$1"`
        do
            echo "$1"/"$file"
            if [ -d "$1"/"$file" ]
                then
                    queue[$ptr1]="$1"/"$file"
                    # let "ptr1++"
                    ptr1=$((ptr1+1))
                    # let "cnt_d++"
                    cnt_d=$((cnt_d+1))
                else
                    # let "cnt_f++"
                    cnt_f=$((cnt_f+1))
            fi
        done
    echo
}

OLD_IFS=$IFS #保存原始值
# echo -e - 输出转义字符
# echo -n - 不换行输出
IFS=$(echo -en "\n\t") #改变IFS的值

read "$targ" > "$dist"

while [ $ptr1 -ne $ptr2 ]
    do
    read "${queue[$ptr2]}" >> $dist
    # let "ptr2++"
    ptr2=$((ptr2+1))
    done

echo "[Directories Count]:$cnt_d" >> $dist
echo "[Files Count]:$cnt_f" >> $dist

IFS=$OLD_IFS #还原IFS的原始值