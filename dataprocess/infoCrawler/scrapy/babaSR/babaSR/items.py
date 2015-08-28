# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

from scrapy.item import Item, Field


class BabaSRItem(Item):
    # define the fields for your item here like:
    title = Field()
    link = Field()
    source = Field()
    name = Field()
    picture = Field()
    #summary = Field()
    content = Field()
    price = Field()
    paylink = Field()
    setting = Field()
    tag = Field()
    # desc = Field()
