# coding: UTF-8
import os
import sys
app_root = os.path.dirname(__file__)
sys.path.insert(0, os.path.join(app_root, 'requests-2.4.3'))
sys.path.insert(0, os.path.join(app_root, 'jpush-3.0.1'))
import re
import sae
import web
import time
import urllib
import urllib2,json
import lxml
import hashlib
import jpush as jpush



app_key = u'07003d1118ec025b67947212'
master_secret = u'c0fe818fc0c7cae69a53eabf'

def push_news(push_id, news):
    
    _jpush = jpush.JPush(app_key, master_secret)
    
    push = _jpush.create_push()
    push.audience = jpush.audience(
            jpush.registration_id(push_id)
        )
    push.notification = jpush.notification(alert=news)
    push.platform = jpush.all_
    push.send()
