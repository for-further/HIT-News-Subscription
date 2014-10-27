# _*_ coding:utf-8 _*_
import web
import web.db
import sae.const
 
 
db = web.database(
    dbn='mysql',
    host=sae.const.MYSQL_HOST,
    port=int(sae.const.MYSQL_PORT),
    user=sae.const.MYSQL_USER,
    passwd=sae.const.MYSQL_PASS,
    db=sae.const.MYSQL_DB
)
  
def get_insert(_table, _openID, _keyword):
    return db.insert(_table, openID = _openID, keyword = _keyword)

def get_insert_news(_table, _url, _title):
    return db.insert(_table, url = _url, title = _title)

def get_all(_table):
    return db.select(_table, order = 'id')

def get_update(_table):
    db.update(_table, where="id = 1", openID = '456', keyword = "123,456,789")
    
def get_delete(_table, param):
	db.delete(_table, where=param)
    
def unique(old_list):
    #return self.render.reply_text(fromUser,toUser,int(time.time()), "123")
    newList = []   
    for x in old_list:
        if x not in newList:
            #return self.render.reply_text(fromUser,toUser,int(time.time()), x)
            newList.append(x)
    return newList