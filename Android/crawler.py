# coding: UTF-8
import re
import os
import sae
import web
import sys
import time
import urllib
import urllib2,json
import lxml
import hashlib
import string
import dbConnection as db
from push import *
def my_urlencode(str):
    #reprStr = repr(str).replace(r'\x','%')
    #print reprStr
    #return reprStr[1:-1]
    return urllib.quote(str.encode('utf8'))

def clearTag(text):
    p = re.compile(u'<[^>]+>')
    retval = p.sub("",text)
    return retval


def get_res_list_and_next_page_url(target_url):
    res = urllib2.urlopen(target_url)
    f = res.info()
    #print f.getparam('charset')
    #print target_url
    html=res.read()
    #return html
    #print isinstance(html, unicode)
    #获取res_list
    pattern = re.compile(r'\<!-- a.*\>\s[\s\S]*?\<!-- z')
    res_lists = pattern.findall(html)
    #for i in res_lists:
        #print i
    
    #获取next_page_url
    next_page_url = ''
    pattern = re.compile(r'<a id="sogou_next"[\s\S]*class="np">')
    m = pattern.search(html)
    if m:
        res = m.group()
        res = res.replace('<a id="sogou_next" href="', '')
        j = res.split('"')
        next_page_url = 'http://www.sogou.com/web' + j[0]
    return res_lists,next_page_url


def get_title_and_url(div_data):
    #print div_data
    pattern = re.compile(r'sogou_preview_title="[\s\S]*" url="')
    m = pattern.search(div_data)
    title = ''
    url = ''
    if m:
        res = clearTag(m.group(0))
        j = res.split('" sogou_preview_link="')
        title = j[0].replace('sogou_preview_title="', '')
        url = j[1].replace('" url="', '')
    #else:
        #print 'Title Search Error!'
    #print chardet.detect(u_str)["encoding"]
    return title, url


def get_date(div_data):
    pattern = re.compile(r'<cite id="cacheresult_info_[\s\S]*</cite>')
    m = pattern.search(div_data)
    date = ''
    year = 0
    month = 0
    day = 0
    if m:
        res = m.group(0)
        pattern = re.compile(r'\d*-\d*-\d*')
        mm = pattern.search(res)
        if mm:
            date = mm.group(0)
            date = date.replace('-', '/')
        else:
            date = time.strftime('%Y/%m/%d', time.localtime(time.time()))
        j = date.split('/')
        year = int(j[0])
        month = int(j[1])
        day = int(j[2])
    #print year, month, day, date
    return year * 600 + month * 40 + day;


def get_abstract(div_data):
    #print div_data
    pattern = re.compile(r'<!--summary_beg-->[\s\S]*<!--summary_end-->')
    m = pattern.search(div_data)
    pattern2 = re.compile(r'<p class="str_info">[\s\S]*</p>')
    mm = pattern2.search(div_data)
    pattern3 = re.compile(r'<div class="ft" id="cacheresult_summary_[\s\S]*<div class="fb">')
    mmm = pattern3.search(div_data)
    abstract = ''
    if m:
        abstract = clearTag(m.group(0))
       # print '!'
    elif mm:
        abstract = clearTag(mm.group(0))
    elif mmm:
        abstract = clearTag(mmm.group(0))
    #else : 
        #print 'Abstract Search Error!'
       
    return abstract


def get_photo(div_data):
    pattern = re.compile(r' id="sogou_vr_30000909_pic_img_[\s\S]*onerror="')
    m = pattern.search(div_data)
    photo = ''
    if m:
        res = m.group(0)
        j = res.split('src="')
        photo = j[1][0:-11]
        photo = photo.replace('amp;', '')
    else:
        photo = 'NONE'
        
    return photo


