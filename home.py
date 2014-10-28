# -*- coding: utf-8 -*-
import re
import os
import sae
import web
import sys
import time
import urllib
import urllib2
import dbConnection as db

class home:
    def __init__(self):
        self.app_root = os.path.dirname(__file__)
        self.templates_root = os.path.join(self.app_root, 'templates')
        self.render = web.template.render(self.templates_root)
    def GET(self):
        #db.get_insert_news('News', "12312313123", "郑博郑博郑博郑博郑博郑博郑博郑博郑博郑博郑博")
        #return render.hello("你好")
        m = re.findall(r"""'/news/ + # date
                    \w * # 2014
                    / #/
                    \w\w #month
                    - #-
                    \w* #day
                    / #/
                    \w * #words
                    .htm'
                    \s # white space
                    title='
                    \S *
                    '
                    \s""", urllib2.urlopen("http://today.hit.edu.cn").read(), re.M|re.X)

        #return render.hello(len(m))
        T = time.strftime('%Y/%m-%d', time.localtime(time.time()))
        #T = "2014/10-26"
	    
        for x in db.get_all('News'):
            #return render.hello(len(m))
            db.get_delete('News', "id=" + str(x.id))
        URL = []
        Title = []
        #News = []
        for i in m:
                #return render.hello(len(i))
                y = i.decode('gbk').encode('utf8')
            	x = y.split("title=")
                url = "http://today.hit.edu.cn"
                url += x[0][1:-2]
                title = x[1][1:-2]
                date = i[7:17]
                if(cmp(date, T) != 0):
                        continue
                
                cnt = URL.count(url)
                if(cnt != 0):
                        continue
                URL.append(url)
                #print  url
                
                #if title.find("电气"):
                #    return render.hello("123")
                db.get_insert_news('News', str(url), str(title))

                #return render.hello(str(title))
                #db.get_insert_news('News',"123", "456")
                #News.append(url + " " + news)
        
        #return render.hello(len(m))
