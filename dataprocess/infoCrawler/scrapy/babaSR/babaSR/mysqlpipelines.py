#encoding=utf-8
from twisted.enterprise import adbapi
import datetime
import chardet
import sys
import MySQLdb
import MySQLdb.cursors

from scrapy.conf import settings
from scrapy import log

class MySQLPipeline(object):

    def __init__(self):
        reload(sys)
        sys.setdefaultencoding('utf-8')
        self.dbpool = adbapi.ConnectionPool('MySQLdb', host=settings['DB_SERVER'], db=settings['DB_NAME'],
                user=settings['DB_USERNAME'], passwd=settings['DB_PASSWORD'], cursorclass=MySQLdb.cursors.DictCursor,
                charset='utf8', use_unicode=True)
  
    def process_item(self, item, spider):
        # run db query in thread pool
        # iteminfo=chardet.detect(''.join(item))
        # print 'process_item:',iteminfo
        query = self.dbpool.runInteraction(self._conditional_insert, item)
        query.addErrback(self.handle_error)
        return item
  
    def _conditional_insert(self, tx, item):
        # create record if doesn't exist.
        # all this block run on it's own thread
        tx.execute("select * from product_capture where link = %s", (item['link'][0], ))
        result = tx.fetchone()
        if result:
            log.msg("Item already stored in db: %s" % item, level=log.DEBUG)
        else:
            # titleCode=chardet.detect(item['title'])
            # print 'title:',item['title'],',titleCode:',titleCode
            # contentCode=chardet.detect(item['content'])
            # print 'content:',item['content'],',contentCode:',contentCode
            # print 'date:',datetime.datetime.now()
            tx.execute(\
                "insert into product_capture (title,link,source,name,type,picture,content,price,payLink,setting,tag,status,createTime) "
                "values (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)",
                (item['title'],item['link'],item['source'],item['name'],1,item['picture'],item['content'],item['price'],item['paylink'],item['setting'],item['tag'],0,
                 datetime.datetime.now())
            )
            log.msg("Item stored in db: %s" % item, level=log.DEBUG)
  
    def handle_error(self, e):
        log.err(e)


    def spider_closed(self, spider):
        self.collection.close()
