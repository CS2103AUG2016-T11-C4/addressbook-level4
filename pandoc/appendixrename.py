#! /usr/bin/env python3
from panflute import *
import bs4
import sys
import re


def appendix_rename(elem, doc):
    if type(elem) != Header:
        return
    content = stringify(elem)
    match = re.match(r'[aA]ppendix [A-Z]:\s+', content)
    if not match:
        return
    return Header(Str(content[match.end():]), level=elem.level,
            identifier=elem.identifier, classes=elem.classes,
            attributes=elem.attributes)


if __name__ == '__main__':
    toJSONFilter(appendix_rename)
