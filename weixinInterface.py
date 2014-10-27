# -*- coding: utf-8 -*-
import hashlib
import web
import lxml
import time
import os
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
                reply = u'''输入  -a[空格]关键字  来新增关键字，输入  -d[空格]关键字  来删除一条已经添加的关键字， 输入 -s 来查看当前订阅的关键字， 输入 news 来查看当天的相关新闻。 '''
                return self.render.reply_text(fromUser,toUser,int(time.time()), reply)
            if mscontent == "unsubscribe":
                reply = u'''我知道当前的功能很简单，但是我会慢慢改进，欢迎以后再来！'''
                return self.render.reply_text(fromUser,toUser,int(time.time()), reply)
        elif msgType == "text":
            #return self.render.reply_text(fromUser,toUser,int(time.time()), "success")
            content=xml.find("Content").text#获得用户所输入的内容
            if content == "-h":
                reply = u'''输入  -a[空格]关键字  来新增关键字，输入  -d[空格]关键字  来删除一条已经添加的关键字， 输入 -s 来查看当前订阅的关键字， 输入 news 来查看当天的相关新闻。 '''
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
                #return self.render.reply_text(fromUser,toUser,int(time.time()), "123456")
                
                ret = ""
                cnt = 1
                for x in db.get_all('News'):
                    #cnt = cnt + 1
                    for key in keyword:
                        if x.title.find(key) != -1:
                            #cnt = cnt + 1
                            ret += x.title + "\n" + x.url + "\n"
                            break
                if ret == "":
                    return self.render.reply_text(fromUser,toUser,int(time.time()), "No news yet!") 
                return self.render.reply_text(fromUser,toUser,int(time.time()), ret)
            else:
                if content.find("-a") == 0:
                    tmp = content[3: len(content)]
                    #return self.render.reply_text(fromUser,toUser,int(time.time()), "tmp")
                    for user in db.get_all('User'):
                        #return self.render.reply_text(fromUser,toUser,int(time.time()), "already exists!")
                        if (user.keyword == tmp) and (user.openID == fromUser):
                            return self.render.reply_text(fromUser,toUser,int(time.time()), "already exists!")
                    
                    db.get_insert('User', fromUser, tmp)
                    return self.render.reply_text(fromUser,toUser,int(time.time()), "success.")
                elif content.find("-d") == 0:
                    #return self.render.reply_text(fromUser,toUser,int(time.time()), "123")
                    tmp = content[3: len(content)]
                    flag = 0
                    for user in db.get_all('User'):
                        if (user.keyword == tmp) and (user.openID == fromUser):
                            db.get_delete('User', "id=" + str(user.id))
                            flag = 1
                    
                    if flag == 1:
                        return self.render.reply_text(fromUser,toUser,int(time.time()), "success.")
                    else:
                        return self.render.reply_text(fromUser,toUser,int(time.time()), "no such keyword!")
                else:
                    return self.render.reply_text(fromUser,toUser,int(time.time()), "no such method!")

                    
            return self.render.reply_text(fromUser,toUser,int(time.time()), ret)
