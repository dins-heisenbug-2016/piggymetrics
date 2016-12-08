FROM centos:7
MAINTAINER Krasilnikov Dmitry

# Install Essentials
RUN yum update -y && \
    yum clean all

# Install Packages
RUN yum install -y wget && \
    yum install -y unzip && \
	yum install -y java-1.8.0-openjdk-devel && \
	yum install -y sudo && \
	yum clean all

# Gradle
ENV ALLURE_VERSION 2.0-BETA1
ENV ALLURE_SHA a6f6380776219be42997ccf68df1faa7e90b93d5a8925bc7b04ee67d0a8888ab

RUN useradd -u 1000 -m -s /bin/bash jenkins

RUN cd /usr/lib \
 && curl -fL https://github.com/allurefw/allure-report/releases/download/${ALLURE_VERSION}/allure-commandline.zip -o allure-commandline.zip \
 && echo "$ALLURE_SHA allure-commandline.zip" | sha256sum -c - \
 && unzip "allure-commandline.zip" -d allure-commandline\
 && ln -s "/usr/lib/allure-commandline/bin/allure" /usr/bin/allure \
 && rm "allure-commandline.zip"

# Set Appropriate Environmental Variables
ENV JAVA_HOME /usr/lib/jvm/java-1.8.0-openjdk-1.8.0.111-1.b15.el7_2.x86_64/
ENV ALLURE_HOME /usr/lib/allure-commandline
ENV PATH $PATH:$ALLURE_HOME/bin

COPY resolv.conf /etc/resolv.conf

VOLUME ["/home/jenkins/workspace"]