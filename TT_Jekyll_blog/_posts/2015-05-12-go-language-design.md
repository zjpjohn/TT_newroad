---
layout: post
category : datascience
tags : [Internet,Develop]
title: Go Language Design Note
---
{% include JB/setup %}

Go语言编程
1.Compile go files to .a file according to package name
2.协程与channel, goruntine
3.reflect 反射
4.支持多个返回值
5.pointer /array/slice/map/chan/struct/interface
6.go run: cannot run non-main package
7.:=用于明确表达同时进行变量声明和初始化的工作
var v1 int = 10
var v2 = 10
8.多重赋值功能 
i, j = j, i
9.Go有两种创建数据结构的方法：new和make
new返回指针 / make返回初始化后的(非零)值
new返回一个指向初始化为全0值的 指针，而make返回一个复杂的结构。
基础的区别在于，new(T)返回一个*T类型，一个可以被隐性反向引用的指针（如图中的黑色指 针），而make(T,args)返回一个原始的T，它并不是一个指针。T中常有写隐性的指针（如图中的灰色指针）
10.在Go中字符串是不可变的
11.可变字符串需赋值 s := "hello"
12.array、slice、map
13.Go内置有一个error类型
err := errors.New("emit macho dwarf: elf header corrupted")
if err != nil {
    fmt.Print(err)
}




