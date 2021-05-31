#!/bin/bash

input_file1="$1"
input_file2="$2"
output_file="$3"

function help(){
    echo -e "usage: map.sh <input file1> <input file2> <output file>"
}

starttime=`date +'%Y-%m-%d %H:%M:%S'`

if [ $# -lt 3 ];
then
    echo "error: too few arguments"
    help
    exit
fi

if [ ! -f $input_file1 ]; then
    echo "$input_file1 is not a file"
    exit
fi

if [ ! -f $input_file2 ]; then
    echo "$input_file2 is not a file"
    exit
fi

if [ ! -f "$output_file" ]; then
    touch "$output_file"
else
    rm "$output_file"
    touch "$output_file"
fi
echo "Vaild arguments"
echo "---------------"

declare -A map=()

echo ">>> Save the data in map..."
while read line; do
    arr=($line)
    map[${arr[1]}]=${arr[0]}
done < $input_file1

echo ">>> Map the input"
while read line; do
    echo ${map[$line]} >> $output_file
done < $input_file2

echo ">>> Done! please see the result in file $output_file!"

endtime=`date +'%Y-%m-%d %H:%M:%S'`
start_seconds=$(date --date="$starttime" +%s)
end_seconds=$(date --date="$endtime" +%s)
echo ">>> Runtime: "$((end_seconds-start_seconds))"s"