#!/bin/bash

#clear &&
#echo -e "\n+----------------------" &&
#echo Compiling NearNeural...\\ &&
#echo +--------------------------------------------- &&
#javac -O NearNeural/*.java &&
#
#		  
#echo -e "\n+------------------"&&
#echo Compiling others...\\&&
#echo +---------------------------------------------&&
#javac -O *.java &&
#		  
#		  
#echo -e "\n+---------" &&
#echo Running...\\ &&
#echo +--------------------------------------------- &&
##date > errorLog &&
date
cd Bin
java NearNeuralExample 

#echo -e "\n+-------------------" &&
#echo Beginning JavaDoc...\\ &&
#echo +--------------------------------------------- &&
#rm -r docs
#javadoc -d docs -private -version -author -quiet NearNeural/*.java
