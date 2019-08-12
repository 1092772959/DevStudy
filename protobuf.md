## Springboot中使用Protobuf

https://github.com/protocolbuffers/protobuf/releases/tag/v3.6.1

下载exe文件，配置环境变量

编写配置文件

```protobuf
syntax = "proto2";

package proto;

option java_package = "com.vue.demo.proto.model";
option java_outer_classname = "UserModel";

message User{
    required int32 id = 1;
    required string name = 2;
    required string email = 3;
}
```

命令行编译生成

```bash
protoc --java_out=./ Persion.proto  
```

之后可以使用生成的实体类



#### MAVEN整合protobuf

IDEA也可以整合protobuf

pom.xml，注意protobuf版本要和自己下的exe一致

```xml
<properties>
        <java.version>1.8</java.version>
        <grpc.version>1.6.1</grpc.version>
        <protobuf.version>3.5.1</protobuf.version>
 </properties>

<dependencies>

        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-netty</artifactId>
            <version>${grpc.version}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-protobuf</artifactId>
            <version>${grpc.version}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>io.grpc</groupId>
            <artifactId>grpc-stub</artifactId>
            <version>${grpc.version}</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>com.google.protobuf</groupId>
            <artifactId>protobuf-java</artifactId>
            <version>${protobuf.version}</version>
        </dependency>
    </dependencies>

    <build>
        <extensions>
            <extension>
                <groupId>kr.motd.maven</groupId>
                <artifactId>os-maven-plugin</artifactId>
                <version>1.5.0.Final</version>
            </extension>
        </extensions>

        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>

            <plugin>
                <groupId>org.xolstice.maven.plugins</groupId>
                <artifactId>protobuf-maven-plugin</artifactId>
                <version>0.5.0</version>
                <configuration>
                    <protocArtifact>com.google.protobuf:protoc:${protobuf.version}:exe:${os.detected.classifier}</protocArtifact>
                    <pluginId>grpc-java</pluginId>
                    <pluginArtifact>io.grpc:protoc-gen-grpc-java:${grpc.version}:exe:${os.detected.classifier}</pluginArtifact>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>compile</goal>
                            <goal>compile-custom</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
```

proto配置文件默认放在src/main/proto下



#### Vue中使用protobuf

使用和后端同样的配置文件，并使用前端的编译语言

```protobuf
protoc.exe --js_out=import_style=user,binary:. user.proto
```

将生成的js文件放至src/proto/目录下

测试函数：

```javascript
import userModel from '../proto/user_pb'



methods:{
        testProto: function(){
            var user = new userModel.User();
            user.setId(1);
            user.setName("MR. ACR");
            user.setEmail("123123123");
            
            var btyes = user.serializeBinary();		//序列化
            console.log(btyes);
            
            var user2 = userModel.User.deserializeBinary(btyes);		//反序列化

            console.log(user2);
            this.message = user2.getName();
        }
```





