#!/usr/bin/env python3
from pandocfilters import toJSONFilter, RawBlock
import re

"""
Pandoc filter that causes the content of '<!-- BEGIN LATEX'
and 'END LATEX -->' to be converted into a raw LateX block.
"""

BEGIN_LATEX = '<!-- BEGIN LATEX'
END_LATEX = 'END LATEX -->'


def comment(k, v, fmt, meta):
    if k != 'RawBlock':
        return
    fmt, s = v
    if fmt != 'html':
        return
    if s.startswith(BEGIN_LATEX) and s.endswith(END_LATEX):
        content = s[len(BEGIN_LATEX):-len(END_LATEX)]
        return [RawBlock('latex', content)]


if __name__ == "__main__":
    toJSONFilter(comment)
