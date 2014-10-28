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
        #��ȡ�������
        data = web.input()
        signature=data.signature
        timestamp=data.timestamp
        nonce=data.nonce
        echostr=data.echostr
        #�Լ���token
        token="dsoul0621" #�����д����΢�Ź���ƽ̨�������token
        #�ֵ�������
        list=[token,timestamp,nonce]
        list.sort()
        sha1=hashlib.sha1()
        map(sha1.update,list)
        hashcode=sha1.hexdigest()
        #sha1�����㷨        
 
        #���������΢�ŵ�������ظ�echostr
        if hashcode == signature:
            return echostr
        
    def POST(self):        
        str_xml = web.data() #���post��������
        xml = etree.fromstring(str_xml)#����XML����
        msgType=xml.find("MsgType").text
        fromUser=xml.find("FromUserName").text
        toUser=xml.find("ToUserName").text
        if msgType == "event":
            mscontent = xml.find("Event").text
            if mscontent == "subscribe":
                reply = u'''���ã���ӭ��ע�������ţ�\n����-a[�ո�][�ؼ���]�������ؼ��֣�����-d[�ո�][�ؼ���]��ɾ��һ���Ѿ���ӵĹؼ��֣�����-s���鿴��ǰ���ĵĹؼ��֣� ����-g xxxx/xx/xx���鿴��xxxx��xx��xx�յ�������ţ�����news���鿴�����������ţ��������ʲô����������� -f[�ո�]������Ϣ ���ύ�����'''
                return self.render.reply_text(fromUser,toUser,int(time.time()), reply)
            if mscontent == "unsubscribe":
                reply = u'''��֪����ǰ�Ĺ��ܼܺ򵥣������һ������Ľ�����ӭ�Ժ�������'''
                return self.render.reply_text(fromUser,toUser,int(time.time()), reply)
        elif msgType == "text":
            content=xml.find("Content").text#����û������������
            if content == "-h":
                reply = u'''����-a[�ո�][�ؼ���]�������ؼ��֣�����-d[�ո�][�ؼ���]��ɾ��һ���Ѿ���ӵĹؼ��֣�����-s���鿴��ǰ���ĵĹؼ��֣� ����-g xxxx/xx/xx���鿴��xxxx��xx��xx�յ�������ţ�����news���鿴�����������ţ��������ʲô����������� -f[�ո�]������Ϣ ���ύ�����ʾ����-a ������'''
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
                    return self.render.reply_text(fromUser,toUser,int(time.time()), "���쵽ĿǰΪֹ��û���������Ĺؼ����йص����ţ��������ע��") 
                return self.render.reply_text(fromUser,toUser,int(time.time()), ret)
            else:
                if content.find("-a") == 0:
                    tmp = content[3: len(content)]
                    cnt = 0
                    #return self.render.reply_text(fromUser,toUser,int(time.time()), "tmp")
                    for user in db.get_all('User'):
                        #return self.render.reply_text(fromUser,toUser,int(time.time()), "already exists!")
                        if (user.keyword == tmp) and (user.openID == fromUser):
                            return self.render.reply_text(fromUser,toUser,int(time.time()), "���Ѿ���ע�˴˹ؼ�����ѽ��")
                        if (user.openID == fromUser):
                            cnt += 1
                    if cnt >= 10:
                        return self.render.reply_text(fromUser,toUser,int(time.time()), "�ؼ��ʵ�����Ϊ10������ʹ�� -d[�ո�]�ؼ��� ��ɾ��һЩ�ؼ��ְ�~")
                    db.get_insert('User', fromUser, tmp)
                    return self.render.reply_text(fromUser,toUser,int(time.time()), "����ɹ���")
                elif content.find("-d") == 0:
                    #return self.render.reply_text(fromUser,toUser,int(time.time()), "123")
                    tmp = content[3: len(content)]
                    flag = 0
                    for user in db.get_all('User'):
                        if (user.keyword == tmp) and (user.openID == fromUser):
                            db.get_delete('User', "id=" + str(user.id))
                            flag = 1
                    
                    if flag == 1:
                        return self.render.reply_text(fromUser,toUser,int(time.time()), "ɾ���ɹ���")
                    else:
                        return self.render.reply_text(fromUser,toUser,int(time.time()), "����û�ж��Ĵ˹ؼ��֡�")
                elif content.find("-f") == 0:
                    tmp = content[3: len(content)]
                    fktime = time.strftime('%Y/%m/%d %H:%M',time.localtime())
                    db.get_insert_feedback('feedback', fromUser, fktime, tmp)
                    return self.render.reply_text(fromUser,toUser,int(time.time()), "���ķ�����Ϣ�Ѿ��ύ����л����֧�֣�")
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
                        return self.render.reply_text(fromUser,toUser,int(time.time()), "����û�������ؼ�����ص����ţ�������鿴���������ʽ�Ƿ���ȷ������-g 2014/10/26��ע�⵱ǰֻ�ܲ�ѯ��10��28���Ժ��һЩ���š�") 
                    return self.render.reply_text(fromUser,toUser,int(time.time()), ret)
                else:
                    return self.render.reply_text(fromUser,toUser,int(time.time()), "û����������������� -h ���鿴������")

                    
            return self.render.reply_text(fromUser,toUser,int(time.time()), ret)
