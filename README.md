# selenium-server-video
selenium server contains a servlet with ffmpeg video-recording

selenium server 3.5.2
ffpmeg params: recorder x11grab, 12fps, screensize according to wizdow size, display according to env variable "DISPLAY"

# Build a remote module:
```
./gradlew jar
```
Run hub:
```
java -jar videonode-1.0.jar -role hub
```
Run node:
```
java -jar videonode-1.0.jar -servlets "com.portaone.videonode.core.VideoServlet" -role node -port 5555 -hub "http://localhost:4444/grid/register"
```

# Servlet commands:
start recording
```
/start?name=myVideoFileName
```
stop recording
```
/stop?result=true&name=myVideoFileName
```
parameters:
name - video file name
result - result of the test (if true, then remove the video. Video is supposed to be needed only for failed tests)
