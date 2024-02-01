# https://hub.docker.com/_/openjdk
# https://hub.docker.com/_/maven
FROM maven:3.8-openjdk-8 AS build

RUN apt-get update
RUN curl -sL https://deb.nodesource.com/setup_18.x | bash -
RUN apt-get install -y nodejs

RUN apt-get update && \
    apt-get install -y gnupg wget curl unzip --no-install-recommends && \
    wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list && \
    apt-get update -y && \
    apt-get install -y google-chrome-stable

# https://developer.chrome.com/blog/chrome-for-testing
# RUN npx -y @puppeteer/browsers install chrome@stable
RUN npx -y @puppeteer/browsers install chromedriver@stable
RUN mv $(find /chromedriver -type f -name chromedriver) /chromedriver


# Create a default user
RUN groupadd --system automation && \
    useradd --system --create-home --gid automation  automation && \
    chown --recursive automation:automation /home/automation

WORKDIR /source

COPY . .
RUN mkdir /home/automation/.m2
RUN mv settings.xml /home/automation/.m2/settings.xml
RUN chown -R automation.automation /home/automation

RUN chown -R automation.automation /source
USER automation


# Put Chromedriver into the PATH
ENV PATH /chromedriver:$PATH
# set display port to avoid crash
ENV DISPLAY=:99

# RUN mvn clean compile test

ENTRYPOINT ["mvn", "clean","compile", "test"]

