# coding: UTF-8
import sae
import re
import hashlib
import web
import lxml
import time
import sys
import os
import urllib
import urllib2,json
import dbConnection as db

class testclass:
    def POST(self):
        S = web.input()
        #return S
        #return S.get('op')
        push_id = urllib.unquote(S.get('id'))
        word = urllib.unquote(S.get('word'))
        op = S.get('op')
        if op == '0':
            Key = []
            for user in db.get_all('keyword'):
                if (user.push_id == push_id):
                    Key.append(user.word)
            ans = []
            for news in db.get_all('news'):
                #return urllib.quote(news.title.encode('utf8'))
                if len(Key) == 0:
                    ans.append(news)
                    continue
                for x in Key:
                    if news.title.find(x) != -1 or news.word == x:
                        #return "123"
                        ans.append(news)
                        #ret += news.url + '$' + news.title + '$' + news.image + '$'
                        break
            
            ret = ""
            
            cnt = 0
            for news in ans[::-1]:
                cnt += 1
                ret += news.url + '$' + news.title + '$' + news.image + '$'
                if (cnt == 10):
                    break
            ret = urllib.quote(ret.encode('utf8'))             
            return ret
        elif op == '4':
            Key = []
            for user in db.get_all('keyword'):
                if (user.push_id == push_id):
                    Key.append(user.word)
            ans = []
            for news in db.get_all('news'):
                #return urllib.quote(news.title.encode('utf8'))
                for x in Key:
                    if news.title.find(x) != -1 or news.word == x or len(Key) == 0:
                        ans.append(news);
                        #ret += news.url + '$' + news.title + '$' + news.image + '$'
                        break;
            ret = ""
            
            if (word == 'new'):
                cnt = 0
                for news in ans[::-1]:
                    cnt += 1
                    ret += news.url + '$' + news.title + '$' + news.image + '$'
                    if (cnt == 10):
                        break
            else:
                cnt = 0
                flag = 0
                for news in ans[::-1]:
                    if (news.url == word):
                        flag = 1
                        continue
                    if flag == 0:
                        continue
                    if cnt == 10:
                        break
                    cnt += 1
                    ret += news.url + '$' + news.title + '$' + news.image + '$'
            ret = urllib.quote(ret.encode('utf8'))             
            return ret
        elif op == '1':
            flag = 0
            for user in db.get_all('keyword'):
                if (user.push_id == push_id) and (user.word == word):
                    flag = 1
                    break
            if flag == 1:
                return "failed"
            db.insert_keyword(push_id, word);
            return "succeed"
        elif op == '2':
            flag = 0
            for user in db.get_all('keyword'):
                if (user.push_id == push_id) and (user.word == word):
                    db.delete_keyword("id=" + str(user.id))
                    flag = 1
                    break
            if flag == 0:
                return "failed"
            return "succeed"
        elif op == '3':
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
            #return "123"
            for user in db.get_all('URL'):
                if user.push_id == push_id:
                    db.delete_URL("id=" + str(user.id))
            #return "123"
            if word == "":
                return "failed"
            word = word[0 : -1]
            l = word.split('$')
            for x in Site:
                if l.count(x) == 0:
                    db.insert_URL(x, push_id)
            return "succeed"
        elif op == '5':
            fktime = time.strftime('%Y/%m/%d %H:%M',time.localtime())
            db.insert_feedback(push_id, fktime, word)
            return "succeed"
        elif op == '6':
            for user in db.get_all('keyword'):
                if user.push_id == push_id:
                    db.delete_keyword("id=" + str(user.id))
            return "succeed"
        ret = urllib.quote(ret.encode('utf8'))