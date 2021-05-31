#!/bin/bash
 
# echo "usage: -$0 <file1> <file2>"
 
function judge(){
    file1=$1
    file2=$2
    file3="result.out"
    
    if [ -f $file1 ] && [ -f $file2 ]
    then
        g++ -w Banker.cpp -o Banker
        ./Banker < $file1 > result.out
        di=$(diff -bc $file2 $file3)
        if [ $? != 0 ]
        then
            echo "Sorry, WA!"
            echo "$di"
        else
            echo "Congratulation, AC!"
        fi
    else
        echo "$file1 or $file2 does not exist."
    fi
}

judge "Sample.in" "Sample.out"
judge "Sample2.in" "Sample2.out"
judge "Sample3.in" "Sample3.out"
judge "Sample4.in" "Sample4.out"