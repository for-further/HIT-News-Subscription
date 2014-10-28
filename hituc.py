# -*- coding: utf-8 -*-
import re
import time
import urllib
import urllib2

def hituc():
    m = re.findall(r"""/news/Show\S*
                    \s*target="_blank"\s*\S*\s*\S*\s*\S*\s*\S*
                   </a>""", urllib2.urlopen("http://hituc.hit.edu.cn").read(), re.M|re.X)

    d = re.findall(r"""class="news">\d*-\d*-\d*
                    &nbsp;</TD>""", urllib2.urlopen("http://hituc.hit.edu.cn").read(), re.M|re.X)

    
    #T = time.strftime('%Y/%m/%d', time.localtime(time.time()))
    T = "2014/9/22"

    URL = []
    j = 0
    for i in m:
        date = d[j][13:-11]
        j += 1
        print date
        date = date.replace('-', '/')
        if(cmp(date, T) != 0):
            continue
        l = i.split('target')
        url = "http://hituc.hit.edu.cn"
        url += l[0][0:-2]
        print url
        if(URL.count(url) != 0):
            continue
        URL.append(url)

        news = l[1][23:-4]
        print news
        
        
hituc()
