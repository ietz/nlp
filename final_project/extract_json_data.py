#!/usr/bin/env python3
# -*- coding: utf-8 -*-
import os
import json
import collections

"""
Created on Tue Dec 18 16:31:39 2018

@author: inga
"""

def main():
    path = "/home/inga/Documents/Uni/NLP4WEB/FakeNewsNet/Data/Newsstuff/" 
    result = dict()
    for file in os.listdir(path):
        f = open(path+file, 'r')
        data = json.load(f)
        dstruct = structure(data)
        dict_merge(result, dstruct)
    print(result)
        
def structure(obj):
    if isinstance(obj, dict):
        result = dict()    
        for key, value in obj.items():
            result[key] = structure(value)
        return result
    else:
        return str(type(obj))
    
def dict_merge(dct, merge_dct):
    """ Recursive dict merge. Inspired by :meth:``dict.update()``, instead of
    updating only top-level keys, dict_merge recurses down into dicts nested
    to an arbitrary depth, updating keys. The ``merge_dct`` is merged into
    ``dct``.
    :param dct: dict onto which the merge is executed
    :param merge_dct: dct merged into dct
    :return: None
    """
    for k, v in merge_dct.items():
        if (k in dct and isinstance(dct[k], dict)
                and isinstance(merge_dct[k], collections.Mapping)):
            dict_merge(dct[k], merge_dct[k])
        else:
            dct[k] = merge_dct[k]
    
if __name__ == "__main__":
    main()