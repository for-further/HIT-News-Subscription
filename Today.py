# -*- coding: utf-8 -*-
import re
import time
#import urllib
import urllib2

def today (key):
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
                    \s""", urllib2.urlopen("http://today.hit.edu.cn").read(), re.M|re.X)


        T = time.strftime('%Y/%m-%d', time.localtime(time.time()))
        #T = "2014/10-26"

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
                cnt = URL.count(url)
                if(cnt != 0):
                        continue
                URL.append(url)
                #print  url
        
                news = ""
                r = i.find("title=")
                news += i[r+7:n-2]
                Title.append(news)
                #print news

        k = 0
        for i in Title:
                for j in key:
                        print j
                        if(i.find(j) != -1):
                                print i, URL[k]
                                break
                k += 1
                


key = []
for i in range(0, 2):
        k = raw_input()
        key.append(k)
        
today(key)
