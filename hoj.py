# -*- coding: utf-8 -*-
import re
import time
import urllib
import urllib2


def hoj():
        m = re.findall(r"""<h3><a\shref="http://acm.hit.edu.cn/article/
                        \d*
                        \S*
                        </a>""", urllib.urlopen("http://acm.hit.edu.cn").read(), re.M|re.X)
        
        d = re.findall(r"""<span\sclass="time">\d\d\d\d-\d\d-\d\d
                        </span>""", urllib.urlopen("http://acm.hit.edu.cn").read(), re.M|re.X)
        
        
        #T = time.strftime('%Y-%m-%d', time.localtime(time.time()))
        T = "2014-09-26"

        URL = []
        Title = []
        News = []
        j = 0
        for i in m:
                date = d[j][-17:-7]
                j += 1
                #print date
                if(cmp(date, T) != 0):
                        continue
                url = i[13:47]
                if(URL.count(url) != 0):
                        continue
                URL.append(url)
                print url
                
                news = i[49:-4]
                print news
                Title.append(news)

                News.append(url + "$" + news)
        return News
    
hoj()
