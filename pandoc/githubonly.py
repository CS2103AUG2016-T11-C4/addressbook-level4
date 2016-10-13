#!/usr/bin/env python3
from pandocfilters import toJSONFilter
import re

"""
Pandoc filter that causes everything between
'<!-- BEGIN GITHUB -->' and '<!-- END GITHUB -->'
to be ignored.  The comment lines must appear on
lines by themselves, with blank lines surrounding
them.
"""


incomment = False


def comment(k, v, fmt, meta):
    global incomment
    if k == 'RawBlock':
        fmt, s = v
        if fmt == "html":
            if re.search("<!-- BEGIN GITHUB -->", s):
                incomment = True
                return []
            elif re.search("<!-- END GITHUB -->", s):
                incomment = False
                return []
    if incomment:
        return []  # suppress anything in a comment


if __name__ == "__main__":
    toJSONFilter(comment)
