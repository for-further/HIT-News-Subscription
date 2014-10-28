# -*- coding: utf-8 -*-
import re
import time
import urllib
import urllib2


def studyathit():
        m = re.findall(r"""detail.asp\?id=
                        \d*"\s*
                        class="newstext"\s>·
                        \S*
                        </a>""", urllib.urlopen("http://studyathit.hit.edu.cn").read(), re.M|re.X)

        d = re.findall(r"""&nbsp;&nbsp;\d\d\d\d-\d\d-\d\d
                        </td>""", urllib.urlopen("http://studyathit.hit.edu.cn").read(), re.M|re.X)
        

        #T = time.strftime('%Y-%m-%d', time.localtime(time.time()))
        T = "2014-10-21"

        URL = []
        Title = []
        News = []
        j = 0
        for i in m:
                dat = d[j][-15:-5]
                j += 1
                if(cmp(dat, T) != 0):
                        continue
                date = dat[0:3] + "/" + dat[5:10]
                print date
                url = "http://studyathit.hit.edu.cn/"
                url += i[0:18]
                if(URL.count(url) != 0):
                        continue
                URL.append(url)
                print url

                l = i.split(" >")
                print len(l)
                news = l[1][2:-4]
                print news
                Title.append(news)

                News.append(url + "$" + news)

        print m
        print Title
        return News

studyathit()
