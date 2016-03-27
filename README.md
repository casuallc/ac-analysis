## A站数据分析

### 期望得到的结果
####获得视频信息
* URL地址
* 分类
* 投稿人
* 简介
* 发布时间
* 观看数
* 弹幕数
* 收藏数
* 评论数
* 其他

#### UP主信息
* 主页地址
* uid
* 昵称
* 性别
* 性取向
* 所在地
* 注册时间
* QQ等联系方式
* 投稿数
* 收听数
* 听众数

### 整体设计
### 数据表
URL：<br>
URL_REQUESTED：<br>
VEDIO：<br>
UP：<br>


### 程序设计
首先获取一个URL，如果没有请求过，则发送请求，依次解析出包含的URL（保存到URL表）、视频信息、UP主信息等，解析成功后把该URL保存到URL_REQUESTED表中。

### 类设计
URL：<br>
Vedio：<br>
UP：<br>

Request：<br>
Response：<br>
Template：<br>
TagTemplate：<br>
VedioTemplate：<br>
UPTemplate：<br>

DataService：<br>

Main：<br>