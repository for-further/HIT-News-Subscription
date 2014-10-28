# -*- coding: utf-8 -*-
import re
import time
#import urllib
import urllib2

def zsb ():
    m = re.findall(r"""news/user/News_Detail_use.asp
                        \S *
                        height=800,
                        \s
                        \S* #state
                        \s
                        \S* #menubar
                        \s
                        \S* #resizable
                        \s
                        \S* #scrollbars
                        \s
                        \S* #false
                        \s
                        \S* #target
                        \s
                        \S* #title
                        \s\S*\s*
                        </a>
                        \s*\S*
                        """, urllib2.urlopen("http://zsb.hit.edu.cn").read(), re.M|re.X)

        
    #T = time.strftime('%m.%d', time.localtime(time.time()))
    T = "9.29"

    URL = []
    Title = []
    News = []
    for i in m:
        dat = i[-5:-1]
        if(cmp(dat, T) != -1):
            continue
        d = T.replace('.', '-')
        date = "2014/"
        if(d[1] == '-'):date += "0"
        date += d
        print date
        url = "http://zsb.hit.edu.cn/"
        url += i[0:37]
        cnt = URL.count(url)
        if(cnt != 0):
           continue
        URL.append(url)
        print  url
        
        l = i.split(">")
        news = l[1][0:-3]
        print news

        News.append(url + "$" + news)
                
    return News
zsb()
