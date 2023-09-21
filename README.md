# yiwise-asr-sdk-java
该工程提供了一知ASR的调用SDK和demo，仅支持PCM且采样率8k的音频，配置文件在application.properties中，配置项为asr.gateway.address。
### 应用部署
1. 代码编译：mvn clean package -Dmaven.test.skip=true -P test，得到打包的文件：yiwise-asr-demo.tar.gz
2. 将打包文件拷贝到服务器上，解压运行：

启动命令：```bin/start.sh start```

停止命令：```bin/start.sh stop```

3. 测试：

使用默认音频文件测试： ```curl "http://localhost:7788/api/asr"```

使用自定义音频文件测试：```curl "http://localhost:7788/api/asr?filePath=/tmp/test.wav"```

4. Docker镜像打包：

```mvn clean package -Dmaven.test.skip=true -P localization```

```docker build --force-rm=true --tag=docker.yiwise.net/yiwise-asr/asr-demo:local .```

```docker-compose -f service-asr-demo.yaml up -d```