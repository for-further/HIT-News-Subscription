# -*- coding: utf-8 -*-
import re
import time
import urllib
import urllib2

def yzb():
    d = re.findall(r"""class="date">\d\d-\d\d</div>
                    """, urllib2.urlopen("http://yzb.hit.edu.cn").read(), re.M|re.X)

   
    m = re.findall(r"""/article/list/view/id/
                        \S*
                        \s*
                        title='
                        \S*\s*\S*
                        '>
                    """, urllib2.urlopen("http://yzb.hit.edu.cn").read(), re.M|re.X)

    
    #T = time.strftime('%m-%d', time.localtime(time.time()))
    T = "09-19"

   
    URL = []
    Title = []
    News = []
    j = 0
    for i in m:
        date = d[j][13:-6]
        j += 1
        if(cmp(date, T) != 0):
            continue
        url = "http://yzb.hit.edu.cn"
        url += i[0:25]
        if(URL.count(url) != 0):
            continue
        URL.append(url)
        print url

        k = i.split("title='")
        news = k[1][0:-2]
        print news

        News.append(url + "$" + news)
        
    return News

yzb()
