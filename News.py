# -*- coding: utf-8 -*-
import re
import time
import urllib
import urllib2

f = open("News.txt", "w+")

m = re.findall(r""""/articles/ + # date
                    \w * # 2014
                    / #/
                    \w\w #month
                    - #-
                    \w* #day
                    / #/
                    \w * #words
                    .htm"
                    \s # white space
                    target="_blank">
                    \S *
                    </a>""", urllib.urlopen("http://news.hit.edu.cn").read(), re.M|re.X)


#print m
str = ""
T = time.strftime('%Y/%m-%d', time.localtime(time.time()))
#T = "2014/10-24"

URL = []
Title = []
for i in m:
        date = i[11:21]
        #print date
        if(cmp(date, T) != 0):
            continue

        url = "http://news.hit.edu.cn"
        l = i.find("htm")
        url += i[1:l+3]
        cnt = URL.count(url)
        if(cnt != 0):
            continue
        URL.append(url)
        print url

        n = len(i);
        r = i.find('"_blank">')
        news = i[r+9:n-4]
        print news
        Title.append(news)

        str += url + "\n" + news + "\n\n"
            

#for i, j in enumerate(Title):
        #print j
f.write(str)
f.close()
