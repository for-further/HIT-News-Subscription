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
from weixinInterface import WeixinInterface
import dbConnection as db
from crawler import *
urls = (
    '/ck', 'feedback', 
    '/', 'Hello',
    '/weixin', 'WeixinInterface',
    '/today', 'today_crawler',
    '/acm', 'acm_crawler',
    '/clear', 'db_clear',
    '/news', 'news_crawler',
    '/studyathit', 'studyathit_crawler',
    '/yzb', 'yzb_crawler',
    '/zsb', 'zsb_crawler',
    '/hqjt', 'hqjt_crawler',
    '/ygb', 'ygb_crawler',
    '/usp', 'usp_crawler',
    '/jwc', 'jwc_crawler',
    '/hituc', 'hituc_crawler'
)
 
app_root = os.path.dirname(__file__)
templates_root = os.path.join(app_root, 'templates')
render = web.template.render(templates_root)

class feedback:
    def GET(self): 
        fkcon = db.get_all('feedback')
        return render.checkfk(fkcon) 

class Hello:
    def GET(self):
        return render.hello("ÄãºÃ")
class today_crawler:
    def GET(self):
        today()
class acm_crawler:
    def GET(self):
        acm()
class news_crawler:
    def GET(self):
        news()
class studyathit_crawler:
    def GET(self):
        studyathit()        
class yzb_crawler:
    def GET(self):
        yzb()
class zsb_crawler:
    def GET(self):
        zsb()
class hqjt_crawler:
    def GET(self):
        hqjt()
class ygb_crawler:
    def GET(self):
        ygb()
class usp_crawler:
    def GET(self):
        usp()
class jwc_crawler:
    def GET(self):
        jwc()
class hituc_crawler:
    def GET(self):
        hituc()
class db_clear:
    def GET(self):
        clear()

app = web.application(urls, globals()).wsgifunc() 
application = sae.create_wsgi_app(app)