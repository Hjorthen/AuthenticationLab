if ! pgrep -x "rmiregistry" > /dev/null
then
    cd bin/
    rmiregistry &
    cd ..
fi
java -classpath bin/ -Djava.rmi.server.codebase=file:bin/ --module-path mysql-connector-java-8.0.18/ Server.Server &
java  -classpath bin/ Client.Client
