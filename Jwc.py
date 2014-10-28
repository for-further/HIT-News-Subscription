# -*- coding: utf-8 -*-
import re
import time
import urllib
import urllib2

def jwc():
    m = re.findall(r"""<a\shref="/news/Showgg.asp\?id=
                   \d\d\d\d"
                   \s
                   \S * #target
                   \s
                   \S*
                   </a>""", urllib.urlopen("http://jwc.hit.edu.cn").read(), re.M|re.X)


        
    mm = re.findall(r"""class="news">\d\d\d\d-\d*-\d* #date
                    &nbsp;""", urllib.urlopen("http://jwc.hit.edu.cn").read(), re.M|re.X)


    #T = time.strftime('%Y/%m/%d', time.localtime(time.time()))
    T = "2014/10/24"
        
    URL = []
    j = 0
    for i in m:
        date = mm[j][13:-6]
        j += 1
        date = date.replace('-', '/')
        if(cmp(date, T) != 0):
            continue
        print date
        i = i.split(' target="_blank" class="news">')
        url = "http://jwc.hit.edu.cn"
        url += i[0][9:-1]
        print url
        if(URL.count(url) != 0):
            continue
        URL.append(url)

        news = i[1][0:-4]
        print news
        


jwc()
