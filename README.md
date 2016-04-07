## A、B站数据分析

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
Vedio：视频信息<br>
UP：投稿人信息<br>

Request：<br>
HttpService：处理http请求，返回结果保存到临时文件中等待后续分析，因java字符串长度限制，不能直接放到字符串对象中<br>
Response：<br>
Template：解析模板，定义统一的解析方法。<br>
URLTemplate：解析文件中的url<br>
VedioTemplate：解析视频的模板<br>
UPTemplate：解析投稿人<br>

DataService：保存和更新数据<br>

Main：<br>
