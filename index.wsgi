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
from Home import *
urls = (
    '/', 'Hello',
    '/weixin', 'WeixinInterface',
    '/Home', 'home'
)
 
app_root = os.path.dirname(__file__)
templates_root = os.path.join(app_root, 'templates')
render = web.template.render(templates_root)
 
class Hello:
    def GET(self):
        return render.hello("ÄãºÃ")

app = web.application(urls, globals()).wsgifunc() 
application = sae.create_wsgi_app(app)