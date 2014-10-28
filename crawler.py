# -*- coding: utf-8 -*-
import re
import os
import sae
import web
import sys
import time
import urllib
import urllib2
import dbConnection as db

def clear():
    for x in db.get_all('News'): 
        db.get_delete('News', "id=" + str(x.id))
        
def today():
    m = re.findall(r"""<title><\S*\s*\S*\s*\S*
                    </title>""", urllib2.urlopen("http://today.hit.edu.cn/rss.xml").read(), re.M|re.X)

    link = re.findall(r"""<link><\S*\s*\S*\s*\S*
                    </link>""", urllib2.urlopen("http://today.hit.edu.cn/rss.xml").read(), re.M|re.X)

        
    T = time.strftime('%Y/%m/%d', time.localtime(time.time()))
    #T = "2014/10/27"

    URL = []
    for x in db.get_all('News'): 
        URL.append(x.url)
    j = 0
    for i in link:
        l = i.split("[CDATA[")
        date = l[1][29:39]
        date = date.replace('-', '/')
        if(cmp(date, T) != 0):
            continue
        r = l[1].split("]]>")
        url = r[0]
        if(URL.count(url) != 0):
            continue
        URL.append(url)
        x = m[j].decode('gbk').encode('utf8')
        title = x[16:-11]
        j += 1
        db.get_insert_news('News', str(url), str(title), date)
 
def acm():
    m = re.findall(r"""<h3><a\shref="http://acm.hit.edu.cn/article/
                    \d*
                    \S*
                    </a>""", urllib.urlopen("http://acm.hit.edu.cn").read(), re.M|re.X)
      
    d = re.findall(r"""<span\sclass="time">\d\d\d\d-\d\d-\d\d
                    </span>""", urllib.urlopen("http://acm.hit.edu.cn").read(), re.M|re.X)
        
        
    #T = time.strftime('%Y/%m/%d', time.localtime(time.time()))
    #T = "2014/09/26"

    URL = []
    for x in db.get_all('News'): 
        URL.append(x.url)
    j = 0
    for i in m:
        date = d[j][-17:-7]
        date = date.replace('-', '/')
        j += 1
        #print date
        #if(cmp(date, T) != 0):
        #    continue
        url = i[13:47]
        if(URL.count(url) != 0):
            continue
        URL.append(url)
                
        title = i[49:-4]
        db.get_insert_news('News', str(url), str(title), date)
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


    T = time.strftime('%Y/%m/%d', time.localtime(time.time()))
    #T = "2014/10/24"

    URL = []
    for x in db.get_all('News'): 
        URL.append(x.url)
    for x in m:
        i = x.decode('gbk').encode('utf8')
        date = i[11:21]
        date = date.replace('-', '/')
        j = i.split(' target="_blank">')
        if(cmp(date, T) != 0):
            continue

        url = "http://news.hit.edu.cn"
        url += j[0][1:-1]
        cnt = URL.count(url)
        if(cnt != 0):
            continue
        URL.append(url)
        
        title = j[1][0:-4]
        db.get_insert_news('News', str(url), str(title), date)

def studyathit():
    m = re.findall(r"""detail.asp\?id=
                    \d*"\s*
                    class="newstext"\s>¡¤
                    \S*
                    </a>""", urllib.urlopen("http://studyathit.hit.edu.cn").read(), re.M|re.X)

    d = re.findall(r"""&nbsp;&nbsp;\d\d\d\d-\d\d-\d\d
                    </td>""", urllib.urlopen("http://studyathit.hit.edu.cn").read(), re.M|re.X)
        

    T = time.strftime('%Y/%m/%d', time.localtime(time.time()))
    #T = "2014/10/21"

    URL = []
    for x in db.get_all('News'): 
        URL.append(x.url)
    j = 0
    for i in m:
        date = d[j][-15:-5]
        date = date.replace('-', '/')
        j += 1
        if(cmp(date, T) != 0):
            continue
        url = "http://studyathit.hit.edu.cn/"
        url += i[0:18]
        if(URL.count(url) != 0):
            continue
        URL.append(url)

        l = i.split(" >")
        #x = l[1].decode('GB2312').encode('utf8')
        title = l[1][2:-4]
        db.get_insert_news('News', str(url), title, date)
        
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

    
    #T = time.strftime('%Y/%m/%d', time.localtime(time.time()))
    T = "2014/09/19"

   
    URL = []
    for x in db.get_all('News'): 
        URL.append(x.url)
    j = 0
    for i in m:
        date = "2014/" + d[j][13:-6]
        date = date.replace('-', '/')
        j += 1
        if(cmp(date, T) != 0):
            continue
        url = "http://yzb.hit.edu.cn"
        url += i[0:25]
        if(URL.count(url) != 0):
            continue
        URL.append(url)
        
        k = i.split("title='")
        title = k[1][0:-2]
        db.get_insert_news('News', str(url), title, date)

