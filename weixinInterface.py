# -*- coding: utf-8 -*-
import sae
import re
import hashlib
import web
import lxml
import time
import sys
import os
import urllib
import urllib2,json
import dbConnection as db
from lxml import etree
class WeixinInterface:
    
    def __init__(self):
        self.app_root = os.path.dirname(__file__)
        self.templates_root = os.path.join(self.app_root, 'templates')
        self.render = web.template.render(self.templates_root)
 
    def GET(self):
        #获取输入参数
        data = web.input()
        signature=data.signature
        timestamp=data.timestamp
        nonce=data.nonce
        echostr=data.echostr
        #自己的token
        token="dsoul0621" #这里改写你在微信公众平台里输入的token
        #字典序排序
        list=[token,timestamp,nonce]
        list.sort()
        sha1=hashlib.sha1()
        map(sha1.update,list)
        hashcode=sha1.hexdigest()
        #sha1加密算法        
 
        #如果是来自微信的请求，则回复echostr
        if hashcode == signature:
            return echostr
        
    def POST(self):        
        str_xml = web.data() #获得post来的数据
        xml = etree.fromstring(str_xml)#进行XML解析
        msgType=xml.find("MsgType").text
        fromUser=xml.find("FromUserName").text
        toUser=xml.find("ToUserName").text
        if msgType == "event":
            mscontent = xml.find("Event").text
            if mscontent == "subscribe":
                reply = u'''您好，欢迎关注工大新闻！\n输入-a[空格][关键字]来新增关键字，输入-d[空格][关键字]来删除一条已经添加的关键字，输入-s来查看当前订阅的关键字， 输入-g xxxx/xx/xx来查看在xxxx年xx天xx日的相关新闻，输入news来查看当天的相关新闻，如果您有什么意见，请输入 -f[空格]反馈信息 来提交意见。'''
                return self.render.reply_text(fromUser,toUser,int(time.time()), reply)
            if mscontent == "unsubscribe":
                reply = u'''我知道当前的功能很简单，但是我会慢慢改进，欢迎以后再来！'''
                return self.render.reply_text(fromUser,toUser,int(time.time()), reply)
        elif msgType == "text":
            content=xml.find("Content").text#获得用户所输入的内容
            if content == "-h":
                reply = u'''输入-a[空格][关键字]来新增关键字，输入-d[空格][关键字]来删除一条已经添加的关键字，输入-s来查看当前订阅的关键字， 输入-g xxxx/xx/xx来查看在xxxx年xx天xx日的相关新闻，输入news来查看当天的相关新闻，如果您有什么意见，请输入 -f[空格]反馈信息 来提交意见。示例：-a 哈工大。'''
                return self.render.reply_text(fromUser,toUser,int(time.time()), reply)
            elif content == "-s":
                keyword = []
            	for user in db.get_all('User'):
                    if user.openID == fromUser:
                        keyword.append(user.keyword)
                keyword = db.unique(keyword)
                #return self.render.reply_text(fromUser,toUser,int(time.time()), "123")
                ret = ""
                for key in keyword:
                    ret += key + " "
                return self.render.reply_text(fromUser,toUser,int(time.time()), ret)
            elif content == "news":
                #ret = "no news yet!"
                keyword = []
            	for user in db.get_all('User'):
                    if user.openID == fromUser:
                        keyword.append(user.keyword)
                keyword = db.unique(keyword)
                #return self.render.reply_text(fromUser,toUser,int(time.time()), "123")
                
                #return self.render.reply_text(fromUser,toUser,int(time.time()), '#')
                
                ret = ""
                T = time.strftime('%Y/%m/%d', time.localtime(time.time()))
                
                for x in db.get_all('News'):
                    if(cmp(x.date, T) != 0):
                        continue
                    for key in keyword:
                        if x.title.find(key) != -1:
                            ret += x.title + "\n" + x.url + "\n"
                            break
                if ret == "":
                    return self.render.reply_text(fromUser,toUser,int(time.time()), "今天到目前为止还没有与您订阅关键字有关的新闻，请持续关注。") 
                return self.render.reply_text(fromUser,toUser,int(time.time()), ret)
            else:
                if content.find("-a") == 0:
                    tmp = content[3: len(content)]
                    cnt = 0
                    #return self.render.reply_text(fromUser,toUser,int(time.time()), "tmp")
                    for user in db.get_all('User'):
                        #return self.render.reply_text(fromUser,toUser,int(time.time()), "already exists!")
                        if (user.keyword == tmp) and (user.openID == fromUser):
                            return self.render.reply_text(fromUser,toUser,int(time.time()), "您已经关注了此关键字了呀！")
                        if (user.openID == fromUser):
                            cnt += 1
                    if cnt >= 10:
                        return self.render.reply_text(fromUser,toUser,int(time.time()), "关键词的上限为10个，请使用 -d[空格]关键字 来删除一些关键字吧~")
                    db.get_insert('User', fromUser, tmp)
                    return self.render.reply_text(fromUser,toUser,int(time.time()), "插入成功。")
                elif content.find("-d") == 0:
                    #return self.render.reply_text(fromUser,toUser,int(time.time()), "123")
                    tmp = content[3: len(content)]
                    flag = 0
                    for user in db.get_all('User'):
                        if (user.keyword == tmp) and (user.openID == fromUser):
                            db.get_delete('User', "id=" + str(user.id))
                            flag = 1
                    
                    if flag == 1:
                        return self.render.reply_text(fromUser,toUser,int(time.time()), "删除成功。")
                    else:
                        return self.render.reply_text(fromUser,toUser,int(time.time()), "您并没有订阅此关键字。")
                elif content.find("-f") == 0:
                    tmp = content[3: len(content)]
                    fktime = time.strftime('%Y/%m/%d %H:%M',time.localtime())
                    db.get_insert_feedback('feedback', fromUser, fktime, tmp)
                    return self.render.reply_text(fromUser,toUser,int(time.time()), "您的反馈信息已经提交，感谢您的支持！")
                elif content.find("-g") == 0:
                    #return self.render.reply_text(fromUser,toUser,int(time.time()), "123")
                    keyword = []
            	    for user in db.get_all('User'):
                        if user.openID == fromUser:
                            keyword.append(user.keyword)
                    keyword = db.unique(keyword)
                    T = content[3: len(content)]
                    ret = ""
                    for x in db.get_all('News'):
                        if(cmp(x.date, T) != 0):
                            continue
                        #return self.render.reply_text(fromUser,toUser,int(time.time()), T)    
                        for key in keyword:
                            if x.title.find(key) != -1:
                                ret += x.title + "\n" + x.url + "\n"
                                break
                    if ret == "":
                        return self.render.reply_text(fromUser,toUser,int(time.time()), "当天没有与您关键字相关的新闻，或者请查看命令输入格式是否正确，例如-g 2014/10/26。注意当前只能查询到10月28日以后的一些新闻。") 
                    return self.render.reply_text(fromUser,toUser,int(time.time()), ret)
                else:
                    return self.render.reply_text(fromUser,toUser,int(time.time()), "没有这样的命令！请输入 -h 来查看帮助。")

                    
            return self.render.reply_text(fromUser,toUser,int(time.time()), ret)
