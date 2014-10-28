# -*- coding: utf-8 -*-
import re
import time
import urllib
import urllib2

def hqjt():
    m = re.findall(r"""/news/show.asp\?id= #prefix
                    \d\d\d\d\s #id
                    target="_blank"> #
                    \S * #title
                    </a>
                    \s
                    <span
                    \s
                    style="color:\#aaa;font-size:10px;">
                    \d\d-\d\d
                    </span>""", urllib.urlopen("http://hqjt.hit.edu.cn/").read(), re.M|re.X)


    URL = []
    News = []
    #T = time.strftime('%Y/%m/%d', time.localtime(time.time()))
    T = "2014/10/24"

    for i in m:
        #print i
        date = "2014/" + i[-12:-7]
        date = date.replace('-', '/')
        print date
        if(cmp(date, T) != 0):
            continue
        url = "http://hqjt.hit.edu.cn"
        l = i.split(' target="_blank">')
        url += l[0]
        if(URL.count(url) != 0):
            continue
        URL.append(url)
        print url

        r = l[1].split("</a>")
        news= r[0]
        print news

        News.append(url + "$" + news)

    return News


hqjt()
