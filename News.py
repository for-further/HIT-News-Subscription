# -*- coding: utf-8 -*-
import re
import time
import urllib
import urllib2


def news():
        m = re.findall(r""""/articles/ + # date
                    \w * # 2014
                    / #/
                    \w\w #month
                    - #-
                    \w* #day
                    / #/
                    \w * #words
                    .htm"
                    \s* # white space
                    target="_blank">
                    \S*\s*\S*\s*\S*
                    </a>""", urllib.urlopen("http://news.hit.edu.cn").read(), re.M|re.X)


        #T = time.strftime('%Y/%m-%d', time.localtime(time.time()))
        T = "2014/10-24"

        URL = []
        News = []
        for i in m:
            date = i[11:21]
            j = i.split(' target="_blank">')
           # print j
            #print date
            if(cmp(date, T) != 0):
                continue

            url = "http://news.hit.edu.cn"
            url += j[0][1:-1]
            cnt = URL.count(url)
            if(cnt != 0):
                continue
            URL.append(url)
            print url

            nnews = j[1][0:-4]
            print nnews

            News.append(url + "$" + nnews)
        return News

news()
