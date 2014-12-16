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
from time_push import *
from crawler import *
from push import *
from test import testclass
import dbConnection as db


urls = (   
    '/ck', 'feedback', 
    '/push', 'pushclass',
    '/test', 'testclass',
    '/crawler', 'web_crawler',
	'/', 'Hello'
)

app_root = os.path.dirname(__file__)
templates_root = os.path.join(app_root, 'templates')
render = web.template.render(templates_root)
                             
class Hello:
    def GET(self):
        #res = urllib2.urlopen('http://www.sogou.com/web?q=%e8%ae%a1%e7%ae%97%e6%9c%ba%e5%ad%a6%e9%99%a2&query=%22%e8%ae%a1%e7%ae%97%e6%9c%ba%e5%ad%a6%e9%99%a2%22++site%3Atoday.hit.edu.cn&fieldtitle=&fieldcontent=&fieldstripurl=&bstype=&ie=utf8&include=checkbox&sitequery=today.hit.edu.cn&located=0&tro=on&filetype=&num=10')
        return render.hello("123")
    
class web_crawler:
    def GET(self):
        return render.hello(init())	

class pushclass:
    def GET(self):
        return render.hello(Gao())
 
class feedback:
    def GET(self): 
        fkcon = db.get_all('feedback')
        return render.checkfk(fkcon) 
        
app = web.application(urls, globals()).wsgifunc() 
application = sae.create_wsgi_app(app)