# https://hub.docker.com/_/openjdk
# https://hub.docker.com/_/maven
FROM maven:3.8-openjdk-8 AS build

RUN apt-get update && \
    apt-get install -y gnupg wget curl unzip --no-install-recommends && \
    wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    echo "deb http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google.list && \
    apt-get update -y && \
    apt-get install -y google-chrome-stable && \
    CHROMEVER=$(google-chrome --product-version | grep -o "[^\.]*\.[^\.]*\.[^\.]*") && \
    DRIVERVER=$(curl -s "https://chromedriver.storage.googleapis.com/LATEST_RELEASE_$CHROMEVER") && \
    wget -q --continue -P /chromedriver "http://chromedriver.storage.googleapis.com/$DRIVERVER/chromedriver_linux64.zip" && \
    unzip /chromedriver/chromedriver* -d /chromedriver

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

