FROM jboss/wildfly:10.1.0.Final

ARG console

COPY --chown=jboss:jboss docker-entrypoint.sh .

COPY --chown=jboss:jboss wildfly/scripts scripts
COPY --chown=jboss:jboss wildfly/modules ${JBOSS_HOME}/modules
COPY --chown=jboss:jboss target/aws-wrapper-wildfly-*.war ${JBOSS_HOME}/standalone/deployments/aws-wrapper-wildfly.war

RUN ${JBOSS_HOME}/bin/add-user.sh -m -u admin -p admin
RUN ${JBOSS_HOME}/bin/jboss-cli.sh --file=scripts/configure-wildfly.cli; rm -rf ${JBOSS_HOME}/standalone/configuration/standalone_xml_history

CMD ["./docker-entrypoint.sh"]
