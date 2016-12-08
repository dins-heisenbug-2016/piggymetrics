FROM centos:7
MAINTAINER Krasilnikov Dmitry

RUN echo $'[dockerrepo] \n\
name            = Docker Repository \n\
baseurl         = https://yum.dockerproject.org/repo/main/centos/7/ \n\
enabled         = 1 \n\
gpgcheck        = 1 \n\
gpgkey=https://yum.dockerproject.org/gpg' > /etc/yum.repos.d/docker.repo

# Install Essentials
RUN yum update -y && \
    yum clean all

# Install Packages
RUN yum install -y wget && \
    yum install -y unzip && \
    yum install -y docker-engine && \
	yum install -y java-1.8.0-openjdk-devel && \
	yum install -y sudo && \
	yum clean all

# Gradle
ENV GRADLE_VERSION 3.2.1
ENV GRADLE_SHA 9843a3654d3e57dce54db06d05f18b664b95c22bf90c6becccb61fc63ce60689

RUN useradd -u 1000 -m -s /bin/bash jenkins

RUN cd /usr/lib \
 && curl -fl https://downloads.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -o gradle-bin.zip \
 && echo "$GRADLE_SHA gradle-bin.zip" | sha256sum -c - \
 && unzip "gradle-bin.zip" \
 && ln -s "/usr/lib/gradle-${GRADLE_VERSION}/bin/gradle" /usr/bin/gradle \
 && rm "gradle-bin.zip"

RUN curl -L "https://github.com/docker/compose/releases/download/1.9.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
RUN chmod +x /usr/local/bin/docker-compose

# Copy in the Docker certs, we'll use /usr/local/etc for them
COPY certs/ca.pem /home/jenkins/.docker/ca.pem
COPY certs/cert.pem /home/jenkins/.docker/cert.pem
COPY certs/key.pem /home/jenkins/.docker/key.pem

RUN chmod +r /home/jenkins/.docker/ca.pem
RUN chmod +r /home/jenkins/.docker/cert.pem
RUN chmod +r /home/jenkins/.docker/key.pem

RUN chown -R jenkins:jenkins /home/jenkins/.docker

# Set Appropriate Environmental Variables
ENV JAVA_HOME /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.111-1.b15.el7_2.x86_64/
ENV GRADLE_HOME /usr/lib/gradle
ENV PATH $PATH:$GRADLE_HOME/bin

COPY resolv.conf /etc/resolv.conf

VOLUME ["/home/jenkins/workspace"]