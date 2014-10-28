# -*- coding: utf-8 -*-
import re
import time
import urllib
import urllib2

def usp():
    m = re.findall(r"""showXinWen_XX.asp\?MC=\S*
                    \s*\S*\s*title\s*=\s*\S*
                    \s*\S*\s*\S*\s*\S*\s*\S*\s*\S*\s*\S*\s*\S*\s*\S*\s*\S*\s*\S*\s*\S*\s*\S*\s*\S*\s*\S*\s*\S*
                    </font>""", urllib2.urlopen("http://www.usp.com.cn").read(), re.M|re.X)


    #T = time.strftime('%Y/%m/%d', time.localtime(time.time()))
    T = "2014/10/16"

    URL = []
    for i in m:
        date = i[-17:-7]
        date = date.replace('-', '/')
        print date
        if(cmp(date, T) != 0):
            continue
        url = "http://www.usp.com.cn/"
        j = i.split("' target='_blank'  title = '")
        url += j[0]
        if(URL.count(url) != 0):
            continue
        URL.append(url)
        print url
        
        k = j[1].split("><font color=")
        news = k[0][0:-2]
        print news
        
usp()
