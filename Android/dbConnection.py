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

def insert_keyword(_push_id, _word):
    db.insert("keyword", push_id = _push_id, word = _word)
def delete_keyword(param):
    db.delete("keyword", where = param)
def insert_news(_url, _title, _image, _date, _word):
    db.insert("news", url = _url, title = _title, image = _image, date = _date, word = _word)
def insert_URL(_url, _push_id):
    db.insert("URL", url = _url, push_id = _push_id)
def delete_URL(param):
    db.delete("URL", where = param)
def insert_push_time(_push_id, _time, _last_id):
    db.insert("push_time", push_id = _push_id, time = _time, last_id = _last_id)
def delete_push_time(param):
    db.delete("push_time", where = param)
def insert_feedback(_user, _fktime, _fkcontent):
    db.insert("feedback", user = _user, fktime = _fktime, fkcontent = _fkcontent)
def get_all(_table):
    return db.select(_table, order = 'id')