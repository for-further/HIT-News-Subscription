# -*- coding: utf-8 -*-
import re
import sys
import time
import urllib
import urllib2

def today():
    m = re.findall(r"""<title><\S*\s*\S*\s*\S*
                    </title>""", urllib2.urlopen("http://today.hit.edu.cn/rss.xml").read(), re.M|re.X)

    link = re.findall(r"""<link><\S*\s*\S*\s*\S*
                    </link>""", urllib2.urlopen("http://today.hit.edu.cn/rss.xml").read(), re.M|re.X)

        
    T = time.strftime('%Y/%m-%d', time.localtime(time.time()))
    #T = "2014/10-26"

    URL = []
    News = []
    j = 0
    for i in link:
        l = i.split("[CDATA[")
        date = l[1][29:39]
        #print date
        if(cmp(date, T) != 0):
            continue
        r = l[1].split("]]>")
        url = r[0]
        if(URL.count(url) != 0):
            continue
        URL.append(url)
        print url

        news = m[j][16:-11]
        j += 1
        print news

        News.append(url + "$" + news)

    return News
                

    
today()
