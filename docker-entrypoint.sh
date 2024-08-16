#!/bin/bash

# Jboss node name must be 23 characters or less
jboss_node_name="${HOSTNAME:${#HOSTNAME}<23?0:-23}"
bind_address=$(hostname -i)
export JAVA_OPTS="$JAVA_OPTS -Djgroups.bind_addr=$bind_address -Djboss.node.name=$jboss_node_name"

exec ${JBOSS_HOME}/bin/standalone.sh -c standalone-ha.xml -b=$bind_address -bprivate=$bind_address -bmanagement="0.0.0.0"
exit $?
