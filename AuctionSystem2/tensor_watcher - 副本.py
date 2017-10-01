# -*- coding: utf-8 -*-
"""
Created on Mon Sep 04 15:38:47 2017

@author: you
"""

import sys
import numpy as np
import tensorflow as tf

brut_data = sys.argv[1:]
data =[]
for bd in brut_data:
    data.append( bd.replace(',','.'))
    
#print (map(float, data))

#print (list((map(float, data))))


data = list(map(float, data))

tf.reset_default_graph()

input_size = len(data)
# Create some variables.
#W = tf.get_variable("W", shape=[input_size])
#b = tf.get_variable("b", shape=[2])

# Add ops to save and restore all the variables.

sess=tf.Session()    
saver = tf.train.import_meta_graph('./tmp/model.ckpt.meta')
saver.restore(sess,tf.train.latest_checkpoint('./tmp/'))

# Access saved Variables directly
#print(sess.run('W:0'))
#print(sess.run('b:0'))
 

graph = tf.get_default_graph()
x = graph.get_tensor_by_name("x:0") 
feed_dict ={x:[data]}

op_to_restore = graph.get_tensor_by_name("prediction:0")

print (sess.run(op_to_restore,feed_dict)[0])

"""
with tf.Session() as sess:
  # Restore variables from disk.
  saver.restore(sess, "/tmp/model.ckpt")
  print("Model restored.")
  # Check the values of the variables
  print("W : %s" % W.eval())
  print("b : %s" % b.eval())

"""



