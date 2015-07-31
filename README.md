# push-msg-web

这个项目是为了解决项目web应用的实时消息推送建立

使用了 cometd spring kafka redis

* cometd 用来推送
* spring 用来集成
* kafka 用来接受业务系统需要推送的消息
* redis 用来绑定cometd用户和业务用户的关系 用来推送给特定的人
* 利用cometd的channel 来实现批量推送

###### 程序逻辑

利用kafka接受需要推送的消息，通过cometd来推送出去，cometd推送支持long poling 和 websocket方式

######注意

本工程只测试 long poling方式

项目用的一些jar无法开源出来，你可以把redis和kafka换成自己的实现


