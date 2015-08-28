# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html

import json
import codecs

class FilePipeline(object):

    def __init__(self):
        self.file = codecs.open('dataCapture.json', 'w', encoding='utf-8')

    def process_item(self, item, spider):
    	print 'process_item:',item
        line = json.dumps(dict(item), ensure_ascii=False) + "\n"
        #print 'process_item:',line
        self.file.write(line)
        return item

    def spider_closed(self, spider):
        self.file.close()

