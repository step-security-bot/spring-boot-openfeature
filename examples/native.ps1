$env:PATH = 'C:\Users\wante\.jdks\graalvm-ce-23.0.1\bin' + $env:PATH
$env:GRAALVM_HOME = 'C:\Users\wante\.jdks\graalvm-ce-23.0.1'

../mvnw clean package -Pnative -DskipTests=true -DspringJavaFormatSkip=true -T 1 -pl :unleash-advanced -am
./unleash-advanced/target/unleash-advanced.exe -Dnative.encoding=UTF-8
