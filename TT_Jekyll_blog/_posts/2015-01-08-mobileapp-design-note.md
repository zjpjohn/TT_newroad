---
layout: post
category : datascience
tags : [Internet,Develop]
title: Mobile App Design Note
---
{% include JB/setup %}

### Product Design Architecture

1.数据结构设计

> 大数据环境下的非结构化数据存储   
> 设计原则:面向非结构用户数据，支持大数据分析  
> MetaData源数据与大数据信息分离  
> 数据库选择MongoDB / Hadoop   
> 数据库访问性能压力/ Index 设置  
> 事务管理需求与变通   
> 提供用户级别的逻辑锁   
> NoSQL Research: MongoDB   

2.通用缓存架构设计

> 持久化缓存选择Couchbase，支持Saclablity和FailOver     
> NoSQL Research: Couchbase    

3.文件缓存/存储架构设计

> 设计统一文件资源访问缓存入口，透明化后台云存储    
> 文件消息队列算法 （基于AMQP协议）RabbitMQ/ZeroMQ (基于分布式环境)    
> 开发通用云存储访问API，可有效切换不同云存储产品    
> 云存储API性能考量，支持多线程，断点续传，秒传算法     
> 内容与MetaData实现数据分离，使用Hadoop存储内容信息，并支持文件分块    
> 云存储去重算法及资源版本差异信息    

4.同步策略设计 

> 服务器端同步策略    
> 处理同步冲突策略    
> 客户端同步策略    

> 分版本下发timestamp设计    
> 时间戳/客户端版本号/终端版本号     
> 保存数据格式所能支持的最低格式     
> 默认分类如何实现，以支持登录后同步未登录前的分类信息     

5.多版本校验回溯设计

> 版本回溯算法     
> 保存版本差异信息，基于文件分块的实现    
> 如何有效数据结构以支持版本回溯     
 
6.云端数据检索架构设计

> 查询性能，全文检索，动态索引，分布式部署     
> 开源解决方案ElasticSearch/Lucene     
> 设计全文搜索Cache,加快查询速度     


7.云产品性能瓶颈

> 客户端代码性能     
> 客户端/服务器端数据同步格式复杂与否/http交互性能     
> 同步数据与服务器端数据结构数据转换性能，包括JSON parser性能/数据转换代码性能     
> 服务器端业务逻辑性能     
> 数据库数据结构是否符合业务需求，以简化Service层逻辑     
> 如何设计有效地数据库Index,以提高查询性能     
> 设计面向切面的通用缓存,来提高性能      
> 以空间换时间     

8.用户数据分析考量

> 如何有效收集/统计用户数据     
> 收集用户信息需要哪些数据     
> 选择第三方解决方案 友盟/腾讯大数据      
> 如何使用Mongo aggregate 方法分析非结构数据      

9.云产品设计原则

> 云端/终端使用同样唯一的ID标识      
> 客户端数据与服务器端数据同步及数据转换策略     
> SQLLite 与 MongoDB / 结构化数据与非结构化数据的转换     
> CouchbaseLite 支持客户端数据与服务器端同结构自动同步。设计考量      
> 统一资源云端存储接口设计。如透明化云存储，统一访问文件缓存层。      
> 如何解决多终端并发数据交互,timing策略       