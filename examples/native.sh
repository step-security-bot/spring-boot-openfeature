#!/usr/bin/env bash
DIR=$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)
cd $DIR

source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 23-graalce
sdk use java 23-graalce

GRAALVM_HOME=$HOME/.sdkman/candidates/java/current

../mvnw package -Pnative -DskipTests=true -DspringJavaFormatSkip=true -T 1 -pl :unleash-advanced -am
./unleash-advanced/target/unleash-advanced
