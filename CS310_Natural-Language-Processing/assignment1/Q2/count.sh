#!/bin/bash

input_file="$1"
output_file="$2"

function help(){
    echo -e "usage: count.sh <input file> <output file>"
}

starttime=$(date +'%Y-%m-%d %H:%M:%S')

if [ $# -lt 2 ];
then
    echo "error: too few arguments"
    help
    exit
fi

if [ ! -f $input_file ]; then
    echo "$input_file is not a file"
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

echo ">>> Convert [A-Z] to [a-z] and get valid string by re..."
tmp_res=$(cat $input_file)
tmp_res=$(echo “$tmp_res” | tr [A-Z] [a-z])

echo ">>> Modify string by re..."
# only a-z '
tmp_res=$(echo "$tmp_res" | sed "s/[^a-z|'| ]/ /g")
# tmp_res=$(echo "$tmp_res" | sed "s/[^a-z']\+/ /g")
# a'a => a a
tmp_res=$(echo "$tmp_res" | sed "s/\('\)\([^st]\)/ \2/g")
# tmp_res=$(echo "$tmp_res" | sed "s/\('\+\)\([st]*[a-ru-z]\+\)/ \2/g")
# 's => s   s' => s
tmp_res=$(echo "$tmp_res" | sed "s/ '/ /g; s/' / /g")
# tmp_res=$(echo "$tmp_res" | sed "s/'\+$/ /g; s/^'\+/ /g")
# that's's => that's s
tmp_res=$(echo "$tmp_res" | sed "s/\('[st]\)'\([st]\)/\1 \2/g")
# a'sa => a sa
tmp_res=$(echo "$tmp_res" | sed "s/'\([st][^ ]\)/ \1/g")


echo ">>> Convert line feed to space..."
tmp_res=$(echo "$tmp_res" | tr '\r\n' ' ' | tr '[:cntrl:]' ' ' | tr -s ' ' | tr ' ' '\n')

echo ">>> Count word frequency and sort..."
tmp_res=$(echo "$tmp_res" | sort | uniq -c | sort -brn)

echo ">>> Format and output..."
tmp_res=$(echo "$tmp_res" > $output_file)

echo ">>> Done! please see the result in file $output_file!"

endtime=$(date +'%Y-%m-%d %H:%M:%S')
start_seconds=$(date --date="$starttime" +%s)
end_seconds=$(date --date="$endtime" +%s)
echo ">>> Runtime: "$((end_seconds-start_seconds))"s"