# Use official Tomcat 10 with JDK 17 (Jakarta EE 9 compatible)
FROM tomcat:10.1-jdk17

# Remove default ROOT and other sample apps
RUN rm -rf /usr/local/tomcat/webapps/*

# Copy the Spring Boot WAR (already packaged for external Tomcat)
COPY target/student-management-boot.war /usr/local/tomcat/webapps/ROOT.war

# Verify WAR is copied
RUN ls -la /usr/local/tomcat/webapps/

# Set environment variables for memory tuning
ENV CATALINA_OPTS="-Xmx512m -Xms256m -server -XX:+UseG1GC"
ENV JAVA_OPTS="-Djava.awt.headless=true"

# Expose Tomcat port
EXPOSE 8080

# Healthcheck for app startup
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Start Tomcat
CMD ["catalina.sh", "run"]