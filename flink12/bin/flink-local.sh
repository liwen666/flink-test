#!/usr/bin/env bash

PROJECT_HOME=/home/liwen/flink12
LIB_PATH=/home/liwen/flink12/examples
logFile=/data/log/query/start_`date "+%Y%m%d%H%M%S"`.out
APP_PATH=""
JAVA_OPT=" -XX:+UseG1GC -Xms1024M -Xms1024M -XX:MaxDirectMemorySize=268435458 -XX:MaxMetaspaceSize=268435456 "
MAIN_CLASS=""




for i in $(seq 1 $#)
do
#动态配置flink地址
if [ "x${@:i:1}" = "x--main_class" ]; then
	MAIN_CLASS=":: ${@:i+1:1}"
fi
#动态配置flink地址
if [ "x${@:i:1}" = "x--project_home" ]; then
	PROJECT_HOME="${@:i+1:1}"
fi

#动态配置外部依赖
if [ "x${@:i:1}" = "x--lib_path" ]; then
	LIB_PATH="${@:i+1:1}"
fi

#动态配置外部依赖
if [ "x${@:i:1}" = "x--app_path" ]; then
	APP_PATH="${@:i+1:1}"
fi
#动态配置外部依赖
if [ "x${@:i:1}" = "x--logInfoFile" ]; then
	logFile="${@:i+1:1}"
fi


done

if [ "$MAIN_CLASS" = "" ]; then
      echo "缺少主类 请指定参数 --main_class  主类"$@
        exit 1
fi

if [ "x$LIB_PATH" = "x" ]; then
echo "脚本参数异常"$@
        exit 1
fi

if [ "x$PROJECT_HOME" = "x" ]; then
echo "脚本参数异常"$@
        exit 1
fi
echo  PROJECT_HOME $PROJECT_HOME
echo  MAIN_CLASS $MAIN_CLASS
echo  LIB_PATH $LIB_PATH
echo  APP_PATH $APP_PATH

CLASSPATH=$APP_PATH

for jarfile in "$PROJECT_HOME"/lib/*.jar
do
   CLASSPATH="$CLASSPATH:$jarfile";
done

for jarfile in "$LIB_PATH"/*.jar
do
   CLASSPATH="$CLASSPATH:$jarfile";
done
echo -----$CLASSPATH------

mkdir -vp /data/log/query
echo ----日志路径是------$logFile
java $JAVA_OPT  -classpath $CLASSPATH$MAIN_CLASS  $@   > $logFile & tail -f $logFile


