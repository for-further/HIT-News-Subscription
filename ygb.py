# -*- coding: utf-8 -*-
import re
import time
import urllib
import urllib2

def ygb():
    m = re.findall(r"""/news/show
                    \S*
                    \s\s*
                    \S*\s*\S*\s*\S*\s*\S*"
                    >""", urllib.urlopen("http://ygb.hit.edu.cn").read(), re.M|re.X)

    mm = re.findall(r"""
                \(\s
                \S *
                \s*\)""", urllib.urlopen("http://ygb.hit.edu.cn").read(), re.M|re.X)

    
    #T = time.strftime('%Y/%m/%d', time.localtime(time.time()))
    T = "2014/10/24"


    URL = []
    j = 0
    for i in m:
        date = mm[j][2:-2]
        j += 1
        date = date.replace('-', '/')
        print date
        if(cmp(date, T) != 0):
            continue
        url = "http://ygb.hit.edu.cn"
        l = i.split(" title=")
        url += l[0][0:-1]
        print url
        if(URL.count(url) != 0):
            continue
        URL.append(url)

        news = l[1][1:-2]
        print news
       

ygb()
