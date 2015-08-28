import re
#import json

from scrapy.selector import Selector
try:
    from scrapy.spider import Spider
except:
    from scrapy.spider import BaseSpider as Spider
#from scrapy.utils.response import get_base_url
from scrapy.contrib.spiders import CrawlSpider, Rule
from scrapy.contrib.linkextractors.sgml import SgmlLinkExtractor as sle
#from scrapy.contrib.linkextractors import LinkExtractor
from scrapy.http import Request
from babaSR.items import BabaSRItem
from babaSR.util import *

class DemohourSpider(CrawlSpider):
    name = "babaSR"
    allowed_domains = ["demohour.com"]
    start_urls = [
        "http://www.demohour.com/projects/latest",
        "http://www.demohour.com/projects/sell",
        "http://www.demohour.com/projects/presell"
    ]
    rules = [
        Rule(sle(allow=("/subject/\d+/?$")), callback='parse_2'),
        Rule(sle(allow=("/tag/[^/]+/?$", )), follow=True),
        Rule(sle(allow=("/tag/$", )), follow=True),
    ]
    
    # main parse method in Spider
    def parse(self, response):
        hxs = Selector(response)   
        newurls = hxs.xpath('//a/@href').extract()
        validurls = []
        items = [] 
        for url in newurls:   
            if 'http' not in url:
                url = 'http://www.demohour.com/' + url
            if ('projects' in url and 'url' not in url):
                regex=ur"[0-9]{6}"
                if re.search(regex, url):
                    validurls.append(url) 
            if 'projects/latest?attribute=online&page' in url:
                yield Request(url,callback=self.parse)
            
        uniqurls= list(set(validurls))
        print 'validurls:',uniqurls
        items.extend([self.make_requests_from_url(url).replace(callback=self.parse) for url in uniqurls])  
        

        sites = hxs.xpath('///div[@id="project_list"]/dl')
        items = []
        for site in sites:
            item = BabaSRItem()
            title= site.xpath('dt/a/text()').extract()[0]
            item['title'] = title.encode('utf-8')
            link = site.xpath('dt/a/@href').extract()[0]
            item['link'] = link.encode('utf-8')
            item['source'] = self.allowed_domains[0]
            items.append(item)
            print 'itemInfo:',item
            yield Request(item['link'],meta={'item':item},callback=self.parse_item)
        #return items  
    

    def parse_item(self,response):
        hxs =Selector(response)
        items = []
        item = response.meta['item']
        products = hxs.xpath('//div[@class="content"]')
        for product in products:
            # parseContent detail method
            parseTuple = self.parseContent(product)
            item['name'] = parseTuple[0]
            item['picture'] =parseTuple[1]
            item['content'] = parseTuple[2]
            item['price'] = parseTuple[3]
            item['paylink'] = parseTuple[4]
            item['setting'] = parseTuple[5]
            item['tag'] = parseTuple[6]
            #item['desc'] = parseTuple[7]
            #print 'finalItem:',item
            items.append(item)
        return items
    

    def parseContent(self,product):
        #print chardet.detect(content)
        name=product.xpath('div[@class="l1"]/div[@class="c12"]/div[@class="c43"]/div[@class="c40"]/text()').extract()[0].encode('utf-8')
        piclist=product.xpath('div[@class="l1"]/div[@class="c12"]/div[@class="c31"]/div[@class="c312"]/div/img/@src').extract()
        if len(piclist) > 0:
            piclink = piclist[0]
            if 'http' not in piclink:
                piclink ='http://www.demohour.com/' + ''.join(piclink)
            piclist.append(piclink.encode('utf-8'))
        else:
            piclist = None
        content = product.xpath('div[@class="l1"]/div[@class="c12"]/div[@class="c31"]/div[@id="project_content"]').extract()[0].encode('utf-8')
        productPiclist = product.xpath('//*[@id="project_content"]/p/img/@src').extract()
        piclist.extend(productPiclist)
        picture = str(piclist)
        content = filter_divtags(content)
        #print 'name:',name, 'picture:',picture
        price = 0.0
        paylink = None
        setting = None
        tag = None
        return (name,picture,content,price,paylink,setting,tag)

        
