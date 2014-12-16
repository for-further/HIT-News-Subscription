# coding: UTF-8
import re
import os
import sae
import web
import sys
import time
import urllib
import urllib2,json
import lxml
import hashlib
import string
import dbConnection as db
from push import *


def Gao():
    #push_news('090a4d7b383', '【班助一】爱满空巢——化工学院1314101团支部探望空巢老人' + '$' + 'http://today.hit.edu.cn/news/2014/12-02/6272013221RL0.htm')
    #return
    minute = time.strftime('%Y/%m/%d/%H/%M/%S', time.localtime(time.time()))
    j = minute.split('/')
    now = int(j[0]) * 365 * 24 * 60 + int(j[1]) * 30 * 24 * 60 + int(j[2]) * 24 * 60 + int(j[3]) * 60 + int(j[4])
    res = ""
    user = []
    news = []
    for x in db.get_all('news'):
        news.append(x)
    
    for x in db.get_all('keyword'):
        if x.push_id == "null" or x.push_id == "":
            continue

        if user.count(x.push_id) == 0:
            p = 0
            push_time = 0
            last_id = 0
            for y in db.get_all('push_time'):
                if y.push_id == x.push_id:
                    p = y.id
                    push_time = y.time
                    last_id = y.last_id
                    break
            
            us = ""
            tit = ""
            ur = ""
            flag = 0
            for y in news[::-1]:
                if y.word == x.word or y.title.find(x.word) != -1:
                    if now - push_time >= 60 and y.id > last_id:
                        user.append(x.push_id)
                        us = x.push_id
                        tit = y.title + '$' + y.url
                        ur = y.url
                        last_id = y.id
                        flag = 1
                        break;
            if flag == 0:
                continue
            
            #if x.push_id == "000f08ac5e9":
            #    return "123"
            
            #if x.push_id == '070dec8af07':
            #    return "456"
            
            for y in db.get_all('URL'):
                if y.push_id == us and ur.find(y.url) != -1:
                    flag = 0
                    break
            if flag == 0:
                continue
            if push_time > 0:
                db.delete_push_time("id = " + str(p))
            db.insert_push_time(us, now, last_id)
            push_news(us, tit)
    