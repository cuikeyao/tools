# 项目名称

Tools

## 项目概要

该项目旨在提供以下两个子项目的功能：

### agent项目

agent项目的主要功能是监控服务器上的进程是否正常运行，并将结果报告给[Uptime Kuma](http://tools.cuikeyao.com:3001/)。Agent项目可以定期检查服务器上的进程状态，并将结果发送给Uptime Kuma进行处理。

### video-website项目

video-website项目是一个提供视频网站服务的子项目。它的主要功能是在cpolar内网穿透地址更改后，自动修改[视频网站项目](https://github.com/cuikeyao/video)GitHub Page重定向地址。这样，当cpolar内网穿透地址发生变化时，用户仍然可以访问到正确的视频网站。

## 安装和运行

以下是安装和运行该项目的步骤：

1. 克隆项目到本地机器： `git clone https://github.com/cuikeyao/tools.git`
2. 进入项目目录： `cd tools`
3. 编译和打包项目： `mvn clean package`
4. 运行agent项目：在agent项目目录下执行  `java -jar /home/cuikeyao/agent.jar --jasypt.encryptor.password=123456`
5. 运行video-website项目：在video-website项目目录下执行  `java -jar /home/cuikeyao/video-website.jar --jasypt.encryptor.password=123456`

请确保在运行项目之前，已经安装了Java运行环境和相应的依赖项。

## 贡献

如果你对该项目有任何改进或建议，欢迎提交pull request或提出issue。欢迎并感谢你的贡献！

## 许可证

该项目基于[MIT许可证](https://opensource.org/licenses/MIT)进行发布和分发。详细信息请参阅许可证文件。

## 联系方式

如果你有任何问题或疑问，可以通过以下方式联系我：

- 电子邮件：[991486302@qq.com](mailto:991486302@qq.com)
- 项目主页：[https://github.com/cuikeyao/tools](https://github.com/cuikeyao/tools)