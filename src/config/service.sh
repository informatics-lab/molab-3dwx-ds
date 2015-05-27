#!/bin/sh

APP_NAME=molab-3dwx-ds
APP_DIR=$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )
LOG_DIR=${APP_DIR}/log
START_SCRIPT=${APP_DIR}/${APP_NAME}.sh
PID_FILE=${APP_DIR}/${APP_NAME}.pid
HEADLESS_SETTING=-Djava.awt.headless=true
MEMORY_SETTINGS="-Xmx512m -XX:PermSize=64m -XX:MaxPermSize=128m"
JMX_SETTINGS="-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=1072 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false"
START_CMD="java ${MEMORY_SETTINGS} ${HEADLESS_SETTING} ${JMX_SETTINGS} -jar ${APP_NAME}.jar"

app_start() {
    if [[ $EUID -eq 0 ]]; then
       echo "This application should not be run as root!" 1>&2
       echo "Switching to user 'appd' and starting ${APP_NAME}..." 1>&2
       sudo -u appd ${START_SCRIPT} start
       exit $?
    fi
    if ! [ -d "$LOG_DIR" ]; then
        mkdir -p ${LOG_DIR}
    fi
    $(nohup ${START_CMD} 1>&2 > ${LOG_DIR}/${APP_NAME}-nohup.log) &
    echo $! > ${PID_FILE}
}

app_running() {
  copiesRunning=$(ps -ef | grep "${START_CMD}" | grep -v grep | wc -l)
  if [[ 1 -lt ${copiesRunning} ]]; then
    echo "ERROR: There appear to be multiple (${copiesRunning}) copies of ${APP_NAME} running"
    return 0;
  fi
  if [[ 1 -eq ${copiesRunning} ]]; then
    echo $(ps -ef | grep "${START_CMD}" | grep -v grep | awk '{ print $2 }') > ${PID_FILE}
    return 0;
  fi
  return 1;
}

start() {
  if app_running; then
    :;
  else
    if [ -f ${PID_FILE} ]; then
      rm -rf ${PID_FILE};
    fi
    app_start;
        echo "Giving ${APP_NAME} 5 seconds to start..."
        sleep 5;
  fi
  status;
}

stop() {
  if app_running; then
    kill $(<${PID_FILE});
    echo "Waiting for ${APP_NAME} to finish..."
    while ps -p `cat ${PID_FILE}` > /dev/null; do sleep 1; done;
    rm -rf ${PID_FILE};
  else
    echo "${APP_NAME} does not appear to be running";
    return 1;
  fi
}

restart() {
   stop;
   start;
}

status() {
  if app_running; then
    echo "${APP_NAME} is running with process id" $(<${PID_FILE});
  else
    echo "${APP_NAME} does not appear to be running";
    return 1;
  fi
}

case $1 in
  start) start;;
  stop) stop;;
  restart) restart;;
  status) status;;
  *) echo "Usage: $0 (start|stop|restart|status)";
     exit 2;;
esac

exit $?;