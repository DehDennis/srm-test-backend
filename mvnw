#!/bin/sh
# ----------------------------------------------------------------------------
# Maven Start Up Batch script for Unix/macOS
# ----------------------------------------------------------------------------

# Resolve o caminho do diretório base do projeto
PRG="$0"
while [ -h "$PRG" ]; do
  ls=`ls -ld "$PRG"`
  link=`expr "$ls" : '.*-> \(.*\)$'`
  if expr "$link" : '/.*' > /dev/null; then
    PRG="$link"
  else
    PRG=`dirname "$PRG"`/"$link"
  fi
done
SAVED=`pwd`
CDPATH=""
cd "`dirname "$PRG"`/.." >/dev/null
JAVACMD="$JAVA_HOME/bin/java"

# Executa o JAR do wrapper localizado em .mvn/wrapper/
exec "$JAVACMD" -jar .mvn/wrapper/maven-wrapper.jar "$@"
