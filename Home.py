# -*- coding: utf-8 -*-
import re
import time
import urllib
import urllib2

def home():
    m = re.findall(r"""index.php\?
                    \S * #url
                    \s
                    \S *
                    \s
                    \S * #img><font
                    \s
                    \S*
                    </span>""", urllib.urlopen("http://home.hit.edu.cn").read(), re.M|re.X)

    #T = time.strftime('%m-%d', time.localtime(time.time()))
    T = "10-24"

    URL = []
    Title = []
    News = []
    for i in m:
        n = len(i)
        date = i[n-12:n-7]
        if(cmp(date, T) != 0):
            continue
        url = "http://home.hit.edu.cn"
        l = i.find("><img")
        url += i[0:l-1]
        cnt = URL.count(url)
        if(cnt != 0):
            continue
        URL.append(url)
        print url

        l = i.find("'white'>")
        r = i.find("</font>")
        news = i[l+8:r]
        Title.append(news)
        print news

        News.append(url + "$" + news)
    return News

home()