def zsb ():
    m = re.findall(r"""news/user/News_Detail_use.asp
                        \S *
                        height=800,
                        \s
                        \S* #state
                        \s
                        \S* #menubar
                        \s
                        \S* #resizable
                        \s
                        \S* #scrollbars
                        \s
                        \S* #false
                        \s
                        \S* #target
                        \s
                        \S* #title
                        \s\S*\s*
                        </a>
                        \s*\S*
                        """, urllib2.urlopen("http://zsb.hit.edu.cn").read(), re.M|re.X)

        
    T = time.strftime('%m.%d', time.localtime(time.time()))
    #T = "9.29"

    URL = []
    for x in db.get_all('News'): 
        URL.append(x.url)
    for i in m:
        dat = i[-5:-1]
        if(cmp(dat, T) != 0):
            continue
        d = T.replace('.', '/')
        date = "2014/"
        if(d[1] == '/'):date += "0"
        date += d
        url = "http://zsb.hit.edu.cn/"
        url += i[0:37]
        cnt = URL.count(url)
        if(cnt != 0):
           continue
        URL.append(url)
        
        l = i.split(">")
        
        title = l[1][0:-3].decode('gbk').encode('utf8')
        db.get_insert_news('News', str(url), title, date)

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


    T = time.strftime('%Y/%m/%d', time.localtime(time.time()))
    #T = "2014/10/24"
    URL = []
    for x in db.get_all('News'): 
        URL.append(x.url)
    for i in m:
        date = "2014/" + i[-12:-7]
        date = date.replace('-', '/')
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
        title = r[0].decode('gbk').encode('utf8')

        db.get_insert_news('News', str(url), title, date)

def ygb():
    m = re.findall(r"""/news/show
                    \S*
                    \s\s*
                    \S*\s*\S*\s*\S*\s*\S*"
                    >""", urllib.urlopen("http://ygb.hit.edu.cn").read(), re.M|re.X)

    mm = re.findall(r"""
                \(\s
                \S *
                \s*\)""", urllib.urlopen("http://ygb.hit.edu.cn").read(), re.M|re.X)

    
    T = time.strftime('%Y/%m/%d', time.localtime(time.time()))
    #T = "2014/10/24"


    URL = []
    for x in db.get_all('News'): 
        URL.append(x.url)
    j = 0
    for i in m:
        date = mm[j][2:-2]
        j += 1
        date = date.replace('-', '/')
        if(cmp(date, T) != 0):
            continue
        url = "http://ygb.hit.edu.cn"
        l = i.split(" title=")
        url += l[0][0:-1]
        if(URL.count(url) != 0):
            continue
        URL.append(url)

        title = l[1][1:-2]
        db.get_insert_news('News', str(url), title, date)

def usp():
    m = re.findall(r"""showXinWen_XX.asp\?MC=\S*
                    \s*\S*\s*title\s*=\s*\S*
                    \s*\S*\s*\S*\s*\S*\s*\S*\s*\S*\s*\S*\s*\S*\s*\S*\s*\S*\s*\S*\s*\S*\s*\S*\s*\S*\s*\S*\s*\S*
                    </font>""", urllib2.urlopen("http://www.usp.com.cn").read(), re.M|re.X)


    T = time.strftime('%Y/%m/%d', time.localtime(time.time()))
    #T = "2014/10/16"

    URL = []    
    for x in db.get_all('News'): 
        URL.append(x.url)
    for x in m:
        i = x.decode('gbk').encode('utf8')
        date = i[-17:-7]
        date = date.replace('-', '/')
        print date
        if(cmp(date, T) != 0):
            continue
        url = "http://www.usp.com.cn/"
        j = i.split("' target='_blank'  title = '")
        url += j[0]
        if(URL.count(url) != 0):
            continue
        URL.append(url)
        
        k = j[1].split("><font color=")
        title = k[0][0:-2]
        db.get_insert_news('News', str(url), title, date)

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


    T = time.strftime('%Y/%m/%d', time.localtime(time.time()))
    #T = "2014/10/24"
        
    URL = []
    for x in db.get_all('News'): 
        URL.append(x.url)
    j = 0
    for i in m:
        date = mm[j][13:-6]
        j += 1
        date = date.replace('-', '/')
        if(cmp(date, T) != 0):
            continue
        i = i.split(' target="_blank" class="news">')
        url = "http://jwc.hit.edu.cn"
        url += i[0][9:-1]
        if(URL.count(url) != 0):
            continue
        URL.append(url)

        title = i[1][0:-4].decode('gbk').encode('utf8')
        db.get_insert_news('News', str(url), title, date)
        
def hituc():
    m = re.findall(r"""/news/Show\S*
                    \s*target="_blank"\s*\S*\s*\S*\s*\S*\s*\S*
                   </a>""", urllib2.urlopen("http://hituc.hit.edu.cn").read(), re.M|re.X)

    d = re.findall(r"""class="news">\d*-\d*-\d*

                    &nbsp;</TD>""", urllib2.urlopen("http://hituc.hit.edu.cn").read(), re.M|re.X)

    
    T = time.strftime('%Y/%m/%d', time.localtime(time.time()))
    #T = "2014/9/22"

    URL = []
    for x in db.get_all('News'): 
        URL.append(x.url)
    j = 0
    for i in m:
        date = d[j][13:-11]
        j += 1
        date = date.replace('-', '/')
        if(cmp(date, T) != 0):
            continue
        l = i.split('target')
        url = "http://hituc.hit.edu.cn"
        url += l[0][0:-2]
        if(URL.count(url) != 0):
            continue
        URL.append(url)

        title = l[1][23:-4].decode('gbk').encode('utf8')
        db.get_insert_news('News', str(url), title, date)
        