def sogou(Key, Site):
    #return "123"
    user = []
    tit = []
    ur =[]
    for keyword in Key:
        for site in Site:
            url = 'http://www.sogou.com/web?q=' + my_urlencode(keyword) + '&query=%22' + my_urlencode(keyword) + '%22++site%3A' + site + '&fieldtitle=&fieldcontent=&fieldstripurl=&bstype=&ie=utf8&include=checkbox&sitequery=' + site + '&located=0&tro=on&filetype=&num=10'
            #print url
            target_urls = []
            target_urls.append(url)

            page_num = 10 
            URL = []
            for x in db.get_all('news'): 
                URL.append(x.url)
            for cnt in range(page_num):
                #print "===============第",cnt+1,"页==============="

                if target_urls[cnt] == "END_FLAG":
                    break
                #return  get_res_list_and_next_page_url(target_urls[cnt])
                res_lists,next_page_url = get_res_list_and_next_page_url(target_urls[cnt])
                #return url + '\n' + next_page_url
                if next_page_url: #考虑没有“下一页”的情况
                    target_urls.append(next_page_url)
                else:
                    target_urls.append("END_FLAG")

                
                for index in range(len(res_lists)):
                    #print "第",index+1,"个搜索结果..."
            
                    #获取title和url
                    news, url = get_title_and_url(res_lists[index])
                    #print "标题：", news
                    #print "URL：", url
                    if news == 'Sheet1' or news == '后勤集团' or news.find('综合信息服务') != -1:
                        continue
                    if url.find('depart') != -1:
                        continue
                    if(URL.count(url) != 0):
                        continue
                    URL.append(url)
                    #获取日期
                    date = get_date(res_lists[index])
                    day = time.strftime('%Y/%m/%d', time.localtime(time.time()))
                    j = day.split('/')
                    today = int(j[0]) * 600 + int(j[1]) * 40 + int(j[2])
                    if date < today - 1:
                        target_urls[cnt + 1] = "END_FLAG"
                        break
                    #print "日期：",date
                    
                
                    #获取描述
                    abstract = get_abstract(res_lists[index])
                    #print "概要：",abstract
                     
                    #获取图片URL
                    photo = get_photo(res_lists[index])
                    #print "photo: ", photo
                    
                    #print "\r\n\r\n"
                    db.insert_news(url, news, photo, date, keyword)
                    for x in db.get_all('keyword'):
                        if (x.word == keyword) and (user.count(x.push_id) == 0):
                            user.append(x.push_id)
                            tit.append(news + '$' + url)
                            ur.append(url);
    '''
    minute = time.strftime('%Y/%m/%d/%H/%M/%S', time.localtime(time.time()))
    j = minute.split('/')
    now = int(j[0]) * 365 * 24 * 60 + int(j[1]) * 30 * 24 * 60 + int(j[2]) * 24 * 60 + int(j[3]) * 60 + int(j[4])
    res = ""
    for i in range(0, len(user)):
        res = res + user[i] + '\n'
        t = 0
        p = 0
        for x in db.get_all('push_time'):
            if x.push_id == user[i]:
                p = x.id
                t = x.time
                break;
        flag = 0
        for x in db.get_all('URL'):
            if x.push_id == user[i] and ur[i].find(x.url) != -1:
                flag = 1
                break
        if flag == 0:
            continue
        #return str(t) + '\n' + str(now)
        if now - t >= 60:
            #return user[i] + '\n' + tit[i]
            push_news(user[i], tit[i])
        if t > 0:
            db.delete_push_time("id = " + str(p)) 
        db.insert_push_time(user[i], now, 0)
    
    return res
    '''

def GO(Key):
    Site = []
    #__devilcomment = '''
    Site.append('today.hit.edu.cn')
    Site.append('news.hit.edu.cn')
    Site.append('hqjt.hit.edu.cn')
    Site.append('hituc.hit.edu.cn')
    Site.append('jwc.hit.edu.cn')
    Site.append('acm.hit.edu.cn')
    Site.append('studyathit.hit.edu.cn')
    Site.append('yzb.hit.edu.cn')
    Site.append('zsb.hit.edu.cn')
    Site.append('ygb.hit.edu.cn')
    Site.append('www.usp.com.cn')

    #Site.append('zsb.hit.edu.cn')
    return sogou(Key, Site)
    
def init():
    ret = ""
    Key = []
    for key in db.get_all('keyword'):
        if Key.count(key.word) == 0:
            Key.append(key.word)
            ret += key.word
    #Key.append('计算机学院')
    return GO(Key)
    return ret
