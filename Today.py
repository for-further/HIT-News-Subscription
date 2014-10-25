# -*- coding: utf-8 -*-
import re
import time
import urllib
import urllib2

f = open("Today.txt", "w+")

m = re.findall(r"""'/news/ + # date
                    \w * # 2014
                    / #/
                    \w\w #month
                    - #-
                    \w* #day
                    / #/
                    \w * #words
                    .htm'
                    \s # white space
                    title='
                    \S *
                    '
                    \s""", urllib.urlopen("http://today.hit.edu.cn").read(), re.M|re.X)


str = ""
T = time.strftime('%Y/%m-%d', time.localtime(time.time()))
#T = "2014/10-24"
URL = []
Title = []
for i in m:
        date = i[7:17]
        if(cmp(date, T) != 0):
                continue
        n = len(i)
        l = i.find("htm")
        url = "http://today.hit.edu.cn"
        url += i[1:l+3]
        URL.append(url)
        #print  url
        
        news = ""
        r = i.find("title=")
        news += i[r+7:n-2]
        Title.append(news)
        #print news

        str += news + "\n" + url + "\n\n"

#for i, j in enumerate(Title):
        #print j
f.write(str)
f.close()
