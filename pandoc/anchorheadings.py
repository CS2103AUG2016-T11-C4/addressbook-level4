#! /usr/bin/env python3
from panflute import *
import bs4
import sys


def anchor_headings(elem, doc):
    if type(elem) != Header:
        return
    identifier = elem.identifier
    for child in elem.content:
        if type(child) == RawInline and child.format == 'html':
            b = bs4.BeautifulSoup(child.text, 'html.parser')
            if not b.a or 'name' not in b.a.attrs:
                continue
            identifier = b.a['name']
    return Header(*elem.content, level=elem.level, identifier=identifier,
                  classes=elem.classes, attributes=elem.attributes)


if __name__ == '__main__':
    toJSONFilter(anchor_headings)
